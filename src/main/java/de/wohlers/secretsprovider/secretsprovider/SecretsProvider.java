package de.wohlers.secretsprovider.secretsprovider;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public abstract class SecretsProvider {

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    protected final SecretsProviderSettings settings;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final Properties secrets = new Properties();

    public SecretsProvider(SecretsProviderSettings settings) throws SecretsProviderException {
        this.settings = settings;
        init();
        updateSecrets();
        initScheduledTask();
    }

    protected abstract void init();

    private void initScheduledTask() {
        executorService.scheduleAtFixedRate(this::updateSecrets, settings.getUpdateIntervall(), settings.getUpdateIntervall(), TimeUnit.HOURS);
    }

    private void updateSecrets() throws SecretsProviderException {
        storeSecrets(readSecrets());
    }

    abstract protected InputStream readSecrets() throws SecretsProviderException;

    private void storeSecrets(InputStream inputStream) {
        Map<Object, Object> backup = null;
        try {
            readWriteLock.writeLock().lock();
            backup = Map.copyOf(secrets);
            secrets.clear();
            secrets.load(inputStream);
        } catch (IOException e) {
            log.warn("failed to refresh secrets", e);
            if (backup != null && !backup.isEmpty()) {
                secrets.putAll(backup);
            } else {
                log.error("could not restore previous state: no secrets");
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public final String get(String key) {
        try {
            readWriteLock.readLock().lock();
            return (String)secrets.get(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
