package com.sigpwned.jsonification.value;

import java.util.Collection;
import java.util.Set;

import com.sigpwned.jsonification.JsonValue;

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
public interface JsonObject extends JsonValue {
    /**
     * A name/value pair
     */
    public static interface Entry {
        public String getName();
        
        public JsonValue getValue();
    }
    
    /**
     * @param name The name of the value to retrieve
     * @return the {@code JsonValue} associated with the given {@code name}, or
     *         {@code null} if no such value exists.
     */
    public JsonValue get(String name);

    /**
     * Associates {@code value} with {@code name} in this object. If
     * {@code name} was not already associated with a value in this object, it
     * is added. If {@code value} is {@code null}, then it is stored as
     * {@link com.sigpwned.jsonification.Json#NULL} instead.
     * 
     * @param name The name to associate with the given value
     * @param value The value to associate
     * @return The current object, to chain invocations
     */
    public JsonObject set(String name, JsonValue value);
    
    /**
     * Associates a new {@link com.sigpwned.jsonification.value.scalar.JsonBoolean}
     * representing {@code value} with {@code name} in this object. If
     * {@code name} was not already associated with a value in this object, it
     * is added.
     * 
     * @param name The name to associate with the given value
     * @param value The value to associate
     * @return The current object, to chain invocations
     */
    public JsonObject set(String name, boolean value);
    
    /**
     * Associates a new {@link com.sigpwned.jsonification.value.scalar.JsonNumber}
     * representing {@code value} with {@code name} in this object. If
     * {@code name} was not already associated with a value in this object, it
     * is added.
     * 
     * @param name The name to associate with the given value
     * @param value The value to associate
     * @return The current object, to chain invocations
     */
    public JsonObject set(String name, long value);
    
    /**
     * Associates a new {@link com.sigpwned.jsonification.value.scalar.JsonNumber}
     * representing {@code value} with {@code name} in this object. If
     * {@code name} was not already associated with a value in this object, it
     * is added.
     * 
     * @param name The name to associate with the given value
     * @param value The value to associate
     * @return The current object, to chain invocations
     */
    public JsonObject set(String name, double value);
    
    /**
     * Associates a new {@link com.sigpwned.jsonification.value.scalar.JsonString}
     * representing {@code value} with {@code name} in this object. If
     * {@code name} was not already associated with a value in this object, it
     * is added. If {@code value} is {@code null}, then it is stored as
     * {@link com.sigpwned.jsonification.Json#NULL} instead.
     * 
     * @param name The name to associate with the given value
     * @param value The value to associate
     * @return The current object, to chain invocations
     */
    public JsonObject set(String name, String value);
    
    /**
     * @param name The name to test
     * @return {@code true} if the given {@code name} is associated with a
     *         value in this object, or {@code false} otherwise
     */
    public boolean has(String name);
    
    /**
     * Removes the value associated with the given {@code name} from this
     * object. 
     * 
     * @param name The name for which to remove the associated value
     * @return The value associated with the given name, or {@code null} if no
     *         value was associated
     */
    public JsonValue remove(String name);
    
    /**
     * @return {@link Set} of all names associated with values in this object.
     *         The order of the keys in the set is undefined by default.
     *         Defined key orderings can be achieved by using a custom
     *         {@link com.sigpwned.jsonification.JsonFactory} implementation.
     */
    public Set<String> keys();
    
    /**
     * @return {@link Collection} of all values associated with names in this
     *         object. The order of the values in the set is undefined by
     *         default. Defined key orderings can be achieved by using a custom
     *         {@link com.sigpwned.jsonification.JsonFactory} implementation.
     */
    public Collection<JsonValue> values();
    
    /**
     * @return A sequence of the name/value pairs contained in this object. The
     *         order of the values in the set is undefined by default. Defined
     *         sequence orderings can be achieved by using a custom
     *         {@link com.sigpwned.jsonification.JsonFactory} implementation.
     */
    public Iterable<JsonObject.Entry> entries();
    
    /**
     * @return the number of associated name/value pairs in this object.
     */
    public int size();
}
