package com.sigpwned.jsonification.exception;

import com.sigpwned.jsonification.JsonException;
import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.value.ScalarJsonValue;

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
public class ClassCastJsonException extends JsonException {
    private static final long serialVersionUID = -3854318051038016222L;
    
    public static enum Type {
        VALUE, SCALAR;
    }
    
    private final ClassCastJsonException.Type type;
    
    private final JsonValue value;
    private final JsonValue.Type valueCastedType;
    private final ScalarJsonValue.Flavor scalarCastedFlavor;
    
    public ClassCastJsonException(JsonValue value, JsonValue.Type valueCastedType) {
        super("Attempted illegal cast from "+value.getType()+" to "+valueCastedType);
        this.type = ClassCastJsonException.Type.VALUE;
        this.value = value;
        this.valueCastedType = valueCastedType;
        this.scalarCastedFlavor = null;
    }

    public ClassCastJsonException(ScalarJsonValue value, ScalarJsonValue.Flavor scalarCastedFlavor) {
        super("Attempted illegal scalar cast from "+value.getFlavor()+" to "+scalarCastedFlavor);
        this.type = ClassCastJsonException.Type.SCALAR;
        this.value = value;
        this.valueCastedType = JsonValue.Type.OBJECT;
        this.scalarCastedFlavor = scalarCastedFlavor;
    }

    public ClassCastJsonException.Type getType() {
        return type;
    }
    
    public JsonValue getValue() {
        return value;
    }

    public JsonValue.Type getValueObjectType() {
        return getValue().getType();
    }

    public JsonValue.Type getValueCastedType() {
        return valueCastedType;
    }

    public ScalarJsonValue.Flavor getScalarObjectFlavor() {
        ScalarJsonValue.Flavor result;
        if(getType() == ClassCastJsonException.Type.SCALAR)
            result = getValue().asScalar().getFlavor();
        else
            result = null;
        return result;
    }

    public ScalarJsonValue.Flavor getScalarCastedFlavor() {
        return scalarCastedFlavor;
    }
}
