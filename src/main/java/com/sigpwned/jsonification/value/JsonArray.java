package com.sigpwned.jsonification.value;

import com.sigpwned.jsonification.JsonValue;

public interface JsonArray extends JsonValue, Iterable<JsonValue> {
    public JsonValue get(int index);
    
    public JsonValue set(int index, JsonValue value);
    
    public JsonValue set(int index, boolean value);
    
    public JsonValue set(int index, long value);
    
    public JsonValue set(int index, double value);
    
    public JsonValue set(int index, String value);
    
    public void add(JsonValue value);
    
    public void add(boolean value);
    
    public void add(long value);
    
    public void add(double value);
    
    public void add(String value);
    
    public void add(int index, JsonValue value);
    
    public void add(int index, boolean value);
    
    public void add(int index, long value);
    
    public void add(int index, double value);
    
    public void add(int index, String value);
    
    public JsonValue remove(int index);
    
    public int size();
}
