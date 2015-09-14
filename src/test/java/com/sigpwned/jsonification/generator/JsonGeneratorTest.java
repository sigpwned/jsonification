package com.sigpwned.jsonification.generator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.sigpwned.jsonification.exception.GenerateJsonException;

public class JsonGeneratorTest {
    @Test
    public void test1() throws IOException {
        StringWriter w=new StringWriter();
        try {
            try (JsonGenerator g=new JsonGenerator(w)) {
                g.openObject();
                g.value("hello", 123);
                g.value("world", "My man");
                g.openArray("dude");
                g.value("man");
                g.value("word");
                g.closeArray();
                g.closeObject();
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
            try (JsonGenerator g=new JsonGenerator(w)) {
                g.openObject();
                g.value("hello", 123);
                g.value("world", "My man");
                g.openArray("dude");
                g.value("man");
                g.value("word");
                g.closeObject();
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
            try (JsonGenerator g=new JsonGenerator(w)) {
                g.closeObject();
            }
        }
        finally {
            w.close();
        }
    }
}
