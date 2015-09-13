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
    
    public boolean has(String name);
    
    public Set<String> keys();
    
    public Collection<JsonValue> values();
    
    public Iterable<JsonObject.Entry> entries();
}
