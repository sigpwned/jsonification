package com.sigpwned.jsonification.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.sigpwned.jsonification.Json;
import com.sigpwned.jsonification.JsonError;
import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.value.JsonObject;

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
public final class DefaultJsonObject extends AbstractJsonValue implements JsonObject {
    public static enum KeyOrder {
        UNORDERED, INSERTION, ALPHABETICAL;
    }
    
    private static Map<String,JsonValue> map(DefaultJsonObject.KeyOrder keyOrder) {
        Map<String,JsonValue> result;
        
        switch(keyOrder) {
        case ALPHABETICAL:
            result = new TreeMap<>();
            break;
        case INSERTION:
            result = new LinkedHashMap<>();
            break;
        case UNORDERED:
            result = new HashMap<>();
            break;
        default:
            throw new JsonError("unrecognized key order: "+keyOrder);
        }
        
        return result;
    }
    
    private final Map<String,JsonValue> values;
    
    public DefaultJsonObject() {
        this(DefaultJsonObject.KeyOrder.UNORDERED);
    }

    public DefaultJsonObject(DefaultJsonObject.KeyOrder keyOrder) {
        this(map(keyOrder));
    }

    private DefaultJsonObject(Map<String,JsonValue> values) {
        this.values = values;
    }
    
    @Override
    public DefaultJsonObject asObject() {
        return this;
    }

    @Override
    public JsonValue.Type getType() {
        return JsonValue.Type.OBJECT;
    }

    @Override
    public JsonValue get(String name) {
        return values.get(name);
    }

    @Override
    public DefaultJsonObject set(String name, JsonValue value) {
        values.put(name, value!=null ? value : Json.NULL);
        return this;
    }
    
    @Override
    public DefaultJsonObject set(String name, boolean value) {
        return set(name, DefaultJsonBoolean.valueOf(value));
    }

    @Override
    public DefaultJsonObject set(String name, long value) {
        return set(name, DefaultJsonNumber.valueOf(value));
    }

    @Override
    public DefaultJsonObject set(String name, double value) {
        return set(name, DefaultJsonNumber.valueOf(value));
    }

    @Override
    public DefaultJsonObject set(String name, String value) {
        return set(name, DefaultJsonString.valueOf(value));
    }

    @Override
    public boolean has(String name) {
        return values.containsKey(name);
    }
    
    @Override
    public JsonValue remove(String name) {
        return values.remove(name);
    }

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(values.keySet());
    }

    @Override
    public Collection<JsonValue> values() {
        return Collections.unmodifiableCollection(values.values());
    }

    @Override
    public Iterable<JsonObject.Entry> entries() {
        return new Iterable<JsonObject.Entry>() {
            public Iterator<JsonObject.Entry> iterator() {
                final Iterator<Map.Entry<String,JsonValue>> iterator=values.entrySet().iterator();
                return new Iterator<JsonObject.Entry>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public JsonObject.Entry next() {
                        final Map.Entry<String,JsonValue> e=iterator.next();
                        return new JsonObject.Entry() {
                            @Override
                            public String getName() {
                                return e.getKey();
                            }

                            @Override
                            public JsonValue getValue() {
                                return e.getValue();
                            }
                        };
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    @Override
    public int size() {
        return values.size();
    }
}
