package com.sigpwned.jsonification.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.sigpwned.jsonification.JsonEventParser;
import com.sigpwned.jsonification.JsonFactory;
import com.sigpwned.jsonification.JsonGenerator;
import com.sigpwned.jsonification.JsonParser;
import com.sigpwned.jsonification.JsonTreeGenerator;
import com.sigpwned.jsonification.JsonTreeParser;
import com.sigpwned.jsonification.generator.DefaultJsonGenerator;
import com.sigpwned.jsonification.generator.DefaultJsonTreeGenerator;
import com.sigpwned.jsonification.parser.DefaultJsonEventParser;
import com.sigpwned.jsonification.parser.DefaultJsonParser;
import com.sigpwned.jsonification.parser.DefaultJsonTreeParser;
import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonNull;
import com.sigpwned.jsonification.value.JsonObject;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;
import com.sigpwned.jsonification.value.scalar.JsonNumber;
import com.sigpwned.jsonification.value.scalar.JsonString;

public class DefaultJsonFactory implements JsonFactory {
    private DefaultJsonObject.KeyOrder keyOrder;
    
    public DefaultJsonFactory() {
        this(DefaultJsonObject.KeyOrder.UNORDERED);
    }
    
    public DefaultJsonFactory(DefaultJsonObject.KeyOrder keyOrder) {
        if(keyOrder == null)
            throw new NullPointerException();
        this.keyOrder = keyOrder;
    }
    
    @Override
    public JsonObject newObject() {
        return new DefaultJsonObject(getKeyOrder());
    }

    @Override
    public JsonArray newArray() {
        return new DefaultJsonArray();
    }

    @Override
    public JsonBoolean newValue(boolean value) {
        return DefaultJsonBoolean.valueOf(value);
    }

    @Override
    public JsonNumber newValue(long value) {
        return DefaultJsonNumber.valueOf(value);
    }

    @Override
    public JsonNumber newValue(double value) {
        return DefaultJsonNumber.valueOf(value);
    }

    @Override
    public JsonString newValue(String value) {
        return DefaultJsonString.valueOf(value);
    }

    @Override
    public JsonNull newNull() {
        return JsonNull.NULL;
    }

    public DefaultJsonObject.KeyOrder getKeyOrder() {
        return keyOrder;
    }
    
    public void setKeyOrder(DefaultJsonObject.KeyOrder keyOrder) {
        if(keyOrder == null)
            throw new NullPointerException();
        this.keyOrder = keyOrder;
    }

    @Override
    public JsonParser newParser(Reader input) throws IOException {
        return new DefaultJsonParser(input);
    }

    @Override
    public JsonEventParser newEventParser(Reader input) throws IOException {
        return newEventParser(newParser(input));
    }

    @Override
    public JsonEventParser newEventParser(JsonParser parser) throws IOException {
        return new DefaultJsonEventParser(parser);
    }

    @Override
    public JsonTreeParser newTreeParser(Reader input) throws IOException {
        return newTreeParser(newParser(input));
    }

    @Override
    public JsonTreeParser newTreeParser(JsonParser parser) throws IOException {
        return newTreeParser(newEventParser(parser));
    }

    @Override
    public JsonTreeParser newTreeParser(JsonEventParser events) throws IOException {
        return new DefaultJsonTreeParser(events);
    }

    @Override
    public JsonGenerator newGenerator(Writer output) throws IOException {
        return new DefaultJsonGenerator(output);
    }

    @Override
    public JsonTreeGenerator newTreeGenerator() throws IOException {
        return new DefaultJsonTreeGenerator();
    }
}
