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
public interface JsonParser extends AutoCloseable {
    public static interface Handler {
        public void openObject(String name);
        
        public void closeObject();
        
        public void openArray(String name);
        
        public void closeArray();
        
        public void nil(String name);
        
        public void scalar(String name, long value);
        
        public void scalar(String name, double value);
        
        public void scalar(String name, boolean value);
        
        public void scalar(String name, String value);
    }

    /**
     * Handle JSON events until one complete JSON value has been parsed. A
     * JSON value is one complete scalar, object, array, or nil. If the given
     * input contains more than one complete JSON value, only the first is
     * parsed.
     * 
     * @param handler the handler to receive parse events
     * 
     * @return {@code true} if a value is parsed, or {@code false} otherwise
     * 
     * @throws IOException if an underlying {@code IOException} occurs while
     *         reading JSON
     */
    public boolean parse(JsonParser.Handler handler) throws IOException;

    public void next(JsonParser.Handler handler) throws IOException;

    public void close() throws IOException;

}