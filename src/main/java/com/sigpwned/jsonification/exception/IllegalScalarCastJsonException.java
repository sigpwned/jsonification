package com.sigpwned.jsonification.exception;

import com.sigpwned.jsonification.JsonException;
import com.sigpwned.jsonification.value.ScalarJsonValue;

public class IllegalScalarCastJsonException extends JsonException {
    private static final long serialVersionUID = 659629898743825806L;
    
    private final ScalarJsonValue.Flavor objectFlavor;
    private final ScalarJsonValue.Flavor castedFlavor;
    
    public IllegalScalarCastJsonException(ScalarJsonValue.Flavor objectFlavor, ScalarJsonValue.Flavor castedFlavor) {
        super("Attempted illegal cast from "+objectFlavor+" to "+castedFlavor);
        this.objectFlavor = objectFlavor;
        this.castedFlavor = castedFlavor;
    }

    public ScalarJsonValue.Flavor getObjectFlavor() {
        return objectFlavor;
    }

    public ScalarJsonValue.Flavor getCastedFlavor() {
        return castedFlavor;
    }
}
