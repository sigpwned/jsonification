package com.sigpwned.jsonification.impl;

import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.exception.IllegalScalarCastJsonException;
import com.sigpwned.jsonification.value.ScalarJsonValue;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;
import com.sigpwned.jsonification.value.scalar.JsonNumber;
import com.sigpwned.jsonification.value.scalar.JsonString;

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
    public ScalarJsonValue asScalar() {
        return this;
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