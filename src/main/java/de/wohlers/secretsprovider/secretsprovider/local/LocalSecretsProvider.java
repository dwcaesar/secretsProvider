package de.wohlers.secretsprovider.secretsprovider.local;

import de.wohlers.secretsprovider.secretsprovider.SecretsProvider;
import de.wohlers.secretsprovider.secretsprovider.SecretsProviderException;
import de.wohlers.secretsprovider.secretsprovider.SecretsProviderSettings;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalSecretsProvider extends SecretsProvider {

    public LocalSecretsProvider(SecretsProviderSettings settings) throws SecretsProviderException {
        super(settings);
    }

    @Override
    protected void init() {
        // not necessary for local files
    }

    @Override
    protected InputStream readSecrets() throws SecretsProviderException {
        try {
            final Path path = verifyAndGetPath();
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new SecretsProviderException("cannot read file ", SecretsProviderException.Reason.INVALID_SOURCE, e);
        }
    }

    private Path verifyAndGetPath() throws SecretsProviderException {
        if (settings.getLocal() == null) {
            throw new SecretsProviderException("no configuration for local secrets provider", SecretsProviderException.Reason.INVALID_CONFIG);
        }
        final String path = settings.getLocal().getPath();
        if (path == null || path.isBlank()) {
            throw new SecretsProviderException("path to local secrets file is not specified", SecretsProviderException.Reason.INVALID_CONFIG);
        }
        return FileSystems.getDefault().getPath(path).normalize().toAbsolutePath();
    }
}
