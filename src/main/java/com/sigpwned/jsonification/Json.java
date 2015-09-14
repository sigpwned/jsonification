package com.sigpwned.jsonification;

import java.io.IOException;
import java.io.Reader;

import com.sigpwned.jsonification.parser.JsonTreeParser;
import com.sigpwned.jsonification.value.JsonNull;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;

public class Json {
    public static JsonNull NULL=JsonNull.NULL;
    
    public static JsonBoolean TRUE=JsonBoolean.TRUE;
    
    public static JsonBoolean FALSE=JsonBoolean.FALSE;
    
    public static JsonValue parse(Reader reader) throws IOException {
        JsonValue result;
        try (JsonTreeParser p=new JsonTreeParser(reader)) {
            result = p.next();
        }
        return result;
    }
    
    public static JsonValue parse(String text) throws IOException {
        JsonValue result;
        try (JsonTreeParser p=new JsonTreeParser(text)) {
            result = p.next();
        }
        return result;
    }
}
