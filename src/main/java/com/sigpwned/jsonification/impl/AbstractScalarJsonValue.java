package com.sigpwned.jsonification.impl;

import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.exception.IllegalScalarCastJsonException;
import com.sigpwned.jsonification.value.ScalarJsonValue;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;
import com.sigpwned.jsonification.value.scalar.JsonNumber;
import com.sigpwned.jsonification.value.scalar.JsonString;

/* default */ abstract class AbstractScalarJsonValue extends AbstractJsonValue implements ScalarJsonValue {
    private final Object value;
    
    public AbstractScalarJsonValue(Object value) {
        if(value == null)
            throw new NullPointerException();
        this.value = value;
    }
    
    @Override
    public JsonValue.Type getType() {
        return JsonValue.Type.SCALAR;
    }
    
    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public JsonNumber asNumber() {
        throw new IllegalScalarCastJsonException(getFlavor(), ScalarJsonValue.Flavor.NUMBER);
    }

    @Override
    public JsonBoolean asBoolean() {
        throw new IllegalScalarCastJsonException(getFlavor(), ScalarJsonValue.Flavor.BOOLEAN);
    }

    @Override
    public JsonString asString() {
        throw new IllegalScalarCastJsonException(getFlavor(), ScalarJsonValue.Flavor.STRING);
    }
}