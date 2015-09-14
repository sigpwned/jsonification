package com.sigpwned.jsonification;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

import com.sigpwned.jsonification.generator.JsonTreeGenerator;
import com.sigpwned.jsonification.io.IgnoreCloseReader;
import com.sigpwned.jsonification.parser.JsonTreeParser;
import com.sigpwned.jsonification.value.JsonNull;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;

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
     * Reads one {@link JsonValue} from the given {@code Reader} and returns it.
     * The given {@code Reader} is left open.
     */
    public static JsonValue parse(Reader reader) throws IOException {
        JsonValue result;
        try (JsonTreeParser p=new JsonTreeParser(new IgnoreCloseReader(reader))) {
            result = p.next();
        }
        return result;
    }
    
    /**
     * Reads one {@link JsonValue} from the given {@code String} and returns it.
     */
    public static JsonValue parse(String text) throws IOException {
        JsonValue result;
        try (JsonTreeParser p=new JsonTreeParser(text)) {
            result = p.next();
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
}
