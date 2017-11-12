package com.sigpwned.jsonification;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonNull;
import com.sigpwned.jsonification.value.JsonObject;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;
import com.sigpwned.jsonification.value.scalar.JsonNumber;
import com.sigpwned.jsonification.value.scalar.JsonString;

public interface JsonFactory {
    public JsonObject newObject();

    public JsonArray newArray();

    public JsonBoolean newValue(boolean value);

    public JsonNumber newValue(long value);

    public JsonNumber newValue(double value);

    public JsonString newValue(String value);
    
    public JsonNull newNull();
    
    public JsonParser newParser(Reader input) throws IOException;
    
    public JsonParser newValueParser(JsonValue value) throws IOException;
    
    public JsonEventParser newEventParser(Reader input) throws IOException;
    
    public JsonEventParser newEventParser(JsonParser parser) throws IOException;
    
    public JsonTreeParser newTreeParser(Reader input) throws IOException;
    
    public JsonTreeParser newTreeParser(JsonParser parser) throws IOException;
    
    public JsonTreeParser newTreeParser(JsonEventParser events) throws IOException;
    
    public JsonGenerator newGenerator(Writer output) throws IOException;
    
    public JsonTreeGenerator newTreeGenerator() throws IOException;
}
