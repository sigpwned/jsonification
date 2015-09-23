package com.sigpwned.jsonification.impl;

import com.sigpwned.jsonification.Json;
import com.sigpwned.jsonification.value.ScalarJsonValue;
import com.sigpwned.jsonification.value.scalar.JsonNumber;

/**
 * Copyright 2015 Andy Boothe
 *     
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class DefaultJsonNumber extends AbstractScalarJsonValue implements JsonNumber {
    public static JsonNumber valueOf(Number value) {
        return value!=null ? new DefaultJsonNumber(value) : Json.NULL;
    }
    
    public static JsonNumber valueOf(long value) {
        return new DefaultJsonNumber(value);
    }
    
    public static JsonNumber valueOf(double value) {
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
