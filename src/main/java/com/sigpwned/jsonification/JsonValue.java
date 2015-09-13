package com.sigpwned.jsonification;

import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonObject;
import com.sigpwned.jsonification.value.ScalarJsonValue;


public interface JsonValue {
    public static enum Type {
        OBJECT, ARRAY, SCALAR, NULL;
    }
    
    public boolean isNull();
    
    public JsonValue.Type getType();
    
    public JsonObject asObject();
    
    public JsonArray asArray();
    
    public ScalarJsonValue asScalar();
}
