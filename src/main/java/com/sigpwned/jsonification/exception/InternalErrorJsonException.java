package com.sigpwned.jsonification.exception;

import com.sigpwned.jsonification.JsonException;

public class InternalErrorJsonException extends JsonException {
    private static final long serialVersionUID = -7504776750575187884L;

    public InternalErrorJsonException(String message) {
        super(message);
    }

    public InternalErrorJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
