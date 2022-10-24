package de.wohlers.secretsprovider.secretsprovider;

import de.wohlers.secretsprovider.secretsprovider.aws.AWSSecretsProvider;
import de.wohlers.secretsprovider.secretsprovider.local.LocalSecretsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SecretsProviderBuilder {

    private final SecretsProviderSettings settings;

    @Bean
    public SecretsProvider secretsProvider() {
        SecretsProvider secretsProvider;

        switch (settings.getType()) {
            case AWS -> secretsProvider = new AWSSecretsProvider(settings);
            case LOCAL -> secretsProvider = new LocalSecretsProvider(settings);
            default ->
                    throw new SecretsProviderException("unsupported secrets provider: " + settings.getType(), SecretsProviderException.Reason.NO_CONFIGURATION);
        }

        return secretsProvider;
    }

}
