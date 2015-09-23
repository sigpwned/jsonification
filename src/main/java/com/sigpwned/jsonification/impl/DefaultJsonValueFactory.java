package com.sigpwned.jsonification.impl;

import com.sigpwned.jsonification.JsonValueFactory;
import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonNull;
import com.sigpwned.jsonification.value.JsonObject;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;
import com.sigpwned.jsonification.value.scalar.JsonNumber;
import com.sigpwned.jsonification.value.scalar.JsonString;

public class DefaultJsonValueFactory implements JsonValueFactory {
    private DefaultJsonObject.KeyOrder keyOrder;
    
    public DefaultJsonValueFactory() {
        this(DefaultJsonObject.KeyOrder.UNORDERED);
    }
    
    public DefaultJsonValueFactory(DefaultJsonObject.KeyOrder keyOrder) {
        if(keyOrder == null)
            throw new NullPointerException();
        this.keyOrder = keyOrder;
    }
    
    @Override
    public JsonObject newObject() {
        return new DefaultJsonObject(getKeyOrder());
    }

    @Override
    public JsonArray newArray() {
        return new DefaultJsonArray();
    }

    @Override
    public JsonBoolean newValue(boolean value) {
        return DefaultJsonBoolean.valueOf(value);
    }

    @Override
    public JsonNumber newValue(long value) {
        return DefaultJsonNumber.valueOf(value);
    }

    @Override
    public JsonNumber newValue(double value) {
        return DefaultJsonNumber.valueOf(value);
    }

    @Override
    public JsonString newValue(String value) {
        return DefaultJsonString.valueOf(value);
    }

    @Override
    public JsonNull newNull() {
        return JsonNull.NULL;
    }

    public DefaultJsonObject.KeyOrder getKeyOrder() {
        return keyOrder;
    }
    
    public void setKeyOrder(DefaultJsonObject.KeyOrder keyOrder) {
        if(keyOrder == null)
            throw new NullPointerException();
        this.keyOrder = keyOrder;
    }
}
