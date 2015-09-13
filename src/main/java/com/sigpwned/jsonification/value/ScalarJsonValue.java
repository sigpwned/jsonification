package com.sigpwned.jsonification.value;

import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;
import com.sigpwned.jsonification.value.scalar.JsonNumber;
import com.sigpwned.jsonification.value.scalar.JsonString;

public interface ScalarJsonValue extends JsonValue {
    public static enum Flavor {
        NUMBER, BOOLEAN, STRING, NULL;
    }
    
    public ScalarJsonValue.Flavor getFlavor();
    
    public Object getValue();
    
    public JsonNumber asNumber();
    
    public JsonBoolean asBoolean();
    
    public JsonString asString();

}
