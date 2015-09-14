package com.sigpwned.jsonification;

import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonNull;
import com.sigpwned.jsonification.value.JsonObject;
import com.sigpwned.jsonification.value.scalar.JsonBoolean;
import com.sigpwned.jsonification.value.scalar.JsonNumber;
import com.sigpwned.jsonification.value.scalar.JsonString;

public interface JsonValueFactory {
    public JsonObject newObject();

    public JsonArray newArray();

    public JsonBoolean newValue(boolean value);

    public JsonNumber newValue(long value);

    public JsonNumber newValue(double value);

    public JsonString newValue(String value);
    
    public JsonNull newNull();
}
