package com.sigpwned.jsonification.generator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.sigpwned.jsonification.Json;
import com.sigpwned.jsonification.JsonGenerator;
import com.sigpwned.jsonification.exception.GenerateJsonException;
import com.sigpwned.jsonification.impl.DefaultJsonObject;
import com.sigpwned.jsonification.impl.DefaultJsonValueFactory;

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
public class TreeJsonGeneratorTest {
    @Test
    public void test1() throws IOException {
        StringWriter w=new StringWriter();
        try {
            try (TreeJsonGenerator g=new TreeJsonGenerator(new DefaultJsonValueFactory(DefaultJsonObject.KeyOrder.INSERTION))) {
                g.openObject();
                g.scalar("hello", 123);
                g.scalar("world", "My man");
                g.openArray("dude");
                g.scalar("man");
                g.scalar("word");
                g.closeArray();
                g.closeObject();
                w.write(Json.emit(g.getValue()));
            }
        }
        finally {
            w.close();
        }
        assertThat(w.toString(), is("{\"hello\":123,\"world\":\"My man\",\"dude\":[\"man\",\"word\"]}"));
    }

    @Test(expected=GenerateJsonException.class)
    public void test2() throws IOException {
        StringWriter w=new StringWriter();
        try {
            try (TreeJsonGenerator g=new TreeJsonGenerator(new DefaultJsonValueFactory(DefaultJsonObject.KeyOrder.INSERTION))) {
                g.openObject();
                g.scalar("hello", 123);
                g.scalar("world", "My man");
                g.openArray("dude");
                g.scalar("man");
                g.scalar("word");
                g.closeObject();
                w.write(Json.emit(g.getValue()));
            }
        }
        finally {
            w.close();
        }
    }

    @Test(expected=GenerateJsonException.class)
    public void test3() throws IOException {
        StringWriter w=new StringWriter();
        try {
            try (JsonGenerator g=new TreeJsonGenerator()) {
                g.closeObject();
            }
        }
        finally {
            w.close();
        }
    }
}
