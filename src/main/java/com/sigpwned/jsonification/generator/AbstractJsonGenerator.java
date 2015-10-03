package com.sigpwned.jsonification.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sigpwned.jsonification.JsonError;
import com.sigpwned.jsonification.JsonGenerator;
import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.JsonWalker;
import com.sigpwned.jsonification.exception.GenerateJsonException;
import com.sigpwned.jsonification.util.JsonWalkers;

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
public abstract class AbstractJsonGenerator implements AutoCloseable, JsonGenerator {
    protected static class Scope {
        public static enum Type {
            ROOT, OBJECT, ARRAY;
        }
        
        public final Scope.Type type;
        public int count;
        
        private Scope(Scope.Type type) {
            this.type = type;
            this.count = 0;
        }
        
        public String toString() {
            return type.name();
        }
    }

    private final List<Scope> scopes;
    private String nextName;
    
    public AbstractJsonGenerator() {
        this.scopes = new ArrayList<>();
        this.scopes.add(new Scope(Scope.Type.ROOT));
    }
    
    
    @Override
    public void openObject() throws IOException {
        openObject(null);
    }
    
    @Override
    public void openObject(String name) throws IOException {
        doOpenObject(scope(), name(name));
        scopes.add(new Scope(Scope.Type.OBJECT));
    }
    
    @Override
    public void closeObject() throws IOException {
        Scope top=scopes.remove(scopes.size()-1);
        if(top.type != Scope.Type.OBJECT)
            throw new GenerateJsonException("cannot close object in "+top+" scope");
        doCloseObject(top);
        scope().count += 1;
    }
    
    @Override
    public void openArray() throws IOException {
        openArray(null);
    }
    
    @Override
    public void openArray(String name) throws IOException {
        doOpenArray(scope(), name(name));
        scopes.add(new Scope(Scope.Type.ARRAY));
    }
    
    @Override
    public void closeArray() throws IOException {
        Scope top=scopes.remove(scopes.size()-1);
        if(top.type != Scope.Type.ARRAY)
            throw new GenerateJsonException("cannot close object in "+top+" scope");
        doCloseArray(top);
        scope().count += 1;
    }
    
    @Override
    public void scalar(Long value) throws IOException {
        scalar(null, value);
    }
    
    @Override
    public void scalar(String name, Long value) throws IOException {
        if(value != null)
            scalar(name, value.longValue());
        else
            nil(name);
    }
    
    @Override
    public void scalar(long value) throws IOException {
        scalar(null, value);
    }
    
    @Override
    public void scalar(String name, long value) throws IOException {
        Scope top=scope();
        doValue(top, name(name), value);
        top.count = top.count+1;
    }
    
    @Override
    public void scalar(Double value) throws IOException {
        scalar(null, value);
    }
    
    @Override
    public void scalar(String name, Double value) throws IOException {
        if(value != null)
            scalar(name, value.doubleValue());
        else
            nil(name);
    }
    
    @Override
    public void scalar(double value) throws IOException {
        scalar(null, value);
    }
    
    @Override
    public void scalar(String name, double value) throws IOException {
        Scope top=scope();
        doValue(top, name(name), value);
        top.count = top.count+1;
    }
    
    @Override
    public void scalar(String value) throws IOException {
        scalar(null, value);
    }
    
    @Override
    public void scalar(String name, String value) throws IOException {
        if(value != null) {
            Scope top=scope();
            doValue(top, name(name), value);
            top.count = top.count+1;
        }
        else
            nil(name);
    }
    
    @Override
    public void scalar(Boolean value) throws IOException {
        scalar(null, value);
    }
    
    @Override
    public void scalar(String name, Boolean value) throws IOException {
        if(value != null)
            scalar(name, value.booleanValue());
        else
            nil(name);
    }
    
    @Override
    public void scalar(boolean value) throws IOException {
        scalar(null, value);
    }
    
    @Override
    public void scalar(String name, boolean value) throws IOException {
        Scope top=scope();
        doValue(top, name(name), value);
        top.count = top.count+1;
    }
    
    @Override
    public void nil() throws IOException {
        nil(null);
    }
    
    @Override
    public void nil(String name) throws IOException {
        Scope top=scope();
        doNil(top, name(name));
        top.count = top.count+1;
    }
    
    private String name(String name) throws IOException {
        if(name!=null && nextName!=null && !name.equals(nextName))
            throw new GenerateJsonException("Cannot specify different name two different ways: "+name+" versus "+nextName);

        String result=null;
        if(nextName != null)
            result = nextName;
        if(name != null)
            result = name;
        
        switch(scope().type) {
        case ARRAY:
        case ROOT:
            if(result != null)
                throw new GenerateJsonException("cannot provide name at "+scope()+" scope: "+name);
            break;
        case OBJECT:
            if(result == null)
                throw new GenerateJsonException("must provide name at "+scope()+" scope");
            break;
        default:
            throw new JsonError("unrecognized scope: "+scope());
        }
        
        nextName = null;
        
        return result;
    }    
    
    @Override
    public void value(JsonValue value) throws IOException {
        value(null, value);
    }
    
    @Override
    public void value(String name, JsonValue value) throws IOException {
        if(name != null)
            setNextName(name);
        try {
            new JsonWalker(value).walk(JsonWalkers.newGeneratorHandler(this));
        }
        catch(GenerateJsonException e) {
            if(e.getCause()!=null && e.getCause() instanceof IOException)
                throw (IOException) e.getCause();
            else
                throw e;
        }
    }
    
    @Override
    public void setNextName(String nextName) {
        if(getNextName()!=null && nextName!=null && !getNextName().equals(nextName))
            throw new GenerateJsonException("Cannot reset nextName to different value: "+getNextName()+" to "+nextName);
        this.nextName = nextName;
    }

    @Override
    public String getNextName() {
        return nextName;
    }

    protected Scope scope() {
        return scopes.get(scopes.size()-1);
    }
    
    protected static boolean printable(char ch) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
        return !Character.isISOControl(ch) && ch!=0xFFFF && block!=null && block != Character.UnicodeBlock.SPECIALS;
    }
    
    protected abstract void doOpenObject(Scope scope, String name) throws IOException;
    
    protected abstract void doCloseObject(Scope scope) throws IOException;
    
    protected abstract void doOpenArray(Scope scope, String name) throws IOException;
    
    protected abstract void doCloseArray(Scope scope) throws IOException;
    
    protected abstract void doValue(Scope scope, String name, String value) throws IOException;
    
    protected abstract void doValue(Scope scope, String name, long value) throws IOException;
    
    protected abstract void doValue(Scope scope, String name, double value) throws IOException;
    
    protected abstract void doValue(Scope scope, String name, boolean value) throws IOException;
    
    protected abstract void doNil(Scope scope, String name) throws IOException;
}
