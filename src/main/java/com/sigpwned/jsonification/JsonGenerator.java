package com.sigpwned.jsonification;

import java.io.IOException;

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
public interface JsonGenerator extends AutoCloseable {
    public void openObject() throws IOException;

    public void openObject(String name) throws IOException;

    public void closeObject() throws IOException;

    public void openArray() throws IOException;

    public void openArray(String name) throws IOException;

    public void closeArray() throws IOException;

    public void scalar(Long value) throws IOException;

    public void scalar(String name, Long value) throws IOException;

    public void scalar(long value) throws IOException;

    public void scalar(String name, long value) throws IOException;

    public void scalar(Double value) throws IOException;

    public void scalar(String name, Double value) throws IOException;

    public void scalar(double value) throws IOException;

    public void scalar(String name, double value) throws IOException;

    public void scalar(String value) throws IOException;

    public void scalar(String name, String value) throws IOException;

    public void scalar(Boolean value) throws IOException;

    public void scalar(String name, Boolean value) throws IOException;

    public void scalar(boolean value) throws IOException;

    public void scalar(String name, boolean value) throws IOException;

    public void nil() throws IOException;

    public void nil(String name) throws IOException;

    public void value(JsonValue value) throws IOException;

    public void value(String name, JsonValue value) throws IOException;

    public void setNextName(String nextName);

    public String getNextName();
    
    public void close() throws IOException;

}