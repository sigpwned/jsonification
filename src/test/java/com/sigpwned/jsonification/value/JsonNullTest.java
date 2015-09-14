package com.sigpwned.jsonification.value;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.sigpwned.jsonification.Json;
import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.exception.NullJsonException;

public class JsonNullTest {
    @Test(expected=NullJsonException.class)
    public void addTest1() {
        JsonNull.NULL.add(Json.TRUE);
    }

    @Test(expected=NullJsonException.class)
    public void addTest2() {
        JsonNull.NULL.add(0, Json.TRUE);
    }

    @Test
    public void asArrayTest() {
        JsonNull.NULL.asArray();
    }

    @Test
    public void asBooleanTest() {
        JsonNull.NULL.asBoolean();
    }

    @Test
    public void asNumberTest() {
        JsonNull.NULL.asNumber();
    }

    @Test
    public void asObjectTest() {
        JsonNull.NULL.asObject();
    }

    @Test
    public void asScalarTest() {
        JsonNull.NULL.asScalar();
    }

    @Test
    public void asStringTest() {
        JsonNull.NULL.asString();
    }

    @Test(expected=NullJsonException.class)
    public void booleanValTest() {
        JsonNull.NULL.booleanVal();
    }

    @Test(expected=NullJsonException.class)
    public void doubleValTest() {
        JsonNull.NULL.doubleVal();
    }

    @Test(expected=NullJsonException.class)
    public void entriesTest() {
        JsonNull.NULL.entries();
    }

    @Test(expected=NullJsonException.class)
    public void getTest1() {
        JsonNull.NULL.get(0);
    }

    @Test(expected=NullJsonException.class)
    public void getTest2() {
        JsonNull.NULL.get("hello");
    }

    @Test
    public void getBooleanValueTest() {
        assertThat(JsonNull.NULL.getBooleanValue(), nullValue());
    }

    @Test
    public void getFlavorTest() {
        assertThat(JsonNull.NULL.getFlavor(), is(ScalarJsonValue.Flavor.NULL));
    }

    @Test
    public void getNumberValueTest() {
        assertThat(JsonNull.NULL.getNumberValue(), nullValue());
    }

    @Test
    public void getStringValueTest() {
        assertThat(JsonNull.NULL.getStringValue(), nullValue());
    }

    @Test
    public void getTypeTest() {
        assertThat(JsonNull.NULL.getType(), is(JsonValue.Type.NULL));
    }

    @Test
    public void getValueTest() {
        assertThat(JsonNull.NULL.getValue(), nullValue());
    }

    @Test
    public void isNullTest() {
        assertThat(JsonNull.NULL.isNull(), is(true));
    }

    @Test(expected=NullJsonException.class)
    public void iteratorTest() {
        JsonNull.NULL.iterator();
    }

    @Test(expected=NullJsonException.class)
    public void keysTest() {
        JsonNull.NULL.keys();
    }

    @Test(expected=NullJsonException.class)
    public void longValTest() {
        JsonNull.NULL.longVal();
    }

    @Test(expected=NullJsonException.class)
    public void removeTest1() {
        JsonNull.NULL.remove(0);
    }

    @Test(expected=NullJsonException.class)
    public void removeTest2() {
        JsonNull.NULL.remove("hello");
    }

    @Test(expected=NullJsonException.class)
    public void setTest1() {
        JsonNull.NULL.set(0, Json.TRUE);
    }

    @Test(expected=NullJsonException.class)
    public void setTest2() {
        JsonNull.NULL.set("hello", Json.TRUE);
    }

    @Test(expected=NullJsonException.class)
    public void sizeTest() {
        JsonNull.NULL.size();
    }

    @Test(expected=NullJsonException.class)
    public void stringValTest() {
        JsonNull.NULL.stringVal();
    }

    @Test(expected=NullJsonException.class)
    public void valuesTest() {
        JsonNull.NULL.values();
    }
}
