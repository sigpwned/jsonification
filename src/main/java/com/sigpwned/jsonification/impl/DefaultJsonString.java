package com.sigpwned.jsonification.impl;

import com.sigpwned.jsonification.value.ScalarJsonValue;
import com.sigpwned.jsonification.value.scalar.JsonString;

public final class DefaultJsonString extends AbstractScalarJsonValue implements JsonString {
    public DefaultJsonString(String value) {
        super(value);
    }
    
    @Override
    public JsonString asString() {
        return this;
    }

    @Override
    public ScalarJsonValue.Flavor getFlavor() {
        return ScalarJsonValue.Flavor.STRING;
    }

    @Override
    public String getStringValue() {
        return (String) getValue();
    }

    @Override
    public String stringVal() {
        return getStringValue();
    }
}
