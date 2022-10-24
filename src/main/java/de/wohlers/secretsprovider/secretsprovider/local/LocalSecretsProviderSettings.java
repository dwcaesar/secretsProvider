package de.wohlers.secretsprovider.secretsprovider.local;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("secrets-provider.local")
public class LocalSecretsProviderSettings {

    /**
     * Path to a locally stored properties file with secrets.
     */
    private String path;
}
