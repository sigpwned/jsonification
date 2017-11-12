package com.sigpwned.jsonification.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sigpwned.jsonification.Json;
import com.sigpwned.jsonification.JsonEvent;
import com.sigpwned.jsonification.JsonEventParser;
import com.sigpwned.jsonification.JsonFactory;
import com.sigpwned.jsonification.JsonTreeParser;
import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.exception.ParseJsonException;
import com.sigpwned.jsonification.impl.DefaultJsonFactory;
import com.sigpwned.jsonification.impl.DefaultJsonObject;

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
public class DefaultJsonValueParserTest {
    private JsonFactory oldFactory;
    
    @Before
    public void setupDefaultJsonValueParserTest() {
        oldFactory = Json.setDefaultFactory(new DefaultJsonFactory(DefaultJsonObject.KeyOrder.INSERTION));
    }
    
    @After
    public void cleanupDefaultJsonValueParserTest() {
        Json.setDefaultFactory(oldFactory);
    }
    
    @Test
    public void test1() throws IOException {
        JsonValue value;
        try (JsonTreeParser p=new DefaultJsonTreeParser("17 19")) {
            value = p.next();
        }
        
        try (JsonEventParser p=new DefaultJsonEventParser(new DefaultJsonValueParser(value))) {
            JsonEvent e1=p.next();
            assertThat(e1.getType(), is(JsonEvent.Type.SCALAR));
            assertThat(e1.getName(), nullValue());
            assertThat(e1.getValue().asNumber().longVal(), is(17L));

            JsonEvent e3=p.next();
            assertThat(e3, nullValue());
        }
    }

    @Test
    public void test2() throws IOException {
        JsonValue value;
        try (JsonTreeParser p=new DefaultJsonTreeParser("[ 123 , 456 ]")) {
            value = p.next();
        }
        
        try (JsonEventParser p=new DefaultJsonEventParser(new DefaultJsonValueParser(value))) {
            JsonEvent e1=p.next();
            assertThat(e1.getType(), is(JsonEvent.Type.OPEN_ARRAY));
            assertThat(e1.getName(), nullValue());
            assertThat(e1.getValue(), nullValue());

            JsonEvent e2=p.next();
            assertThat(e2.getType(), is(JsonEvent.Type.SCALAR));
            assertThat(e2.getName(), nullValue());
            assertThat(e2.getValue().asNumber().longVal(), is(123L));

            JsonEvent e3=p.next();
            assertThat(e3.getType(), is(JsonEvent.Type.SCALAR));
            assertThat(e3.getName(), nullValue());
            assertThat(e3.getValue().asNumber().longVal(), is(456L));
            
            JsonEvent e4=p.next();
            assertThat(e4.getType(), is(JsonEvent.Type.CLOSE_ARRAY));
            assertThat(e4.getName(), nullValue());
            assertThat(e4.getValue(), nullValue());
        
            JsonEvent e5=p.next();
            assertThat(e5, nullValue());
        }
    }

