package com.sigpwned.jsonification.exception;

import com.sigpwned.jsonification.JsonException;

public class NullJsonException extends JsonException {
    private static final long serialVersionUID = -1610912978778101961L;

    public NullJsonException() {
        super("null");
    }
}
