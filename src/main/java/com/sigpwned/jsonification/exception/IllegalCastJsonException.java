package com.sigpwned.jsonification.exception;

import com.sigpwned.jsonification.JsonException;
import com.sigpwned.jsonification.JsonValue;

public class IllegalCastJsonException extends JsonException {
    private static final long serialVersionUID = -3854318051038016222L;
    
    private final JsonValue.Type objectType;
    private final JsonValue.Type castedType;
    
    public IllegalCastJsonException(JsonValue.Type objectType, JsonValue.Type castedType) {
        super("Attempted illegal cast from "+objectType+" to "+castedType);
        this.objectType = objectType;
        this.castedType = castedType;
    }

    public JsonValue.Type getObjectType() {
        return objectType;
    }

    public JsonValue.Type getCastedType() {
        return castedType;
    }
}
