package com.sigpwned.jsonification.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

import com.sigpwned.jsonification.Json;
import com.sigpwned.jsonification.JsonEvent;
import com.sigpwned.jsonification.JsonEventParser;
import com.sigpwned.jsonification.JsonFactory;
import com.sigpwned.jsonification.JsonParser;
import com.sigpwned.jsonification.exception.ParseJsonException;

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
public class DefaultJsonEventParser implements AutoCloseable, JsonEventParser {
    private final JsonParser parser;
    private JsonEvent peek;
    private JsonFactory factory;
    private String nextName;
    
    /* default */ DefaultJsonEventParser(String text) throws IOException {
        this(Json.getDefaultFactory().newParser(new StringReader(text)));
    }
    
    public DefaultJsonEventParser(JsonParser parser) {
        this.parser = parser;
        this.factory = Json.getDefaultFactory();
    }
    
    @Override
    public JsonFactory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(JsonFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public void nextName(String nextName) {
        this.nextName = nextName;
    }
    
    @Override
    public JsonEvent openObject() throws IOException {
        return openObject(null);
    }
    
    @Override
    public JsonEvent openObject(String name) throws IOException {
        JsonEvent e=next();
        
        if(e.getType() != JsonEvent.Type.OPEN_OBJECT)
            throw parseException(JsonEvent.Type.OPEN_OBJECT, e.getType());

        name = name(name);
        if(!Objects.equals(name, e.getName()))
            throw parseException(name, e.getName());
        
        return e;
    }
    
    @Override
    public JsonEvent closeObject() throws IOException {
        JsonEvent e=next();
        if(e.getType() != JsonEvent.Type.CLOSE_OBJECT)
            throw parseException(JsonEvent.Type.CLOSE_OBJECT, e.getType());
        return e;
    }
    
    @Override
    public JsonEvent openArray() throws IOException {
        return openArray(null);
    }
    
    @Override
    public JsonEvent openArray(String name) throws IOException {
        JsonEvent e=next();
        
        if(e.getType() != JsonEvent.Type.OPEN_ARRAY)
            throw parseException(JsonEvent.Type.OPEN_ARRAY, e.getType());

        name = name(name);
        if(!Objects.equals(name, e.getName()))
            throw parseException(name, e.getName());
        
        return e;
    }
    
    @Override
    public JsonEvent closeArray() throws IOException {
        JsonEvent e=next();
        if(e.getType() != JsonEvent.Type.CLOSE_ARRAY)
            throw parseException(JsonEvent.Type.CLOSE_ARRAY, e.getType());
        return e;
    }
    
    @Override
    public JsonEvent scalar() throws IOException {
        return scalar(null);
    }
    
    @Override
    public JsonEvent scalar(String name) throws IOException {
        JsonEvent e=next();
        
        if(e.getType() != JsonEvent.Type.SCALAR)
            throw parseException(JsonEvent.Type.SCALAR, e.getType());

        name = name(name);
        if(!Objects.equals(name, e.getName()))
            throw parseException(name, e.getName());
        
        return e;
    }
    
    @Override
    public JsonEvent nil() throws IOException {
        return nil(null);
    }
    
    @Override
    public JsonEvent nil(String name) throws IOException {
        JsonEvent e=next();
        
        if(e.getType() != JsonEvent.Type.NULL)
            throw parseException(JsonEvent.Type.NULL, e.getType());
        
        name = name(name);
        if(!Objects.equals(name, e.getName()))
            throw parseException(name, e.getName());
        
        return e;
    }
    
    @Override
    public void eof() throws IOException {
        if(peek() != null)
            throw new ParseJsonException("Expected EOF, but received "+peek().getType());
    }
    
    private String name(String name) {
        String result;
        
        if(nextName!=null && name!=null) {
            if(nextName.equals(name))
                result = nextName;
            else
                throw new ParseJsonException("Disagreement between nextName "+nextName+" and name "+name);
        } else
        if(nextName != null)
            result = nextName;
        else
        if(name != null)
            result = name;
        else
            result = null;
        
        nextName = null;
        
        return result;
    }
    
    private static ParseJsonException parseException(String expected, String observed) {
        return new ParseJsonException("Expected field with name "+expected+", but received "+observed);
    }
    
    private static ParseJsonException parseException(JsonEvent.Type expected, JsonEvent.Type observed) {
        return new ParseJsonException("Expected "+expected+", but received "+observed);
    }
    
    @Override
    public JsonEvent peek() throws IOException {
        if(peek == null) {
            getParser().next(new JsonParser.Handler() {
                @Override
                public void scalar(String name, String value) {
                    peek = new JsonEvent(JsonEvent.Type.SCALAR, name, getFactory().newValue(value));
                }
                
                @Override
                public void scalar(String name, boolean value) {
                    peek = new JsonEvent(JsonEvent.Type.SCALAR, name, getFactory().newValue(value));
                }
                
                @Override
                public void scalar(String name, double value) {
                    peek = new JsonEvent(JsonEvent.Type.SCALAR, name, getFactory().newValue(value));
                }
                
                @Override
                public void scalar(String name, long value) {
                    peek = new JsonEvent(JsonEvent.Type.SCALAR, name, getFactory().newValue(value));
                }
                
                @Override
                public void openObject(String name) {
                    peek = new JsonEvent(JsonEvent.Type.OPEN_OBJECT, name, null);
                }
                
                @Override
                public void openArray(String name) {
                    peek = new JsonEvent(JsonEvent.Type.OPEN_ARRAY, name, null);
                }
                
                @Override
                public void nil(String name) {
                    peek = new JsonEvent(JsonEvent.Type.SCALAR, name, getFactory().newNull());
                }
                
                @Override
                public void closeObject() {
                    peek = new JsonEvent(JsonEvent.Type.CLOSE_OBJECT, null, null);
                }
                
                @Override
                public void closeArray() {
                    peek = new JsonEvent(JsonEvent.Type.CLOSE_ARRAY, null, null);
                }
            });
        }
        return peek;
    }
    
    @Override
    public JsonEvent next() throws IOException {
        JsonEvent result=peek();
        peek = null;
        return result;
    }

    private JsonParser getParser() {
        return parser;
    }

    @Override
    public void close() throws IOException {
        getParser().close();
    }
}
