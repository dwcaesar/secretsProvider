package de.wohlers.secretsprovider.secretsprovider;

import lombok.Getter;

@Getter
public class SecretsProviderException extends RuntimeException {

    private final Reason reason;

    public SecretsProviderException(String message, Reason reason) {
        this(message, reason, null);
    }

    public SecretsProviderException(String message, Reason reason, Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }

    public enum Reason {
        INVALID_SOURCE, INVALID_CONFIG, NO_CONFIGURATION
    }
}
