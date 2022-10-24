package de.wohlers.secretsprovider.secretsprovider.aws;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("secrets-provider.aws")
public class AWSSecretsProviderSettings {

    private String regionName;
    private String applicationName;
}
