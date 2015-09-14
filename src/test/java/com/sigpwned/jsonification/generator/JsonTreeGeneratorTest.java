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
