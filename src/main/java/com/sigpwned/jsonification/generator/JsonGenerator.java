package com.sigpwned.jsonification.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.sigpwned.jsonification.JsonError;
import com.sigpwned.jsonification.exception.GenerateJsonException;

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
public class JsonGenerator implements AutoCloseable {
    private static class Scope {
        public static enum Type {
            ROOT, OBJECT, ARRAY;
        }
        
        public final Scope.Type type;
        public int count;
        
        public Scope(Scope.Type type) {
            this.type = type;
            this.count = 0;
        }
        
        public String toString() {
            return type.name();
        }
    }
    
    private final Writer writer;
    private final char[] cpbuf;
    private final List<Scope> scopes;
    
    public JsonGenerator(Writer writer) {
        this.writer = writer;
        this.cpbuf = new char[2];
        this.scopes = new ArrayList<>();
        this.scopes.add(new Scope(Scope.Type.ROOT));
    }
    
    public void openObject() throws IOException {
        openObject(null);
    }
    
    public void openObject(String name) throws IOException {
        Scope top=scope();
        if(top.type!=Scope.Type.ROOT && top.count!=0)
            writer.write(",");
        name(name);
        writer.write("{");
        scopes.add(new Scope(Scope.Type.OBJECT));
    }
    
    public void closeObject() throws IOException {
        Scope top=scopes.remove(scopes.size()-1);
        if(top.type != Scope.Type.OBJECT)
            throw new GenerateJsonException("cannot close object in "+top+" scope");
        writer.write("}");
        scope().count += 1;
    }
    
    public void openArray() throws IOException {
        openArray(null);
    }
    
    public void openArray(String name) throws IOException {
        Scope top=scope();
        if(top.type!=Scope.Type.ROOT && top.count!=0)
            writer.write(",");
        name(name);
        writer.write("[");
        scopes.add(new Scope(Scope.Type.ARRAY));
    }
    
    public void closeArray() throws IOException {
        Scope top=scopes.remove(scopes.size()-1);
        if(top.type != Scope.Type.ARRAY)
            throw new GenerateJsonException("cannot close object in "+top+" scope");
        writer.write("]");
        scope().count += 1;
    }
    
    public void scalar(long value) throws IOException {
        scalar(null, value);
    }
    
    public void scalar(String name, long value) throws IOException {
        Scope top=scope();
        if(top.type!=Scope.Type.ROOT && top.count!=0)
            writer.write(",");
        name(name);
        writer.write(Long.toString(value));
        top.count = top.count+1;
    }
    
    public void scalar(double value) throws IOException {
        scalar(null, value);
    }
    
    public void scalar(String name, double value) throws IOException {
        Scope top=scope();
        if(top.type!=Scope.Type.ROOT && top.count!=0)
            writer.write(",");
        name(name);
        writer.write(Double.toString(value));
        top.count = top.count+1;
    }
    
    public void scalar(String value) throws IOException {
        scalar(null, value);
    }
    
    public void scalar(String name, String value) throws IOException {
        Scope top=scope();
        if(top.type!=Scope.Type.ROOT && top.count!=0)
            writer.write(",");
        name(name);
        writer.write(string(value));
        top.count = top.count+1;
    }
    
    public void scalar(boolean value) throws IOException {
        scalar(null, value);
    }
    
    public void scalar(String name, boolean value) throws IOException {
        Scope top=scope();
        if(top.type!=Scope.Type.ROOT && top.count!=0)
            writer.write(",");
        name(name);
        writer.write(Boolean.toString(value));
        top.count = top.count+1;
    }
    
    public void nil() throws IOException {
        nil(null);
    }
    
    public void nil(String name) throws IOException {
        Scope top=scope();
        if(top.type!=Scope.Type.ROOT && top.count!=0)
            writer.write(",");
        name(name);
        writer.write("null");
        top.count = top.count+1;
    }
    
    private void name(String name) throws IOException {
        switch(scope().type) {
        case ARRAY:
        case ROOT:
            if(name != null)
                throw new GenerateJsonException("cannot provide name at "+scope()+" scope: "+name);
            break;
        case OBJECT:
            if(name == null)
                throw new GenerateJsonException("must provide name at "+scope()+" scope");
            break;
        default:
            throw new JsonError("unrecognized scope: "+scope());
        }
        if(name != null) {
            writer.write(string(name));
            writer.write(":");
        }
    }
    
    private String string(String s) {
        StringBuilder result=new StringBuilder();
        
        result.append("\"");
        
        int index=0;
        while(index < s.length()) {
            int cp=s.codePointAt(index);
            if(Character.isLetter(cp) || Character.isDigit(cp))
                result.appendCodePoint(cp);
            else
            if(cp == ' ')
                result.append(" ");
            else
            if(cp == '"')
                result.append("\\\"");
            else
            if(cp == '\\')
                result.append("\\\\");
            else
            if(cp == '/')
                result.append("\\/");
            else
            if(cp == '\b')
                result.append("\\b");
            else
            if(cp == '\f')
                result.append("\\f");
            else
            if(cp == '\n')
                result.append("\\n");
            else
            if(cp == '\r')
                result.append("\\r");
            else
            if(cp == '\t')
                result.append("\\t");
            else {
                int chars=Character.toChars(cp, cpbuf, 0);
                for(int i=0;i<chars;i++)
                    if(printable(cpbuf[i]))
                        result.append(cpbuf[i]);
                    else
                        result.append(String.format("\\u%04X", (int) cpbuf[i]));
            }
            
            index = index+Character.charCount(cp);
        }
        
        result.append("\"");
        
        return result.toString();
    }
    
    private Writer getWriter() {
        return writer;
    }
    
    private Scope scope() {
        return scopes.get(scopes.size()-1);
    }
    
    public void close() throws IOException {
        getWriter().close();
    }
    
    private static boolean printable(char ch) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
        return !Character.isISOControl(ch) && ch!=0xFFFF && block!=null && block != Character.UnicodeBlock.SPECIALS;
    }
}
