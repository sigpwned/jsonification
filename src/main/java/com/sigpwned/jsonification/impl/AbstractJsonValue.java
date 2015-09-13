package com.sigpwned.jsonification.impl;

import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.exception.IllegalCastJsonException;
import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonObject;
import com.sigpwned.jsonification.value.ScalarJsonValue;

/* default */ abstract class AbstractJsonValue implements JsonValue {
    public AbstractJsonValue() {
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public JsonObject asObject() {
        throw new IllegalCastJsonException(getType(), JsonValue.Type.OBJECT);
    }

    @Override
    public JsonArray asArray() {
        throw new IllegalCastJsonException(getType(), JsonValue.Type.ARRAY);
    }

    @Override
    public ScalarJsonValue asScalar() {
        throw new IllegalCastJsonException(getType(), JsonValue.Type.SCALAR);
    }
}