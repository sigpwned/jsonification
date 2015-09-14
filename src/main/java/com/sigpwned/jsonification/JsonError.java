package com.sigpwned.jsonification;

public class JsonError extends RuntimeException {
    private static final long serialVersionUID = -4418354848118087690L;

    public JsonError(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonError(String message) {
        super(message);
    }

    public JsonError(Throwable cause) {
        super(cause);
    }
}
