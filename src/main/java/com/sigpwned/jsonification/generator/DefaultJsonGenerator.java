package com.sigpwned.jsonification.generator;

import java.io.IOException;
import java.io.Writer;

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
public class DefaultJsonGenerator extends AbstractJsonGenerator {
    private final Writer writer;
    private final char[] cpbuf;
    
    public DefaultJsonGenerator(Writer writer) {
        this.writer = writer;
        this.cpbuf = new char[2];
    }
    
    @Override
    protected void doOpenObject(Scope scope, String name) throws IOException {
        prolog(scope, name);
        getWriter().write("{");
    }

    @Override
    protected void doCloseObject(Scope scope) throws IOException {
        getWriter().write("}");
    }

    @Override
    protected void doOpenArray(Scope scope, String name) throws IOException {
        prolog(scope, name);
        getWriter().write("[");
    }

    @Override
    protected void doCloseArray(Scope scope) throws IOException {
        getWriter().write("]");
    }

    @Override
    protected void doValue(Scope scope, String name, String value) throws IOException {
        prolog(scope, name);
        getWriter().write(string(value));
    }

    @Override
    protected void doValue(Scope scope, String name, long value) throws IOException {
        prolog(scope, name);
        getWriter().write(Long.toString(value));
    }

    @Override
    protected void doValue(Scope scope, String name, double value) throws IOException {
        prolog(scope, name);
        getWriter().write(Double.toString(value));
    }

    @Override
    protected void doValue(Scope scope, String name, boolean value) throws IOException {
        prolog(scope, name);
        getWriter().write(Boolean.toString(value));
    }

    @Override
    protected void doNil(Scope scope, String name) throws IOException {
        prolog(scope, name);
        getWriter().write("null");
    }
    
    private void prolog(Scope scope, String name) throws IOException {
        if(scope.count != 0)
            getWriter().write(",");
        if(name != null) {
            getWriter().write(string(name));
            getWriter().write(":");
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
    
    @Override
    public void close() throws IOException {
        getWriter().close();
    }
}
