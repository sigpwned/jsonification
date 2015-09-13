package com.sigpwned.jsonification.value.scalar;

import com.sigpwned.jsonification.value.ScalarJsonValue;

public interface JsonNumber extends ScalarJsonValue {
    public Number getNumberValue();
    
    public long longVal();
    
    public double doubleVal();
}
