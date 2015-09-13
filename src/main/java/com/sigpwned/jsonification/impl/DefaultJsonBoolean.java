package com.sigpwned.jsonification.impl;

import com.sigpwned.jsonification.value.ScalarJsonValue;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;

public final class DefaultJsonBoolean extends AbstractScalarJsonValue implements JsonBoolean {
    public DefaultJsonBoolean(boolean value) {
        super(value);
    }
    
    @Override
    public JsonBoolean asBoolean() {
        return this;
    }

    @Override
    public ScalarJsonValue.Flavor getFlavor() {
        return ScalarJsonValue.Flavor.BOOLEAN;
    }

    @Override
    public Boolean getBooleanValue() {
        return (Boolean) getValue();
    }

    @Override
    public boolean booleanVal() {
        return getBooleanValue().booleanValue();
    }
}
