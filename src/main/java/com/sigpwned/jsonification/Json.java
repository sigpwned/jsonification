package com.sigpwned.jsonification;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicReference;

import com.sigpwned.jsonification.impl.DefaultJsonFactory;
import com.sigpwned.jsonification.io.IgnoreCloseReader;
import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonNull;
import com.sigpwned.jsonification.value.JsonObject;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;
import com.sigpwned.jsonification.value.scalar.JsonNumber;
import com.sigpwned.jsonification.value.scalar.JsonString;

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
public class Json {
    public static JsonNull NULL=JsonNull.NULL;
    
    public static JsonBoolean TRUE=JsonBoolean.TRUE;
    
    public static JsonBoolean FALSE=JsonBoolean.FALSE;
    
    /**
     * Reads one {@link JsonValue} from the given {@code Reader} and returns
     * it. If more than one JSON value is contained in the given text, then
     * only the first is parsed. The given {@code Reader} is left open.
     */
    public static JsonValue parse(Reader reader) throws IOException {
        JsonValue result;
        try (JsonTreeParser p=getDefaultFactory().newTreeParser(reader)) {
            result = p.next();
        }
        return result;
    }
    
    /**
     * Reads one {@link JsonValue} from the given {@code String} and returns
     * it. If more than one JSON value is contained in the given text, then
     * only the first is parsed.
     */
    public static JsonValue parse(String text) {
        JsonValue result;
        try {
            try (StringReader r=new StringReader(text)) {
                try (JsonTreeParser p=getDefaultFactory().newTreeParser(r)) {
                    result = p.next();
                }
            }
        }
        catch(IOException e) {
            // This should never happen since we're working with Strings
            throw new JsonError("Impossible IOException", e);
        }
        return result;
    }
    
    /**
     * Parses JSON from the given {@code String}. If more than one JSON value
     * is contained in the given text, only the first is parsed. The given
     * {@code Reader} is left open.
     */
    public static boolean parse(Reader reader, JsonParser.Handler handler) throws IOException {
        boolean result;
        try (JsonParser p=getDefaultFactory().newParser(new IgnoreCloseReader(reader))) {
            result = p.parse(handler);
        }
        return result;
    }
    
    /**
     * Parses JSON from the given {@code String}. If more than one JSON value
     * is contained in the given text, only the first is parsed. 
     */
    public static boolean parse(String text, JsonParser.Handler handler) {
        boolean result;
        try {
            try (StringReader r=new StringReader(text)) {
                try (JsonParser p=getDefaultFactory().newParser(r)) {
                    result = p.parse(handler);
                }
            }
        }
        catch(IOException e) {
            throw new JsonError("Impossible IOException", e);
        }
        return result;
    }
    
    /**
     * Converts the given {@link JsonValue} to a {@code String} in JSON format.
     */
    public static String emit(JsonValue tree) {
        StringWriter result=new StringWriter();
        try {
            try {
                try (JsonGenerator g=getDefaultFactory().newGenerator(result)) {
                    g.value(tree);
                }
            }
            finally {
                result.close();
            }
        }
        catch(IOException e) {
            // This should never happen since we're using a StringWriter
            throw new JsonError(e);
        }
        return result.toString();
    }
    
    private static AtomicReference<JsonFactory> defaultFactory=new AtomicReference<JsonFactory>(new DefaultJsonFactory());
    
    public static JsonFactory getDefaultFactory() {
        return defaultFactory.get();
    }
    
    public static void setDefaultFactory(JsonFactory defaultFactory) {
        if(defaultFactory == null)
            throw new NullPointerException();
        Json.defaultFactory.set(defaultFactory);
    }
    
    public static JsonObject newObject() {
        return getDefaultFactory().newObject();
    }

    public static JsonArray newArray() {
        return getDefaultFactory().newArray();
    }

    public static JsonBoolean newValue(boolean value) {
        return getDefaultFactory().newValue(value);
    }

    public static JsonNumber newValue(long value) {
        return getDefaultFactory().newValue(value);
    }

    public static JsonNumber newValue(double value) {
        return getDefaultFactory().newValue(value);
    }

    public static JsonString newValue(String value) {
        return getDefaultFactory().newValue(value);
    }

    public static JsonNull newNull() {
        return getDefaultFactory().newNull();
    }

    public static JsonParser newParser(Reader input) throws IOException {
        return getDefaultFactory().newParser(input);
    }

    public static JsonEventParser newEventParser(Reader input) throws IOException {
        return getDefaultFactory().newEventParser(input);
    }

    public static JsonEventParser newEventParser(JsonParser parser) throws IOException {
        return getDefaultFactory().newEventParser(parser);
    }

    public static JsonTreeParser newTreeParser(Reader input) throws IOException {
        return getDefaultFactory().newTreeParser(input);
    }

    public static JsonTreeParser newTreeParser(JsonParser parser) throws IOException {
        return getDefaultFactory().newTreeParser(parser);
    }

    public static JsonTreeParser newTreeParser(JsonEventParser events)
            throws IOException {
        return getDefaultFactory().newTreeParser(events);
    }

    public static JsonGenerator newGenerator(Writer output) throws IOException {
        return getDefaultFactory().newGenerator(output);
    }

    public static JsonTreeGenerator newTreeGenerator() throws IOException {
        return getDefaultFactory().newTreeGenerator();
    }
}
