package com.sigpwned.jsonification.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.sigpwned.jsonification.JsonParser;
import com.sigpwned.jsonification.exception.ParseJsonException;

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
public class DefaultJsonParserTest {
    @Test(expected=ParseJsonException.class)
    public void test1() throws IOException {
        try (JsonParser p=new DefaultJsonParser("{\"hello\":\"world\"]")) {
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
        try (JsonParser p=new DefaultJsonParser("{]")) {
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
        try (JsonParser p=new DefaultJsonParser("[\"hello\":\"world\"]")) {
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
                public void closeObject() {
                }
                
                @Override
                public void closeArray() {
                }
            });
        }
    }

    @Test
    public void test4() throws IOException {
        try (Reader r=new InputStreamReader(Thread.currentThread().getContextClassLoader().getResource("timeline.json").openStream(), StandardCharsets.UTF_8)) {
            try (JsonParser p=new DefaultJsonParser(r)) {
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
                    public void closeObject() {
                    }
                    
                    @Override
                    public void closeArray() {
                    }
                });
            }
        }
    }
}
