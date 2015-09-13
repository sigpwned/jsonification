package com.sigpwned.jsonification.value;

import com.sigpwned.jsonification.JsonValue;

public interface JsonArray extends JsonValue, Iterable<JsonValue> {
    public JsonValue get(int index);
    
    public JsonValue set(int index, JsonValue value);
    
    public void add(JsonValue value);
    
    public void add(int index, JsonValue value);
    
    public JsonValue remove(int index);
    
    public int size();
}
