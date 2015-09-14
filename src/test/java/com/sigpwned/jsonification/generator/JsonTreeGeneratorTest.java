package com.sigpwned.jsonification.generator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;

import org.junit.Test;

import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.impl.DefaultJsonArray;
import com.sigpwned.jsonification.impl.DefaultJsonObject;
import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonObject;

public class JsonTreeGeneratorTest {
    @Test
    public void test1() throws IOException {
        JsonObject o=new DefaultJsonObject(new LinkedHashMap<String,JsonValue>());
        o.set("hello", "world");
        o.set("dude", 123L);
        
        JsonArray a=new DefaultJsonArray();
        a.add(true);
        a.add(o);
        a.add("america");
        
        StringWriter w=new StringWriter();
        try {
            try (JsonTreeGenerator g=new JsonTreeGenerator(w)) {
                g.tree(a);
            }
        }
        finally {
            w.close();
        }
        assertThat(w.toString(), is("[true,{\"hello\":\"world\",\"dude\":123},\"america\"]"));
    }
}
