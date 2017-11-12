package com.sigpwned.jsonification.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sigpwned.jsonification.JsonParser;
import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.exception.ParseJsonException;
import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonObject;
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
public class DefaultJsonValueParser implements AutoCloseable, JsonParser {
    private static abstract class Scope {
        public static enum Type {
            ROOT, OBJECT, ARRAY;
        }
        
        private final Scope.Type type;
        private final JsonValue value;
        
        public Scope(Scope.Type type, JsonValue value) {
            this.type = type;
            this.value = value;
        }
        
        public String toString() {
            return type.name();
        }
        
        public Scope.Type getType() {
            return type;
        }
        
        public JsonValue getValue() {
            return value;
        }
    }
    
    private static class RootScope extends Scope {
        public RootScope(ScalarJsonValue value) {
            super(Scope.Type.ROOT, value);
        }
        
        public ScalarJsonValue getValue() {
            return super.getValue().asScalar();
        }
    }
    
    private static class ObjectScope extends Scope {
        private Iterator<JsonObject.Entry> iterator;
        
        public ObjectScope(JsonObject value) {
            super(Scope.Type.OBJECT, value);
        }
        
        public JsonObject getValue() {
            return super.getValue().asObject();
        }
        
        public Iterator<JsonObject.Entry> getIterator() {
            return iterator;
        }
        
        public void setIterator(Iterator<JsonObject.Entry> iterator) {
            this.iterator = iterator;
        }
    }
    
    private static class ArrayScope extends Scope {
        private Iterator<JsonValue> iterator;
        
        public ArrayScope(JsonArray value) {
            super(Scope.Type.ARRAY, value);
        }
        
        public JsonArray getValue() {
            return super.getValue().asArray();
        }
        
        public Iterator<JsonValue> getIterator() {
            return iterator;
        }
        
        public void setIterator(Iterator<JsonValue> iterator) {
            this.iterator = iterator;
        }
    }
    
    private final List<Scope> scopes;
    
    public DefaultJsonValueParser(JsonValue value) {
        if(value == null)
            throw new NullPointerException();
        
        Scope scope;
        switch(value.getType()) {
        case ARRAY:
            scope = new ArrayScope(value.asArray());
            break;
        case NULL:
        case SCALAR:
            scope = new RootScope(value.asScalar());
            break;
        case OBJECT:
            scope = new ObjectScope(value.asObject());
            break;
        default:
            throw new RuntimeException("unrecognized value: "+value);
        }
        
        this.scopes = new ArrayList<>();
        this.scopes.add(scope);
    }
    
    /**
     * Handle JSON events until one complete JSON value has been parsed. A
     * JSON value is one complete scalar, object, array, or nil. If the given
     * input contains more than one complete JSON value, only the first is
     * parsed.
     */
    @Override
    public boolean parse(final JsonParser.Handler delegate) throws IOException {
        final boolean[] completed=new boolean[1];
        final int[] depth=new int[1];
        final int[] count=new int[1];
        final JsonParser.Handler handler=new JsonParser.Handler() {
            @Override
            public void scalar(String name, String value) {
                delegate.scalar(name, value);
                completed[0] = true;
                count[0]++;
            }
            
            @Override
            public void scalar(String name, boolean value) {
                delegate.scalar(name, value);
                completed[0] = true;
                count[0]++;
            }
            
            @Override
            public void scalar(String name, double value) {
                delegate.scalar(name, value);
                completed[0] = true;
                count[0]++;
            }
            
            @Override
            public void scalar(String name, long value) {
                delegate.scalar(name, value);
                completed[0] = true;
                count[0]++;
            }
            
            @Override
            public void openObject(String name) {
                delegate.openObject(name);
                depth[0] = depth[0]+1;
                count[0]++;
            }
            
            @Override
            public void openArray(String name) {
                delegate.openArray(name);
                depth[0] = depth[0]+1;
                count[0]++;
            }
            
            @Override
            public void nil(String name) {
                delegate.nil(name);
                completed[0] = true;
                count[0]++;
            }
            
            @Override
            public void closeObject() {
                depth[0] = depth[0]-1;
                delegate.closeObject();
                completed[0] = true;
                count[0]++;
            }
            
            @Override
            public void closeArray() {
                depth[0] = depth[0]-1;
                delegate.closeArray();
                completed[0] = true;
                count[0]++;
            }
        };
        
        boolean eof=false;
        do {
            int oldcount=count[0];
            completed[0] = false;
            next(handler);
            if(count[0] == oldcount)
                eof = true;
        } while(eof==false && (completed[0]==false || depth[0]!=0));
        
        if(completed[0]==false && count[0]!=0)
            throw new ParseJsonException("Unexpect EOF in value");
        
        return completed[0];
    }

