package com.sigpwned.jsonification.value;

import com.sigpwned.jsonification.JsonValue;
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
public interface ScalarJsonValue extends JsonValue {
    public static enum Flavor {
        NUMBER, BOOLEAN, STRING, NULL;
    }
    
    public ScalarJsonValue.Flavor getFlavor();
    
    public Object getValue();
    
    public JsonNumber asNumber();
    
    public JsonBoolean asBoolean();
    
    public JsonString asString();

}
