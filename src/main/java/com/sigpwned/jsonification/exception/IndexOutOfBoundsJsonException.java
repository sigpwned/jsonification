package com.sigpwned.jsonification.exception;

import com.sigpwned.jsonification.JsonException;
import com.sigpwned.jsonification.value.JsonArray;

public class IndexOutOfBoundsJsonException extends JsonException {
    private static final long serialVersionUID = 3152715531476558934L;
    
    private final JsonArray array;
    private final int index;

    public IndexOutOfBoundsJsonException(JsonArray array, int index) {
        super("Index out of bounds: "+index);
        this.array = array;
        this.index = index;
    }
    
    public JsonArray getArray() {
        return array;
    }

    public int getIndex() {
        return index;
    }
}
