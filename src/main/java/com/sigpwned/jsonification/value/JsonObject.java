package com.sigpwned.jsonification.value;

import java.util.Collection;
import java.util.Set;

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
public interface JsonObject extends JsonValue {
    public static interface Entry {
        public String getName();
        
        public JsonValue getValue();
    }
    
    public JsonValue get(String name);
    
    public void set(String name, JsonValue value);
    
    public void set(String name, boolean value);
    
    public void set(String name, long value);
    
    public void set(String name, double value);
    
    public void set(String name, String value);
    
    public boolean has(String name);
    
    public JsonValue remove(String name);
    
    public Set<String> keys();
    
    public Collection<JsonValue> values();
    
    public Iterable<JsonObject.Entry> entries();
    
    public int size();
}
