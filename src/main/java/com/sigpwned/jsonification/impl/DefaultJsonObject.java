package com.sigpwned.jsonification.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.value.JsonObject;

public class DefaultJsonObject extends AbstractJsonValue implements JsonObject {
    private final Map<String,JsonValue> values;
    
    public DefaultJsonObject() {
        this(new LinkedHashMap<String,JsonValue>());
    }

    public DefaultJsonObject(Map<String,JsonValue> values) {
        this.values = values;
    }
    
    @Override
    public JsonObject asObject() {
        return this;
    }

    @Override
    public JsonValue.Type getType() {
        return JsonValue.Type.OBJECT;
    }

    @Override
    public JsonValue get(String name) {
        JsonValue result=values.get(name);
        if(result == null)
            throw new IllegalArgumentException("no such key: "+name);
        return result;
    }

    @Override
    public void set(String name, JsonValue value) {
        if(value == null)
            throw new NullPointerException();
        values.put(name, value);
    }
    
    @Override
    public void set(String name, boolean value) {
        set(name, DefaultJsonBoolean.valueOf(value));
    }

    @Override
    public void set(String name, long value) {
        set(name, DefaultJsonNumber.valueOf(value));
    }

    @Override
    public void set(String name, double value) {
        set(name, DefaultJsonNumber.valueOf(value));
    }

    @Override
    public void set(String name, String value) {
        set(name, DefaultJsonString.valueOf(value));
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
