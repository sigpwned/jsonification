package com.sigpwned.jsonification.parser;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

import com.sigpwned.jsonification.Json;
import com.sigpwned.jsonification.JsonEvent;
import com.sigpwned.jsonification.impl.DefaultJsonBoolean;
import com.sigpwned.jsonification.impl.DefaultJsonNumber;
import com.sigpwned.jsonification.impl.DefaultJsonString;

public class JsonEventParser implements AutoCloseable {
    private final JsonParser parser;
    
    public JsonEventParser(String text) {
        this(new JsonParser(text));
    }
    
    public JsonEventParser(Reader reader) {
        this(new JsonParser(reader));
    }
    
    public JsonEventParser(PushbackReader reader) {
        this(new JsonParser(reader));
    }
    
    public JsonEventParser(JsonParser parser) {
        this.parser = parser;
    }
    
    public JsonEvent next() throws IOException {
        final JsonEvent[] result=new JsonEvent[1];
        getParser().next(new JsonParser.Handler() {
            @Override
            public void scalar(String name, String value) {
                result[0] = new JsonEvent(JsonEvent.Type.VALUE, name, DefaultJsonString.valueOf(value));
            }
            
            @Override
            public void scalar(String name, boolean value) {
                result[0] = new JsonEvent(JsonEvent.Type.VALUE, name, DefaultJsonBoolean.valueOf(value));
            }
            
            @Override
            public void scalar(String name, double value) {
                result[0] = new JsonEvent(JsonEvent.Type.VALUE, name, DefaultJsonNumber.valueOf(value));
            }
            
            @Override
            public void scalar(String name, long value) {
                result[0] = new JsonEvent(JsonEvent.Type.VALUE, name, DefaultJsonNumber.valueOf(value));
            }
            
            @Override
            public void openObject(String name) {
                result[0] = new JsonEvent(JsonEvent.Type.OPEN_OBJECT, name, null);
            }
            
            @Override
            public void openArray(String name) {
                result[0] = new JsonEvent(JsonEvent.Type.OPEN_ARRAY, name, null);
            }
            
            @Override
            public void nil(String name) {
                result[0] = new JsonEvent(JsonEvent.Type.VALUE, name, Json.NULL);
            }
            
            @Override
            public void eof() {
                result[0] = new JsonEvent(JsonEvent.Type.EOF, null, null);
            }
            
            @Override
            public void closeObject() {
                result[0] = new JsonEvent(JsonEvent.Type.CLOSE_OBJECT, null, null);
            }
            
            @Override
            public void closeArray() {
                result[0] = new JsonEvent(JsonEvent.Type.CLOSE_ARRAY, null, null);
            }
        });
        return result[0];
    }

    private JsonParser getParser() {
        return parser;
    }

    public void close() throws IOException {
        getParser().close();
    }
}
