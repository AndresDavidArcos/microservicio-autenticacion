package co.com.pragma.api.exception;

import co.com.pragma.model.exception.ApiException;

public class ConfigurationException extends ApiException {
    public ConfigurationException(String message) {
        super(message);
    }
}
