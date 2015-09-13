package com.sigpwned.jsonification;

public class JsonException extends RuntimeException {
    private static final long serialVersionUID = 1724169517244320354L;

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(String message) {
        super(message);
    }
}
