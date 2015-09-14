package com.sigpwned.jsonification;

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
public class JsonEvent {
    public static enum Type {
        OPEN_OBJECT, CLOSE_OBJECT,
        OPEN_ARRAY, CLOSE_ARRAY,
        VALUE, EOF;
    }
    
    private final JsonEvent.Type type;
    private final String name;
    private final ScalarJsonValue value;

    public JsonEvent(JsonEvent.Type type, String name, ScalarJsonValue value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }
    
    public JsonEvent.Type getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }

    public ScalarJsonValue getValue() {
        return value;
    }
}
