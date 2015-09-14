package com.sigpwned.jsonification.value.scalar;

import com.sigpwned.jsonification.impl.DefaultJsonBoolean;
import com.sigpwned.jsonification.value.ScalarJsonValue;

public interface JsonBoolean extends ScalarJsonValue {
    public static final JsonBoolean TRUE=DefaultJsonBoolean.TRUE;

    public static final JsonBoolean FALSE=DefaultJsonBoolean.FALSE;
    
    public Boolean getBooleanValue();
    
    public boolean booleanVal();
}
