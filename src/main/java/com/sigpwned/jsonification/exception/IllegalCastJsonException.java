package com.sigpwned.jsonification.exception;

import com.sigpwned.jsonification.JsonException;
import com.sigpwned.jsonification.JsonValue;

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
