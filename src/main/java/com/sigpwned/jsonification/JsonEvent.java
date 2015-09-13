package com.sigpwned.jsonification;

import com.sigpwned.jsonification.value.ScalarJsonValue;


public class JsonEvent {
    public static enum Type {
        OPEN_OBJECT, CLOSE_OBJECT,
        OPEN_ARRAY, CLOSE_ARRAY,
        VALUE, EOF;
    }
    
    private final JsonEvent.Type type;
    private final String name;
    private final ScalarJsonValue value;

    public JsonEvent(JsonEvent.Type type, String name, ScalarJsonValue value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }
    
    public JsonEvent.Type getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }

    public ScalarJsonValue getValue() {
        return value;
    }
}
