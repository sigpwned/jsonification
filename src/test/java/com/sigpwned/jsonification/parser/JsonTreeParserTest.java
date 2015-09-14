package com.sigpwned.jsonification.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonObject;
import com.sigpwned.jsonification.value.scalar.JsonNumber;

public class JsonTreeParserTest {
    @Test
    public void test1() throws IOException {
        try (JsonTreeParser p=new JsonTreeParser("17 19")) {
            JsonNumber v1=p.next().asScalar().asNumber();
            assertThat(v1.asScalar().asNumber().longVal(), is(17L));

            JsonNumber v2=p.next().asScalar().asNumber();
            assertThat(v2.asScalar().asNumber().longVal(), is(19L));
        }
    }

    @Test
    public void test2() throws IOException {
        try (JsonTreeParser p=new JsonTreeParser("[ 123 , 456 ]")) {
            JsonArray v1=p.next().asArray();
            
            assertThat(v1.size(), is(2));
            assertThat(v1.get(0).asScalar().asNumber().longVal(), is(123L));
            assertThat(v1.get(1).asScalar().asNumber().longVal(), is(456L));
        }
    }

    @Test
    public void test3() throws IOException {
        try (JsonTreeParser p=new JsonTreeParser("{ hello: \"world\", \"foo\": 123, \"wat\": [ \"man\", true, false, null ] }")) {
            JsonObject v1=p.next().asObject();
            
            assertThat(v1.size(), is(3));
            assertThat(v1.get("hello").asScalar().asString().stringVal(), is("world"));
            assertThat(v1.get("foo").asScalar().asNumber().longVal(), is(123L));
        
            JsonArray v2=v1.get("wat").asArray();
            assertThat(v2.size(), is(4));
            assertThat(v2.get(0).asScalar().asString().stringVal(), is("man"));
            assertThat(v2.get(1).asScalar().asBoolean().booleanVal(), is(true));
            assertThat(v2.get(2).asScalar().asBoolean().booleanVal(), is(false));
            assertThat(v2.get(3).asScalar().isNull(), is(true));
        }
    }
}
