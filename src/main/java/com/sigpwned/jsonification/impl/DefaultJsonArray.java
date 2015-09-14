package com.sigpwned.jsonification.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.value.JsonArray;

public class DefaultJsonArray extends AbstractJsonValue implements JsonArray {
    private final List<JsonValue> values;
    
    public DefaultJsonArray() {
        this(new ArrayList<JsonValue>());
    }
    
    public DefaultJsonArray(List<JsonValue> values) {
        this.values = values;
    }
    
    @Override
    public JsonArray asArray() {
        return this;
    }

    @Override
    public JsonValue.Type getType() {
        return JsonValue.Type.ARRAY;
    }

    @Override
    public Iterator<JsonValue> iterator() {
        return Collections.unmodifiableList(values).iterator();
    }

    @Override
    public JsonValue get(int index) {
        return values.get(index);
    }

    @Override
    public JsonValue set(int index, JsonValue value) {
        return values.set(index, value);
    }

    @Override
    public void add(JsonValue value) {
        values.add(value);
    }

    @Override
    public void add(int index, JsonValue value) {
        values.add(index, value);
    }

    @Override
    public JsonValue remove(int index) {
        return values.remove(index);
    }

    @Override
    public int size() {
        return values.size();
    }

}
