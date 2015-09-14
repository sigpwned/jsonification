package com.sigpwned.jsonification.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.sigpwned.jsonification.JsonEvent;
import com.sigpwned.jsonification.parser.JsonEventParser;

public class JsonEventParserTest {
    @Test
    public void test1() throws IOException {
        try (JsonEventParser p=new JsonEventParser("17 19")) {
            JsonEvent e1=p.next();
            assertThat(e1.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e1.getName(), nullValue());
            assertThat(e1.getValue().asNumber().longVal(), is(17L));

            JsonEvent e2=p.next();
            assertThat(e2.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e2.getName(), nullValue());
            assertThat(e2.getValue().asNumber().longVal(), is(19L));

            JsonEvent e3=p.next();
            assertThat(e3.getType(), is(JsonEvent.Type.EOF));
            assertThat(e3.getName(), nullValue());
            assertThat(e3.getValue(), nullValue());
        }
    }

    @Test
    public void test2() throws IOException {
        try (JsonEventParser p=new JsonEventParser("[ 123 , 456 ]")) {
            JsonEvent e1=p.next();
            assertThat(e1.getType(), is(JsonEvent.Type.OPEN_ARRAY));
            assertThat(e1.getName(), nullValue());
            assertThat(e1.getValue(), nullValue());

            JsonEvent e2=p.next();
            assertThat(e2.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e2.getName(), nullValue());
            assertThat(e2.getValue().asNumber().longVal(), is(123L));

            JsonEvent e3=p.next();
            assertThat(e3.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e3.getName(), nullValue());
            assertThat(e3.getValue().asNumber().longVal(), is(456L));
            
            JsonEvent e4=p.next();
            assertThat(e4.getType(), is(JsonEvent.Type.CLOSE_ARRAY));
            assertThat(e4.getName(), nullValue());
            assertThat(e4.getValue(), nullValue());
        
            JsonEvent e5=p.next();
            assertThat(e5.getType(), is(JsonEvent.Type.EOF));
            assertThat(e5.getName(), nullValue());
            assertThat(e5.getValue(), nullValue());
        }
    }

    @Test
    public void test3() throws IOException {
        try (JsonEventParser p=new JsonEventParser("{ hello: \"world\", \"foo\": 123, \"wat\": [ \"man\", true, false, null ] }")) {
            JsonEvent e1=p.next();
            assertThat(e1.getType(), is(JsonEvent.Type.OPEN_OBJECT));
            assertThat(e1.getName(), nullValue());
            assertThat(e1.getValue(), nullValue());

            JsonEvent e2=p.next();
            assertThat(e2.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e2.getName(), is("hello"));
            assertThat(e2.getValue().asString().stringVal(), is("world"));

            JsonEvent e3=p.next();
            assertThat(e3.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e3.getName(), is("foo"));
            assertThat(e3.getValue().asNumber().longVal(), is(123L));
            
            JsonEvent e4=p.next();
            assertThat(e4.getType(), is(JsonEvent.Type.OPEN_ARRAY));
            assertThat(e4.getName(), is("wat"));
            assertThat(e4.getValue(), nullValue());
            
            JsonEvent e5=p.next();
            assertThat(e5.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e5.getName(), nullValue());
            assertThat(e5.getValue().asString().stringVal(), is("man"));
            
            JsonEvent e6=p.next();
            assertThat(e6.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e6.getName(), nullValue());
            assertThat(e6.getValue().asBoolean().booleanVal(), is(true));
            
            JsonEvent e7=p.next();
            assertThat(e7.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e7.getName(), nullValue());
            assertThat(e7.getValue().asBoolean().booleanVal(), is(false));
            
            JsonEvent e8=p.next();
            assertThat(e8.getType(), is(JsonEvent.Type.VALUE));
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
            assertThat(e11.getType(), is(JsonEvent.Type.EOF));
            assertThat(e11.getName(), nullValue());
            assertThat(e11.getValue(), nullValue());
        }
    }

    @Test
    public void test4() throws IOException {
        try (JsonEventParser p=new JsonEventParser("{}")) {
            JsonEvent e1=p.next();
            assertThat(e1.getType(), is(JsonEvent.Type.OPEN_OBJECT));
            assertThat(e1.getName(), nullValue());
            assertThat(e1.getValue(), nullValue());
            
            JsonEvent e2=p.next();
            assertThat(e2.getType(), is(JsonEvent.Type.CLOSE_OBJECT));
            assertThat(e2.getName(), nullValue());
            assertThat(e2.getValue(), nullValue());
            
            JsonEvent e3=p.next();
            assertThat(e3.getType(), is(JsonEvent.Type.EOF));
            assertThat(e3.getName(), nullValue());
            assertThat(e3.getValue(), nullValue());
        }
    }
}
