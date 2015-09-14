package com.sigpwned.jsonification.parser;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

import com.sigpwned.jsonification.Json;
import com.sigpwned.jsonification.JsonEvent;
import com.sigpwned.jsonification.JsonValueFactory;

/**
 * Copyright 2015 Andy Boothe
 *     
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class JsonEventParser implements AutoCloseable {
    private final JsonParser parser;
    private JsonValueFactory factory;
    
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
        this.factory = Json.getDefaultValueFactory();
    }
    
    public JsonValueFactory getFactory() {
        return factory;
    }

    public void setFactory(JsonValueFactory factory) {
        this.factory = factory;
    }

    public JsonEvent next() throws IOException {
        final JsonEvent[] result=new JsonEvent[1];
        getParser().next(new JsonParser.Handler() {
            @Override
            public void scalar(String name, String value) {
                result[0] = new JsonEvent(JsonEvent.Type.VALUE, name, getFactory().newValue(value));
            }
            
            @Override
            public void scalar(String name, boolean value) {
                result[0] = new JsonEvent(JsonEvent.Type.VALUE, name, getFactory().newValue(value));
            }
            
            @Override
            public void scalar(String name, double value) {
                result[0] = new JsonEvent(JsonEvent.Type.VALUE, name, getFactory().newValue(value));
            }
            
            @Override
            public void scalar(String name, long value) {
                result[0] = new JsonEvent(JsonEvent.Type.VALUE, name, getFactory().newValue(value));
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
                result[0] = new JsonEvent(JsonEvent.Type.VALUE, name, getFactory().newNull());
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
