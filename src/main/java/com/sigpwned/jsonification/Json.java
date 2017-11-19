package com.sigpwned.jsonification;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

import com.sigpwned.jsonification.impl.DefaultJsonFactory;
import com.sigpwned.jsonification.io.IgnoreCloseReader;
import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonNull;
import com.sigpwned.jsonification.value.JsonObject;
import com.sigpwned.jsonification.value.ScalarJsonValue;
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
     * 
     * @param reader The {@link Reader} from which to read JSON
     * 
     * @return the {@code JsonValue} that was read
     * 
     * @throws IOException if an underlying {@code IOException} occurs while
     *         reading JSON
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
     * 
     * @param text The {@link String} from which to read JSON
     * 
     * @return the {@code JsonValue} that was read
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
     * 
     * @param reader The {@link Reader} from which to read JSON
     * 
     * @param handler The {@link JsonParser.Handler} to receive parse events
     * 
     * @return {@code true} if a value was read, or {@code false} otherwise.
     * 
     * @see JsonParser#parse(com.sigpwned.jsonification.JsonParser.Handler)
     * 
     * @throws IOException if an underlying {@code IOException} occurs while
     *         reading JSON
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
     * 
     * @param text The {@link String} from which to read JSON
     * @param handler The {@link JsonParser.Handler} to receive parse events
     * 
     * @return {@code true} if a value was read, or {@code false} otherwise.
     * 
     * @see JsonParser#parse(com.sigpwned.jsonification.JsonParser.Handler)
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
     * 
     * @param tree the {@code JsonValue} to emit
     * 
     * @return the {@code JsonValue} converted to a valid JSON string
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
    
    /**
     * @return the current default {@link JsonFactory} used by constructor
     *         methods. This method is thread-safe.
     * 
     * @see #newObject()
     * @see #newArray()
     * @see #newValue(boolean)
     * @see #newValue(double)
     * @see #newValue(long)
     * @see #newValue(String)
     */
    public static JsonFactory getDefaultFactory() {
        return defaultFactory.get();
    }
    
    /**
     * Sets the current default {@link JsonFactory} used by constructor
     * methods. This method is thread-safe.
     * 
     * @param defaultFactory the new {@code JsonFactory}
     * 
     * @return the old default {@code JsonFactory}
     * 
     * @see #newObject()
     * @see #newArray()
     * @see #newValue(boolean)
     * @see #newValue(double)
     * @see #newValue(long)
     * @see #newValue(String)
     */
    public static JsonFactory setDefaultFactory(JsonFactory defaultFactory) {
        if(defaultFactory == null)
            throw new NullPointerException();
        return Json.defaultFactory.getAndSet(defaultFactory);
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

    public static JsonParser newValueParser(JsonValue value) throws IOException {
        return getDefaultFactory().newValueParser(value);
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
    
    /**
     * Generic hash code method for json values. Should be used by all
     * implementations for cross-compatibility.
     * 
     * See https://stackoverflow.com/questions/113511/best-implementation-for-hashcode-method
     * 
     * @param v The value for which to determine the hash code
     * @return The hash code for the given value
     */
    public static int hashCode(JsonValue v) {
        if(v == null)
            throw new NullPointerException();
            
        int result;
        
        switch(v.getType()) {
        case ARRAY:
        {
            JsonArray av=v.asArray();
            result = 0;
            for(JsonValue element : av)
                result = 37*result + element.hashCode();
        } break;
        case NULL:
            result = 0;
            break;
        case OBJECT:
        {
            JsonObject ov=v.asObject();
            result = 0;
            SortedSet<String> keys=new TreeSet<String>(ov.keys());
            for(String key : keys) {
                int ecode=key.hashCode()+ov.get(key).hashCode();
                result = 37*result + ecode;
            }
        } break;
        case SCALAR:
        {
            ScalarJsonValue sv=v.asScalar();
            switch(sv.getFlavor()) {
            case BOOLEAN:
                result = sv.asBoolean().booleanVal() ? 1 : 0;
                break;
            case NULL:
                result = 0;
                break;
            case NUMBER:
                result = sv.asNumber().getNumberValue().hashCode();
                break;
            case STRING:
                result = sv.asString().getValue().hashCode();
                break;
            default:
                throw new RuntimeException("unrecognized scalar value: "+sv);
            }
        } break;
        default:
            throw new RuntimeException("unrecognized value: "+v);
        }
        
        return result;
    }
    
    /**
     * Generic equals method for json values. Should be used by all
     * implementations for cross-compatibility.
     * 
     * @param a First value to compare for equality
     * @param b Second value to compare for equality
     * @return true if the objects are equal, or false otherwise
     */
    public static boolean equals(JsonValue a, JsonValue b) {
        boolean result;
        
        if(a == b)
            result = true;
        else
        if(a==null || b==null)
            result = false;
        else
        if(a.isNull() && b.isNull())
            result = true;
        else
        if(a.isNull() || b.isNull())
            result = false;
        else
        if(a.getType() == b.getType()) {
            JsonValue.Type type=a.getType();
            switch(type) {
            case ARRAY:
            {
                JsonArray aa=a.asArray(), ab=b.asArray();
                if(aa.size() == ab.size()) {
                    result = true;
                    int size=aa.size();
                    for(int i=0;i<size;i++) {
                        result = Objects.equals(aa.get(i), ab.get(i));
                        if(result == false)
                            break;
                    }
                }
                else
                    result = false;
            } break;
            case NULL:
                result = true;
                break;
            case OBJECT:
            {
                JsonObject oa=a.asObject(), ob=b.asObject();
                if(oa.size() == ob.size()) {
                    if(oa.keys().equals(ob.keys())) {
                        result = true;
                        Set<String> keys=oa.keys();
                        for(String key : keys) {
                            JsonValue ea=oa.get(key), eb=ob.get(key);
                            result = Objects.equals(ea, eb);
                            if(result == false)
                                break;
                        }
                    }
                    else
                        result = false;
                }
                else
                    result = false;
            } break;
            case SCALAR:
            {
                ScalarJsonValue sa=a.asScalar(), sb=b.asScalar();
                if(sa.getFlavor() == sb.getFlavor())
                    result = sa.getValue().equals(sb.getValue());
                else
                    result = false;
            } break;
            default:
                throw new RuntimeException("unrecognized type: "+type);
            }
        }
        else
            result = false;
        
        return result;
    }
}
