package de.wohlers.secretsprovider.secretsprovider.aws;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.AWSSecretsManagerException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.util.StringInputStream;
import de.wohlers.secretsprovider.secretsprovider.SecretsProvider;
import de.wohlers.secretsprovider.secretsprovider.SecretsProviderException;
import de.wohlers.secretsprovider.secretsprovider.SecretsProviderSettings;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class AWSSecretsProvider extends SecretsProvider {

    private AWSSecretsManager client;

    public AWSSecretsProvider(SecretsProviderSettings settings) throws SecretsProviderException {
        super(settings);
    }

    protected void init() {
        verifySettings();
        initClient();
    }

    private void verifySettings() throws SecretsProviderException {
        AWSSecretsProviderSettings aws = settings.getAws();
        if (aws == null) {
            log.error("no configuration for aws secrets provider");
            throw new SecretsProviderException("no configuration for aws secrets provider", SecretsProviderException.Reason.NO_CONFIGURATION);
        }
        if (aws.getRegionName() == null && aws.getRegionName().isBlank()) {
            log.error("no region name specified");
            throw new SecretsProviderException("no region name specified", SecretsProviderException.Reason.INVALID_CONFIG);
        }
        if (aws.getApplicationName() == null && aws.getApplicationName().isBlank()) {
            log.error("no application name specified");
            throw new SecretsProviderException("no application name specified", SecretsProviderException.Reason.INVALID_CONFIG);
        }
    }

    private void initClient() {
        client = AWSSecretsManagerClientBuilder
                .standard()
                .withRegion(settings.getAws().getRegionName())
                .build();
    }

    @Override
    protected InputStream readSecrets() throws SecretsProviderException {
        GetSecretValueRequest request = new GetSecretValueRequest().withSecretId(settings.getAws().getApplicationName());
        try {
            GetSecretValueResult result = client.getSecretValue(request);
            return new StringInputStream(result.getSecretString());
        } catch (AWSSecretsManagerException e) {
            log.warn("failed to retrieve secrets from aws", e);
            throw new SecretsProviderException("failed to retrieve secrets from aws", SecretsProviderException.Reason.INVALID_SOURCE, e);
        } catch (IOException e) {
            log.warn("failed to read secrets from aws", e);
            throw new SecretsProviderException("failed to read secrets from aws", SecretsProviderException.Reason.INVALID_SOURCE, e);
        }
    }
}
