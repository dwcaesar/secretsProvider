package de.wohlers.secretsprovider.secretsprovider;

import de.wohlers.secretsprovider.secretsprovider.aws.AWSSecretsProviderSettings;
import de.wohlers.secretsprovider.secretsprovider.local.LocalSecretsProviderSettings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("secrets-provider")
public class SecretsProviderSettings {
    // generic settings should be modelled as fields
    private Long updateIntervall; // in hours
    private SecretsProviderType type;

    // implementation dependent settings should be modelled as a complex objects
    private AWSSecretsProviderSettings aws;
    private LocalSecretsProviderSettings local;

    public enum SecretsProviderType {
        /**
         * Selects AWS Secrets Provider, which reads secrets from AWS Secrets Manager
         */
        AWS,
        /**
         * <p><strong>use only for tests</strong></p>
         *
         * <p>Selects Local Secrets Provider, which reads secrets from a local file</p>
         *
         * @deprecated use only for tests
         */
        @Deprecated LOCAL
    }
}
