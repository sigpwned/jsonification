package com.sigpwned.jsonification.value;

import java.util.Collection;
import java.util.Set;

import com.sigpwned.jsonification.JsonValue;

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
