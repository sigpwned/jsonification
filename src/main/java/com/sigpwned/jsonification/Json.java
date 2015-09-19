package com.sigpwned.jsonification;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

import com.sigpwned.jsonification.exception.InternalErrorJsonException;
import com.sigpwned.jsonification.generator.JsonTreeGenerator;
import com.sigpwned.jsonification.impl.DefaultJsonValueFactory;
import com.sigpwned.jsonification.io.IgnoreCloseReader;
import com.sigpwned.jsonification.parser.JsonParser;
import com.sigpwned.jsonification.parser.JsonTreeParser;
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
        try (JsonTreeParser p=new JsonTreeParser(new IgnoreCloseReader(reader))) {
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
            try (JsonTreeParser p=new JsonTreeParser(text)) {
                result = p.next();
            }
        }
        catch(IOException e) {
            // This should never happen since we're working with Strings
            throw new InternalErrorJsonException("Impossible IOException", e);
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
        try (JsonParser p=new JsonParser(new IgnoreCloseReader(reader))) {
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
            try (JsonParser p=new JsonParser(text)) {
                result = p.parse(handler);
            }
        }
        catch(IOException e) {
            throw new InternalErrorJsonException("Impossible IOException", e);
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
                try (JsonTreeGenerator g=new JsonTreeGenerator(result)) {
                    g.emit(tree);
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
    
    private static JsonValueFactory factory=new DefaultJsonValueFactory();
    
    public static JsonValueFactory getDefaultValueFactory() {
        return factory;
    }
    
    public static void setDefaultValueFactory(JsonValueFactory factory) {
        Json.factory = factory;
    }
    
    public static JsonObject newObject() {
        return factory.newObject();
    }

    public static JsonArray newArray() {
        return factory.newArray();
    }

    public static JsonBoolean newValue(boolean value) {
        return factory.newValue(value);
    }

    public static JsonNumber newValue(long value) {
        return factory.newValue(value);
    }

    public static JsonNumber newValue(double value) {
        return factory.newValue(value);
    }

    public static JsonString newValue(String value) {
        return factory.newValue(value);
    }

    public static JsonNull newNull() {
        return factory.newNull();
    }
}
