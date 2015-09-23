package com.sigpwned.jsonification.value;

import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.exception.IndexOutOfBoundsJsonException;

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
public interface JsonArray extends JsonValue, Iterable<JsonValue> {
    public JsonValue get(int index) throws IndexOutOfBoundsJsonException;
    
    public JsonValue set(int index, JsonValue value) throws IndexOutOfBoundsJsonException;
    
    public JsonValue set(int index, boolean value) throws IndexOutOfBoundsJsonException;
    
    public JsonValue set(int index, long value) throws IndexOutOfBoundsJsonException;
    
    public JsonValue set(int index, double value) throws IndexOutOfBoundsJsonException;
    
    public JsonValue set(int index, String value) throws IndexOutOfBoundsJsonException;
    
    public void add(JsonValue value);
    
    public void add(boolean value);
    
    public void add(long value);
    
    public void add(double value);
    
    public void add(String value);
    
    public void add(int index, JsonValue value) throws IndexOutOfBoundsJsonException;
    
    public void add(int index, boolean value) throws IndexOutOfBoundsJsonException;
    
    public void add(int index, long value) throws IndexOutOfBoundsJsonException;
    
    public void add(int index, double value) throws IndexOutOfBoundsJsonException;
    
    public void add(int index, String value) throws IndexOutOfBoundsJsonException;
    
    public JsonValue remove(int index) throws IndexOutOfBoundsJsonException;
    
    public int size();
}
