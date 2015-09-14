package com.sigpwned.jsonification.generator;

import java.io.IOException;
import java.io.Writer;

import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.JsonWalker;

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
public class JsonTreeGenerator implements AutoCloseable {
    private static class GeneratorException extends RuntimeException {
        private static final long serialVersionUID = 7564693885415072263L;

        public GeneratorException(IOException cause) {
            super(cause);
        }
        
        public IOException getCause() {
            return (IOException) super.getCause();
        }
    }

    private final JsonGenerator generator;
    
    public JsonTreeGenerator(Writer writer) {
        this(new JsonGenerator(writer));
    }
    
    public JsonTreeGenerator(JsonGenerator generator) {
        this.generator = generator;
    }
    
    public void tree(JsonValue value) throws IOException {
        try {
            new JsonWalker(value).walk(new JsonWalker.Handler() {
                @Override
                public void scalar(String name, String value) {
                    try {
                        getGenerator().value(name, value);
                    }
                    catch(IOException e) {
                        throw new GeneratorException(e);
                    }
                }
                
                @Override
                public void scalar(String name, boolean value) {
                    try {
                        getGenerator().value(name, value);
                    }
                    catch(IOException e) {
                        throw new GeneratorException(e);
                    }
                }
                
                @Override
                public void scalar(String name, double value) {
                    try {
                        getGenerator().value(name, value);
                    }
                    catch(IOException e) {
                        throw new GeneratorException(e);
                    }
                }
                
                @Override
                public void scalar(String name, long value) {
                    try {
                        getGenerator().value(name, value);
                    }
                    catch(IOException e) {
                        throw new GeneratorException(e);
                    }
                }
                
                @Override
                public void openObject(String name) {
                    try {
                        getGenerator().openObject(name);
                    }
                    catch(IOException e) {
                        throw new GeneratorException(e);
                    }
                }
                
                @Override
                public void nil(String name) {
                    try {
                        getGenerator().nil(name);
                    }
                    catch(IOException e) {
                        throw new GeneratorException(e);
                    }
                }
                
                @Override
                public void closeObject() {
                    try {
                        getGenerator().closeObject();
                    }
                    catch(IOException e) {
                        throw new GeneratorException(e);
                    }
                }
                
                @Override
                public void openArray(String name) {
                    try {
                        getGenerator().openArray(name);
                    }
                    catch(IOException e) {
                        throw new GeneratorException(e);
                    }
                }
                
                @Override
                public void closeArray() {
                    try {
                        getGenerator().closeArray();
                    }
                    catch(IOException e) {
                        throw new GeneratorException(e);
                    }
                }
            });
        }
        catch(GeneratorException e) {
            throw e.getCause();
        }
    }

    private JsonGenerator getGenerator() {
        return generator;
    }

    public void close() throws IOException {
        getGenerator().close();
    }
}
