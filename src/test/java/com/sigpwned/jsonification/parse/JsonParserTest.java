package com.sigpwned.jsonification.parse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.sigpwned.jsonification.JsonEvent;
import com.sigpwned.jsonification.parser.JsonParser;

public class JsonParserTest {
    @Test
    public void test1() throws IOException {
        try (JsonParser p=new JsonParser("17")) {
            JsonEvent e1=p.parse();
            assertThat(e1.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e1.getName(), nullValue());
            assertThat(e1.getValue().asNumber().longVal(), is(17L));

            JsonEvent e2=p.parse();
            assertThat(e2.getType(), is(JsonEvent.Type.EOF));
            assertThat(e2.getName(), nullValue());
            assertThat(e2.getValue(), nullValue());
        }
    }

    @Test
    public void test2() throws IOException {
        try (JsonParser p=new JsonParser("[ 123 , 456 ]")) {
            JsonEvent e1=p.parse();
            assertThat(e1.getType(), is(JsonEvent.Type.OPEN_ARRAY));
            assertThat(e1.getName(), nullValue());
            assertThat(e1.getValue(), nullValue());

            JsonEvent e2=p.parse();
            assertThat(e2.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e2.getName(), nullValue());
            assertThat(e2.getValue().asNumber().longVal(), is(123L));

            JsonEvent e3=p.parse();
            assertThat(e3.getType(), is(JsonEvent.Type.VALUE));
            assertThat(e3.getName(), nullValue());
            assertThat(e3.getValue().asNumber().longVal(), is(456L));
            
            JsonEvent e4=p.parse();
            assertThat(e4.getType(), is(JsonEvent.Type.CLOSE_ARRAY));
            assertThat(e4.getName(), nullValue());
            assertThat(e4.getValue(), nullValue());
        
            JsonEvent e5=p.parse();
            assertThat(e5.getType(), is(JsonEvent.Type.EOF));
            assertThat(e5.getName(), nullValue());
            assertThat(e5.getValue(), nullValue());
        }
    }
}
