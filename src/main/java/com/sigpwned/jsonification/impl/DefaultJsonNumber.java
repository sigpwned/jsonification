package com.sigpwned.jsonification.impl;

import com.sigpwned.jsonification.value.ScalarJsonValue;
import com.sigpwned.jsonification.value.scalar.JsonNumber;

public final class DefaultJsonNumber extends AbstractScalarJsonValue implements JsonNumber {
    public static DefaultJsonNumber valueOf(Number value) {
        return new DefaultJsonNumber(value);
    }
    
    public static DefaultJsonNumber valueOf(long value) {
        return new DefaultJsonNumber(value);
    }
    
    public static DefaultJsonNumber valueOf(double value) {
        return new DefaultJsonNumber(value);
    }
    
    public DefaultJsonNumber(long value) {
        this(Long.valueOf(value));
    }
    
    public DefaultJsonNumber(double value) {
        this(Double.valueOf(value));
    }
    
    public DefaultJsonNumber(Number value) {
        super(value);
    }
    
    @Override
    public JsonNumber asNumber() {
        return this;
    }

    @Override
    public ScalarJsonValue.Flavor getFlavor() {
        return ScalarJsonValue.Flavor.NUMBER;
    }

    @Override
    public Number getNumberValue() {
        return (Number) getValue();
    }

    @Override
    public long longVal() {
        return getNumberValue().longValue();
    }

    @Override
    public double doubleVal() {
        return getNumberValue().doubleValue();
    }
}
