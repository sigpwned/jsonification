package com.sigpwned.jsonification;

import com.sigpwned.jsonification.value.JsonObject;

public class JsonWalker {
    public static interface Handler {
        public void openObject(String name);
        
        public void closeObject();
        
        public void openArray(String name);
        
        public void closeArray();
        
        public void nil(String name);
        
        public void scalar(String name, long value);
        
        public void scalar(String name, double value);
        
        public void scalar(String name, boolean value);
        
        public void scalar(String name, String value);
    }

    private final JsonValue value;

    public JsonWalker(JsonValue value) {
        this.value = value;
    }

    public JsonValue getValue() {
        return value;
    }
    
    public void walk(JsonWalker.Handler handler) {
        walk(null, null, value, handler);
    }
    
    private void walk(JsonValue parent, String name, JsonValue value, final JsonWalker.Handler handler) {
        switch(value.getType()) {
        case ARRAY:
        {
            handler.openArray(name);
            for(JsonValue element : value.asArray())
                walk(value, null, element, handler);
            handler.closeArray();
        } break;
        case NULL:
            handler.nil(name);        
            break;
        case OBJECT:
        {
            handler.openObject(name);
            for(JsonObject.Entry entry : value.asObject().entries())
                walk(value, entry.getName(), entry.getValue(), handler);
            handler.closeObject();
        } break;
        case SCALAR:
        {
            switch(value.asScalar().getFlavor()) {
            case BOOLEAN:
                handler.scalar(name, value.asScalar().asBoolean().booleanVal());
                break;
            case NULL:
                // This shouldn't happen, but meh.
                handler.nil(name);
                break;
            case NUMBER:
            {
                Number n=value.asScalar().asNumber().getNumberValue();
                if(n instanceof Long)
                    handler.scalar(name, n.longValue());
                else
                if(n instanceof Double)
                    handler.scalar(name, n.doubleValue());
                else
                if(Math.floor(n.doubleValue()) == n.doubleValue())
                    handler.scalar(name, n.longValue());
                else
                    handler.scalar(name, n.doubleValue());
            } break;
            case STRING:
                handler.scalar(name, value.asScalar().asString().stringVal());
                break;
            default:
                break;
            }
        } break;
        default:
            throw new JsonError("unrecognized value type: "+value.getType());
        }
    }
}
