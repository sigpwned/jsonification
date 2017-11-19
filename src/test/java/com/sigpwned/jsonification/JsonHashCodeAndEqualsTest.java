package com.sigpwned.jsonification;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonObject;

public class JsonHashCodeAndEqualsTest {
    /**
     * Two ints with the same value should match
     */
    @Test
    public void test1() {
        JsonValue a=Json.newValue(1L);
        JsonValue b=Json.newValue(1L);
        
        assertThat(a.hashCode(), is(b.hashCode()));
        assertThat(a, is(b));
    }

    /**
     * Two ints with different values should not match
     */
    @Test
    public void test2() {
        JsonValue a=Json.newValue(1L);
        JsonValue b=Json.newValue(2L);
        
        assertThat(a.hashCode(), not(is(b.hashCode())));
        assertThat(a, not(is(b)));
    }

    /**
     * Two strings with the same value should match
     */
    @Test
    public void test3() {
        JsonValue a=Json.newValue("alpha");
        JsonValue b=Json.newValue("alpha");
        
        assertThat(a.hashCode(), is(b.hashCode()));
        assertThat(a, is(b));
    }

    /**
     * Two strings with different values should not match
     */
    @Test
    public void test4() {
        JsonValue a=Json.newValue("alpha");
        JsonValue b=Json.newValue("bravo");
        
        assertThat(a.hashCode(), not(is(b.hashCode())));
        assertThat(a, not(is(b)));
    }

    /**
     * Two floats with the same value should match
     */
    @Test
    public void test5() {
        JsonValue a=Json.newValue(1.0);
        JsonValue b=Json.newValue(1.0);
        
        assertThat(a.hashCode(), is(b.hashCode()));
        assertThat(a, is(b));
    }

    /**
     * Two floats with different values should not match
     */
    @Test
    public void test6() {
        JsonValue a=Json.newValue(1.0);
        JsonValue b=Json.newValue(2.0);
        
        assertThat(a.hashCode(), not(is(b.hashCode())));
        assertThat(a, not(is(b)));
    }

    /**
     * Two booleans with the same value should match
     */
    @Test
    public void test7() {
        JsonValue a=Json.newValue(true);
        JsonValue b=Json.newValue(true);
        
        assertThat(a.hashCode(), is(b.hashCode()));
        assertThat(a, is(b));
    }

    /**
     * Two booleans with different values should not match
     */
    @Test
    public void test8() {
        JsonValue a=Json.newValue(true);
        JsonValue b=Json.newValue(false);
        
        assertThat(a.hashCode(), not(is(b.hashCode())));
        assertThat(a, not(is(b)));
    }

    /**
     * Two null values should match
     */
    @Test
    public void test9() {
        JsonValue a=Json.NULL;
        JsonValue b=Json.NULL;
        
        assertThat(a.hashCode(), is(b.hashCode()));
        assertThat(a, is(b));
    }

    /**
     * A null and non-null value should not match
     */
    @Test
    public void test10() {
        JsonValue a=Json.NULL;
        JsonValue b=Json.newValue(1);
        
        assertThat(a.hashCode(), not(is(b.hashCode())));
        assertThat(a, not(is(b)));
    }

    /**
     * Scalars of a different type should not match
     */
    @Test
    public void test11() {
        JsonValue a=Json.newValue("1");
        JsonValue b=Json.newValue(1);
        
        assertThat(a.hashCode(), not(is(b.hashCode())));
        assertThat(a, not(is(b)));
    }

    /**
     * Two numbers of the same value but different floatingness should be
     * equals, but not match hash code
     */
    @Test
    public void test12() {
        JsonValue a=Json.newValue(1);
        JsonValue b=Json.newValue(1.0);
        
        assertThat(a.hashCode()==b.hashCode(), is(Long.valueOf(1).hashCode()==Double.valueOf(1.0).hashCode()));
        assertThat(a.equals(b), is(Long.valueOf(1).equals(Double.valueOf(1.0))));
    }
    
    /**
     * Two equal arrays should match
     */
    @Test
    public void test13() {
        JsonArray a=Json.newArray().add(1).add(2);
        JsonArray b=Json.newArray().add(1).add(2);
        
        assertThat(a.hashCode(), is(b.hashCode()));
        assertThat(a, is(b));
    }
    
    /**
     * Two different arrays should match
     */
    @Test
    public void test14() {
        JsonArray a=Json.newArray().add(1).add(2);
        JsonArray b=Json.newArray().add(1).add(2).add(3);
        
        assertThat(a.hashCode(), not(is(b.hashCode())));
        assertThat(a, not(is(b)));
    }
    
    /**
     * Two equal objects in the same order should match
     */
    @Test
    public void test15() {
        JsonObject a=Json.newObject().set("alpha", 1).set("bravo", 2);
        JsonObject b=Json.newObject().set("alpha", 1).set("bravo", 2);
        
        assertThat(a.hashCode(), is(b.hashCode()));
        assertThat(a, is(b));
    }
    
    /**
     * Two equal objects in different order should match
     */
    @Test
    public void test16() {
        JsonObject a=Json.newObject().set("alpha", 1).set("bravo", 2);
        JsonObject b=Json.newObject().set("bravo", 2).set("alpha", 1);
        
        assertThat(a.hashCode(), is(b.hashCode()));
        assertThat(a, is(b));
    }
    
    /**
     * Two objects with different values should not match
     */
    @Test
    public void test17() {
        JsonObject a=Json.newObject().set("alpha", 1).set("bravo", 2);
        JsonObject b=Json.newObject().set("alpha", 1).set("bravo", 3);
        
        assertThat(a.hashCode(), not(is(b.hashCode())));
        assertThat(a, not(is(b)));
    }
    
    /**
     * Two objects with different keys should not match
     */
    @Test
    public void test18() {
        JsonObject a=Json.newObject().set("alpha", 1).set("bravo",   2);
        JsonObject b=Json.newObject().set("alpha", 1).set("charlie", 2);
        
        assertThat(a.hashCode(), not(is(b.hashCode())));
        assertThat(a, not(is(b)));
    }
}
