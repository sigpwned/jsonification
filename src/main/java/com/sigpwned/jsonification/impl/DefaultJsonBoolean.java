package com.sigpwned.jsonification.impl;

import com.sigpwned.jsonification.value.ScalarJsonValue;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;

public final class DefaultJsonBoolean extends AbstractScalarJsonValue implements JsonBoolean {
    public static DefaultJsonBoolean TRUE=new DefaultJsonBoolean(true);

    public static DefaultJsonBoolean FALSE=new DefaultJsonBoolean(false);
    
    public static DefaultJsonBoolean valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }
    
    private DefaultJsonBoolean(boolean value) {
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
