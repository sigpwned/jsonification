package com.sigpwned.jsonification.value;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.exception.NullJsonException;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;
import com.sigpwned.jsonification.value.scalar.JsonNumber;
import com.sigpwned.jsonification.value.scalar.JsonString;


public interface JsonNull extends JsonBoolean, JsonNumber, JsonString, JsonObject, JsonArray {
    public static final JsonNull NULL=new JsonNull() {
        public Boolean getBooleanValue() {
            return null;
        }

        public boolean booleanVal() {
            throw new NullJsonException();
        }

        public boolean isNull() {
            return true;
        }

        public JsonValue.Type getType() {
            return JsonValue.Type.NULL;
        }

        public Number getNumberValue() {
            return null;
        }

        public long longVal() {
            throw new NullJsonException();
        }

        public double doubleVal() {
            throw new NullJsonException();
        }

        public String getStringValue() {
            return null;
        }

        public String stringVal() {
            throw new NullJsonException();
        }

        public JsonValue get(String name) {
            throw new NullJsonException();
        }

        public void set(String name, JsonValue value) {
            throw new NullJsonException();
        }

        public boolean has(String name) {
            throw new NullJsonException();
        }

        public Set<String> keys() {
            throw new NullJsonException();
        }

        public Collection<JsonValue> values() {
            throw new NullJsonException();
        }

        public Iterable<Entry> entries() {
            throw new NullJsonException();
        }

        public JsonValue get(int index) {
            throw new NullJsonException();
        }

        public JsonValue set(int index, JsonValue value) {
            throw new NullJsonException();
        }

        public void add(JsonValue value) {
            throw new NullJsonException();
        }

        public void add(int index, JsonValue value) {
            throw new NullJsonException();
        }

        public JsonValue remove(int index) {
            throw new NullJsonException();
        }
        
        public JsonValue remove(String name) {
            throw new NullJsonException();
        }

        public int size() {
            throw new NullJsonException();
        }

        public Iterator<JsonValue> iterator() {
            throw new NullJsonException();
        }

        public ScalarJsonValue.Flavor getFlavor() {
            return ScalarJsonValue.Flavor.NULL;
        }

        public Object getValue() {
            return null;
        }

        public JsonNumber asNumber() {
            return this;
        }

        public JsonBoolean asBoolean() {
            return this;
        }

        public JsonString asString() {
            return this;
        }

        public JsonObject asObject() {
            return this;
        }

        public JsonArray asArray() {
            return this;
        }

        public ScalarJsonValue asScalar() {
            return this;
        }
    };
}
