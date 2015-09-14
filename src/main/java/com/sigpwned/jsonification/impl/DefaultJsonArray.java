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
    public JsonValue set(int index, boolean value) {
        return set(index, DefaultJsonBoolean.valueOf(value));
    }

    @Override
    public JsonValue set(int index, long value) {
        return set(index, DefaultJsonNumber.valueOf(value));
    }

    @Override
    public JsonValue set(int index, double value) {
        return set(index, DefaultJsonNumber.valueOf(value));
    }

    @Override
    public JsonValue set(int index, String value) {
        return set(index, DefaultJsonString.valueOf(value));
    }
    
    @Override
    public void add(JsonValue value) {
        values.add(value);
    }

    @Override
    public void add(boolean value) {
        add(DefaultJsonBoolean.valueOf(value));
    }

    @Override
    public void add(long value) {
        add(DefaultJsonNumber.valueOf(value));
    }

    @Override
    public void add(double value) {
        add(DefaultJsonNumber.valueOf(value));
    }

    @Override
    public void add(String value) {
        add(DefaultJsonString.valueOf(value));
    }

    @Override
    public void add(int index, JsonValue value) {
        values.add(index, value);
    }

    @Override
    public void add(int index, boolean value) {
        add(index, DefaultJsonBoolean.valueOf(value));
    }

    @Override
    public void add(int index, long value) {
        add(index, DefaultJsonNumber.valueOf(value));
    }

    @Override
    public void add(int index, double value) {
        add(index, DefaultJsonNumber.valueOf(value));
    }

    @Override
    public void add(int index, String value) {
        add(index, DefaultJsonString.valueOf(value));
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
