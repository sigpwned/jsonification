package com.sigpwned.jsonification.value.scalar;

import com.sigpwned.jsonification.impl.DefaultJsonBoolean;
import com.sigpwned.jsonification.value.ScalarJsonValue;

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
public interface JsonBoolean extends ScalarJsonValue {
    public static final JsonBoolean TRUE=DefaultJsonBoolean.TRUE;

    public static final JsonBoolean FALSE=DefaultJsonBoolean.FALSE;
    
    public Boolean getBooleanValue();
    
    public boolean booleanVal();
}
