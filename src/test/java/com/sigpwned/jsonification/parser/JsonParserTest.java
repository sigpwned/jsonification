package com.sigpwned.jsonification.parser;

import java.io.IOException;

import org.junit.Test;

import com.sigpwned.jsonification.exception.ParseJsonException;

public class JsonParserTest {
    @Test(expected=ParseJsonException.class)
    public void test1() throws IOException {
        try (JsonParser p=new JsonParser("{\"hello\":\"world\"]")) {
            p.parse(new JsonParser.Handler() {
                @Override
                public void scalar(String name, String value) {
                }
                
                @Override
                public void scalar(String name, boolean value) {
                }
                
                @Override
                public void scalar(String name, double value) {
                }
                
                @Override
                public void scalar(String name, long value) {
                }
                
                @Override
                public void openObject(String name) {
                }
                
                @Override
                public void openArray(String name) {
                }
                
                @Override
                public void nil(String name) {
                }
                
                @Override
                public void eof() {
                }
                
                @Override
                public void closeObject() {
                }
                
                @Override
                public void closeArray() {
                }
            });
        }
    }

    @Test(expected=ParseJsonException.class)
    public void test2() throws IOException {
        try (JsonParser p=new JsonParser("{]")) {
            p.parse(new JsonParser.Handler() {
                @Override
                public void scalar(String name, String value) {
                }
                
                @Override
                public void scalar(String name, boolean value) {
                }
                
                @Override
                public void scalar(String name, double value) {
                }
                
                @Override
                public void scalar(String name, long value) {
                }
                
                @Override
                public void openObject(String name) {
                }
                
                @Override
                public void openArray(String name) {
                }
                
                @Override
                public void nil(String name) {
                }
                
                @Override
                public void eof() {
                }
                
                @Override
                public void closeObject() {
                }
                
                @Override
                public void closeArray() {
                }
            });
        }
    }

    @Test(expected=ParseJsonException.class)
    public void test3() throws IOException {
        try (JsonParser p=new JsonParser("[\"hello\":\"world\"]")) {
            p.parse(new JsonParser.Handler() {
                @Override
                public void scalar(String name, String value) {
                }
                
                @Override
                public void scalar(String name, boolean value) {
                }
                
                @Override
                public void scalar(String name, double value) {
                }
                
                @Override
                public void scalar(String name, long value) {
                }
                
                @Override
                public void openObject(String name) {
                }
                
                @Override
                public void openArray(String name) {
                }
                
                @Override
                public void nil(String name) {
                }
                
                @Override
                public void eof() {
                }
                
                @Override
                public void closeObject() {
                }
                
                @Override
                public void closeArray() {
                }
            });
        }
    }
}
