package de.wohlers.secretsprovider.secretsprovider;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("secrets")
public class SecretProviderSettings {
}