    @Override
    public void next(final JsonParser.Handler handler) throws IOException {
        Scope scope=scopes.size()!=0 ? scopes.get(scopes.size()-1) : null;
        
        if(scope != null) {
            switch(scope.getType()) {
            case ARRAY:
            {
                ArrayScope array=(ArrayScope) scope;
                if(array.getIterator() == null) {
                    handler.openArray(null);
                    array.setIterator(array.getValue().iterator());
                } else
                if(array.getIterator().hasNext()) {
                    JsonValue value=array.getIterator().next();
                    switch(value.getType()) {
                    case ARRAY:
                    {
                        ArrayScope child=new ArrayScope(value.asArray());
                        child.setIterator(child.getValue().iterator());
                        scopes.add(child);
                        handler.openArray(null);
                    } break;
                    case NULL:
                        handler.nil(null);
                        break;
                    case OBJECT:
                    {
                        ObjectScope child=new ObjectScope(value.asObject());
                        child.setIterator(child.getValue().entries().iterator());
                        scopes.add(child);
                        handler.openObject(null);
                    } break;
                    case SCALAR:
                    {
                        ScalarJsonValue scalar=value.asScalar();
                        switch(scalar.getFlavor()) {
                        case BOOLEAN:
                            handler.scalar(null, scalar.asBoolean().booleanVal());
                            break;
                        case NUMBER:
                            if(scalar.getValue() instanceof Double || scalar.getValue() instanceof Float)
                                handler.scalar(null, scalar.asNumber().doubleVal());
                            else
                                handler.scalar(null, scalar.asNumber().longVal());
                            break;
                        case STRING:
                            handler.scalar(null, scalar.asString().stringVal());
                            break;
                        case NULL:
                            handler.nil(null);
                            break;
                        default:
                            throw new IllegalArgumentException("unrecognized scalar type: "+scalar.getFlavor());
                        }                        
                    } break;
                    default:
                        throw new IllegalArgumentException("unrecognized type: "+value.getType());
                    }
                }
                else {
                    handler.closeArray();
                    scopes.remove(scopes.size()-1);
                }
            } break;
            case OBJECT:
            {
                ObjectScope object=(ObjectScope) scope;
                if(object.getIterator() == null) {
                    handler.openObject(null);
                    object.setIterator(object.getValue().entries().iterator());
                } else
                if(object.getIterator().hasNext()) {
                    JsonObject.Entry entry=object.getIterator().next();
                    String name=entry.getName();
                    JsonValue value=entry.getValue();
                    switch(value.getType()) {
                    case ARRAY:
                    {
                        ArrayScope child=new ArrayScope(value.asArray());
                        child.setIterator(child.getValue().iterator());
                        scopes.add(child);
                        handler.openArray(name);
                    } break;
                    case NULL:
                        handler.nil(name);
                        break;
                    case OBJECT:
                    {
                        ObjectScope child=new ObjectScope(value.asObject());
                        child.setIterator(child.getValue().entries().iterator());
                        scopes.add(child);
                        handler.openObject(name);
                    } break;
                    case SCALAR:
                    {
                        ScalarJsonValue scalar=value.asScalar();
                        switch(scalar.getFlavor()) {
                        case BOOLEAN:
                            handler.scalar(name, scalar.asBoolean().booleanVal());
                            break;
                        case NUMBER:
                            if(scalar.getValue() instanceof Double || scalar.getValue() instanceof Float)
                                handler.scalar(name, scalar.asNumber().doubleVal());
                            else
                                handler.scalar(name, scalar.asNumber().longVal());
                            break;
                        case STRING:
                            handler.scalar(name, scalar.asString().stringVal());
                            break;
                        case NULL:
                            handler.nil(name);
                            break;
                        default:
                            throw new IllegalArgumentException("unrecognized scalar type: "+scalar.getFlavor());
                        }                        
                    } break;
                    default:
                        throw new IllegalArgumentException("unrecognized type: "+value.getType());
                    }
                }
                else {
                    handler.closeObject();
                    scopes.remove(scopes.size()-1);
                }
            } break;
            case ROOT:
            {
                RootScope root=(RootScope) scope;
                switch(root.getValue().getFlavor()) {
                case BOOLEAN:
                    handler.scalar(null, root.getValue().asBoolean().booleanVal());
                    break;
                case NUMBER:
                    if(root.getValue().getValue() instanceof Double || root.getValue().getValue() instanceof Float)
                        handler.scalar(null, root.getValue().asNumber().doubleVal());
                    else
                        handler.scalar(null, root.getValue().asNumber().longVal());
                    break;
                case STRING:
                    handler.scalar(null, root.getValue().asString().stringVal());
                    break;
                case NULL:
                    handler.nil(null);
                    break;
                default:
                    throw new IllegalArgumentException("unrecognized scalar type: "+root.getValue().getFlavor());
                }
                scopes.remove(scopes.size()-1);
            } break;
            default:
                throw new IllegalArgumentException("unrecognized scope type: "+scope.getType());
            }
        }
        else {
            // No event
        }
    }
    
    @Override
    public void close() throws IOException {
        // Meh
    }
}