    @Test
    public void test3() throws IOException {
        JsonValue value;
        try (JsonTreeParser p=new DefaultJsonTreeParser("{ hello: \"world\", \"foo\": 123, \"wat\": [ \"man\", true, false, null ] }")) {
            value = p.next();
        }
        
        try (JsonEventParser p=new DefaultJsonEventParser(new DefaultJsonValueParser(value))) {
            JsonEvent e1=p.next();
            assertThat(e1.getType(), is(JsonEvent.Type.OPEN_OBJECT));
            assertThat(e1.getName(), nullValue());
            assertThat(e1.getValue(), nullValue());

            JsonEvent e2=p.next();
            assertThat(e2.getType(), is(JsonEvent.Type.SCALAR));
            assertThat(e2.getName(), is("hello"));
            assertThat(e2.getValue().asString().stringVal(), is("world"));

            JsonEvent e3=p.next();
            assertThat(e3.getType(), is(JsonEvent.Type.SCALAR));
            assertThat(e3.getName(), is("foo"));
            assertThat(e3.getValue().asNumber().longVal(), is(123L));
            
            JsonEvent e4=p.next();
            assertThat(e4.getType(), is(JsonEvent.Type.OPEN_ARRAY));
            assertThat(e4.getName(), is("wat"));
            assertThat(e4.getValue(), nullValue());
            
            JsonEvent e5=p.next();
            assertThat(e5.getType(), is(JsonEvent.Type.SCALAR));
            assertThat(e5.getName(), nullValue());
            assertThat(e5.getValue().asString().stringVal(), is("man"));
            
            JsonEvent e6=p.next();
            assertThat(e6.getType(), is(JsonEvent.Type.SCALAR));
            assertThat(e6.getName(), nullValue());
            assertThat(e6.getValue().asBoolean().booleanVal(), is(true));
            
            JsonEvent e7=p.next();
            assertThat(e7.getType(), is(JsonEvent.Type.SCALAR));
            assertThat(e7.getName(), nullValue());
            assertThat(e7.getValue().asBoolean().booleanVal(), is(false));
            
            JsonEvent e8=p.next();
            assertThat(e8.getType(), is(JsonEvent.Type.SCALAR));
            assertThat(e8.getName(), nullValue());
            assertThat(e8.getValue().isNull(), is(true));
            
            JsonEvent e9=p.next();
            assertThat(e9.getType(), is(JsonEvent.Type.CLOSE_ARRAY));
            assertThat(e9.getName(), nullValue());
            assertThat(e9.getValue(), nullValue());
            
            JsonEvent e10=p.next();
            assertThat(e10.getType(), is(JsonEvent.Type.CLOSE_OBJECT));
            assertThat(e10.getName(), nullValue());
            assertThat(e10.getValue(), nullValue());
            
            JsonEvent e11=p.next();
            assertThat(e11, nullValue());
        }
    }

    @Test
    public void test4() throws IOException {
        JsonValue value;
        try (JsonTreeParser p=new DefaultJsonTreeParser("{}")) {
            value = p.next();
        }
        
        try (JsonEventParser p=new DefaultJsonEventParser(new DefaultJsonValueParser(value))) {
            JsonEvent e1=p.next();
            assertThat(e1.getType(), is(JsonEvent.Type.OPEN_OBJECT));
            assertThat(e1.getName(), nullValue());
            assertThat(e1.getValue(), nullValue());
            
            JsonEvent e2=p.next();
            assertThat(e2.getType(), is(JsonEvent.Type.CLOSE_OBJECT));
            assertThat(e2.getName(), nullValue());
            assertThat(e2.getValue(), nullValue());
            
            JsonEvent e3=p.next();
            assertThat(e3, nullValue());
        }
    }

    @Test
    public void test5() throws IOException {
        JsonValue v;
        try (JsonTreeParser p=new DefaultJsonTreeParser("{\"hello\":\"world\"}")) {
            v = p.next();
        }
        
        try (JsonEventParser p=new DefaultJsonEventParser(new DefaultJsonValueParser(v))) {
            p.openObject();
            
            p.nextName("hello");
            String value=p.scalar().getValue().asString().stringVal();
            
            p.closeObject();
            
            assertThat(value, is("world"));
        }
    }

    @Test(expected=ParseJsonException.class)
    public void test6() throws IOException {
        JsonValue v;
        try (JsonTreeParser p=new DefaultJsonTreeParser("{\"hello\":\"world\"}")) {
            v = p.next();
        }
        
        try (JsonEventParser p=new DefaultJsonEventParser(new DefaultJsonValueParser(v))) {
            p.openObject();
            
            // This will fail since we didn't provide the name!
            p.scalar().getValue().asString().stringVal();
        }
    }

    @Test
    public void test7() throws IOException {
        JsonValue v;
        try (JsonTreeParser p=new DefaultJsonTreeParser("{fact:\"world\"}")) {
            v = p.next();
        }
        
        try (JsonEventParser p=new DefaultJsonEventParser(new DefaultJsonValueParser(v))) {
            p.openObject();
            
            p.nextName("fact");
            String value=p.scalar().getValue().asString().stringVal();
            
            p.closeObject();
            
            assertThat(value, is("world"));
        }
    }

    @Test
    public void test8() throws IOException {
        JsonValue v;
        try (JsonTreeParser p=new DefaultJsonTreeParser("{hello:\"world\"}")) {
            v = p.next();
        }
        
        try (JsonEventParser p=new DefaultJsonEventParser(new DefaultJsonValueParser(v))) {
            p.openObject();
            
            p.nextName("hello");
            String value=p.scalar().getValue().asString().stringVal();
            
            p.closeObject();
            
            assertThat(value, is("world"));
        }
    }
}
