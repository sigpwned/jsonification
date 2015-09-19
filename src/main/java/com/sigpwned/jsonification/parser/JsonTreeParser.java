package com.sigpwned.jsonification.parser;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.sigpwned.jsonification.JsonError;
import com.sigpwned.jsonification.JsonEvent;
import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.JsonValueFactory;
import com.sigpwned.jsonification.exception.ParseJsonException;
import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonObject;

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
public class JsonTreeParser implements AutoCloseable {
    private final JsonEventParser parser;
    
    public JsonTreeParser(String text) {
        this(new JsonEventParser(text));
    }
    
    public JsonTreeParser(Reader reader) {
        this(new JsonEventParser(reader));
    }
    
    public JsonTreeParser(PushbackReader reader) {
        this(new JsonEventParser(reader));
    }
    
    public JsonTreeParser(JsonEventParser parser) {
        this.parser = parser;
    }
    
    private static class Scope {
        public final String name;
        public final JsonValue value;
     
        public Scope(String name, JsonValue value) {
            this.name = name;
            this.value = value;
        }
    }

    public JsonValueFactory getFactory() {
        return getParser().getFactory();
    }

    public void setFactory(JsonValueFactory factory) {
        getParser().setFactory(factory);
    }

    public JsonValue next() throws IOException {
        JsonValue result=null;
        
        JsonEvent event=getParser().next();
        if(event == null) {
            // We're done here.
            result = null;
        }
        else {
            List<Scope> scopes=new ArrayList<>();
            loop: for(JsonEvent e=event;e!=null;e=getParser().next())
                switch(e.getType()) {
                case OPEN_OBJECT:
                    scopes.add(new Scope(e.getName(), getFactory().newObject()));
                    break;
                case CLOSE_OBJECT:
                {
                    Scope scope=scopes.remove(scopes.size()-1);
                    if(scopes.size() != 0) {
                        Scope top=scopes.get(scopes.size()-1);
                        switch(top.value.getType()) {
                        case ARRAY:
                            top.value.asArray().add(scope.value);
                            break;
                        case OBJECT:
                            top.value.asObject().set(scope.name, scope.value);
                            break;
                        case NULL:
                            throw new JsonError("unexpected null scope");
                        case SCALAR:
                            throw new JsonError("unexpected scalar scope");
                        default:
                            throw new JsonError("unrecognized scope");
                        }
                    }
                    else {
                        result = scope.value;
                        break loop;
                    }
                } break;
                case OPEN_ARRAY:
                    scopes.add(new Scope(e.getName(), getFactory().newArray()));
                    break;
                case CLOSE_ARRAY:
                {
                    Scope scope=scopes.remove(scopes.size()-1);
                    if(scopes.size() != 0) {
                        Scope top=scopes.get(scopes.size()-1);
                        switch(top.value.getType()) {
                        case ARRAY:
                            top.value.asArray().add(scope.value);
                            break;
                        case OBJECT:
                            top.value.asObject().set(scope.name, scope.value);
                            break;
                        case NULL:
                            throw new JsonError("unexpected null scope");
                        case SCALAR:
                            throw new JsonError("unexpected scalar scope");
                        default:
                            throw new JsonError("unrecognized scope");
                        }
                    }
                    else {
                        result = scope.value;
                        break loop;
                    }
                } break;
                case NULL:
                case SCALAR:
                {
                    if(scopes.size() == 0) {
                        result = e.getValue();
                        break loop;
                    }
                    else {
                        Scope scope=scopes.get(scopes.size()-1);
                        switch(scope.value.getType()) {
                        case OBJECT:
                        {
                            JsonObject object=scope.value.asObject();
                            object.set(e.getName(), e.getValue());
                        } break;
                        case ARRAY:
                        {
                            JsonArray array=scope.value.asArray();
                            array.add(e.getValue());
                        } break;
                        case NULL:
                            throw new JsonError("unexpected null scope");
                        case SCALAR:
                            throw new JsonError("unexpected scalar scope");
                        default:
                            throw new JsonError("unrecognized scope");
                        }
                    }
                } break;
                default:
                    throw new JsonError("unrecognized event type: "+e.getType());
                }
            if(scopes.size() != 0)
                throw new ParseJsonException("Unexpected EOF in JSON value");
        }
        
        return result;
    }

    private JsonEventParser getParser() {
        return parser;
    }

    public void close() throws IOException {
        getParser().close();
    }
}
