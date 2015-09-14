package com.sigpwned.jsonification.parser;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.sigpwned.jsonification.exception.ParseJsonException;

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
public class JsonParser implements AutoCloseable {
    public static interface Handler {
        public void openObject(String name);
        
        public void closeObject();
        
        public void openArray(String name);
        
        public void closeArray();
        
        public void nil(String name);
        
        public void scalar(String name, long value);
        
        public void scalar(String name, double value);
        
        public void scalar(String name, boolean value);
        
        public void scalar(String name, String value);
        
        public void eof();
    }
    
    private static class Token {
        public static enum Type {
            OPEN_OBJECT, CLOSE_OBJECT,
            OPEN_ARRAY, CLOSE_ARRAY,
            SYMBOL, COLON, COMMA, LONG, DOUBLE,
            STRING, TRUE, FALSE, NULL, EOF;
        }
        
        public final Token.Type type;
        public final String text;

        public Token(Token.Type type, String text) {
            this.type = type;
            this.text = text;
        }
    }
    
    
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
    
    private final PushbackReader reader;
    private final char[] cpbuf;
    private final List<Scope> scopes;
    private Token peek;
    
    public JsonParser(String text) {
        this(new StringReader(text));
    }
    
    public JsonParser(Reader reader) {
        this(new PushbackReader(reader, 2));
    }
    
    public JsonParser(PushbackReader reader) {
        this.reader = reader;
        this.cpbuf = new char[2];
        this.scopes = new ArrayList<>();
        this.scopes.add(new Scope(Scope.Type.ROOT));
    }
    
    public void parse(final JsonParser.Handler delegate) throws IOException {
        final boolean[] eof=new boolean[1];
        final JsonParser.Handler handler=new JsonParser.Handler() {
            @Override
            public void scalar(String name, String value) {
                delegate.scalar(name, value);
            }
            
            @Override
            public void scalar(String name, boolean value) {
                delegate.scalar(name, value);
            }
            
            @Override
            public void scalar(String name, double value) {
                delegate.scalar(name, value);
            }
            
            @Override
            public void scalar(String name, long value) {
                delegate.scalar(name, value);
            }
            
            @Override
            public void openObject(String name) {
                delegate.openObject(name);
            }
            
            @Override
            public void openArray(String name) {
                delegate.openArray(name);
            }
            
            @Override
            public void nil(String name) {
                delegate.nil(name);
            }
            
            @Override
            public void eof() {
                eof[0] = true;
                delegate.eof();
            }
            
            @Override
            public void closeObject() {
                delegate.closeObject();
            }
            
            @Override
            public void closeArray() {
                delegate.closeArray();
            }
        };
        do {
            next(handler);
        } while(eof[0] == false);
    }

    public void next(final JsonParser.Handler handler) throws IOException {
        Scope scope=scopes.get(scopes.size()-1);
        
        switch(scope.type) {
        case ARRAY:
        {
            Token token=read();
            if(token.type == Token.Type.EOF)
                throw new ParseJsonException("Unexpected EOF in array");
            else
            if(token.type == Token.Type.CLOSE_ARRAY) {
                handler.closeArray();
                scopes.remove(scopes.size()-1);
            }
            else {
                if(token.type == Token.Type.COMMA) {
                    if(scope.count != 0)
                        token = read();
                    else
                        throw new ParseJsonException("Unexpected token in array: "+token.type);
                }
                
                if(token.type == Token.Type.EOF)
                    throw new ParseJsonException("Unexpected EOF in array");
                else
                if(isValue(token))
                    value(handler, null, token);
                else
                if(token.type == Token.Type.OPEN_OBJECT) {
                    handler.openObject(null);
                    scopes.add(new Scope(Scope.Type.OBJECT));
                } else
                if(token.type == Token.Type.OPEN_ARRAY) {
                    handler.openArray(null);
                    scopes.add(new Scope(Scope.Type.ARRAY));
                }
                else
                    throw new ParseJsonException("Unexpected token in array: "+token.type);
                
                scope.count = scope.count+1;
            }
        } break;
        case OBJECT:
        {
            Token token=read();
            if(token.type == Token.Type.EOF)
                throw new ParseJsonException("Unexpected EOF in object");
            else
            if(token.type == Token.Type.CLOSE_OBJECT) {
                handler.closeObject();
                scopes.remove(scopes.size()-1);
            }
            else {
                if(token.type == Token.Type.COMMA) {
                    if(scope.count != 0)
                        token = read();
                    else
                        throw new ParseJsonException("Unexpected token in object: "+token.type);
                }
                
                String name;
                if(token.type == Token.Type.EOF)
                    throw new ParseJsonException("Unexpected EOF in object");
                else
                if(token.type==Token.Type.STRING || token.type==Token.Type.SYMBOL)
                    name = token.text;
                else
                    throw new ParseJsonException("Unexpected token in object: "+token.type);
                
                token = read();
                if(token.type == Token.Type.EOF)
                    throw new ParseJsonException("Unexpected EOF in object");
                else
                if(token.type != Token.Type.COLON)
                    throw new ParseJsonException("Unexpected token in object: "+token.type);
                
                token = read();
                if(token.type == Token.Type.EOF)
                    throw new ParseJsonException("Unexpected EOF in object");
                else
                if(isValue(token))
                    value(handler, name, token);
                else
                if(token.type == Token.Type.OPEN_OBJECT) {
                    handler.openObject(name);
                    scopes.add(new Scope(Scope.Type.OBJECT));
                } else
                if(token.type == Token.Type.OPEN_ARRAY) {
                    handler.openArray(name);
                    scopes.add(new Scope(Scope.Type.ARRAY));
                }
                else
                    throw new ParseJsonException("Unexpected token in array: "+token.type);
                
                scope.count = scope.count+1;
            }
        } break;
        case ROOT:
        {
            Token token=read();
            if(token.type == Token.Type.EOF)
                handler.eof();
            else
            if(isValue(token)) {
                value(handler, null, token);
                scope.count = scope.count+1;
            } else
            if(token.type == Token.Type.OPEN_OBJECT) {
                scopes.add(new Scope(Scope.Type.OBJECT));
                handler.openObject(null);
                scope.count = scope.count+1;
            } else
            if(token.type == Token.Type.OPEN_ARRAY) {
                scopes.add(new Scope(Scope.Type.ARRAY));
                handler.openArray(null);
                scope.count = scope.count+1;
            }
            else
                throw new ParseJsonException("Unexpected token at root scope: "+token.type);
        } break;
        default:
            throw new RuntimeException("unrecognized scope type: "+scope.type);
        }
    }
    
    private void value(JsonParser.Handler handler, String name, Token t) {
        switch(t.type) {
        case CLOSE_ARRAY:
        case CLOSE_OBJECT:
        case COLON:
        case COMMA:
        case OPEN_ARRAY:
        case OPEN_OBJECT:
        case SYMBOL:
        case EOF:
            throw new IllegalArgumentException("not a value: "+t.type);
        case DOUBLE:
        {
            final double value=Double.parseDouble(t.text);
            handler.scalar(name, value);
        } break;
        case FALSE:
            handler.scalar(name, false);
            break;
        case LONG:
        {
            final long value=Long.parseLong(t.text);
            handler.scalar(name, value);
        } break;
        case NULL:
            handler.nil(name);
            break;
        case STRING:
        {
            final String value=t.text;
            handler.scalar(name, value);
        } break;
        case TRUE:
            handler.scalar(name, true);
            break;
        default:
            throw new IllegalArgumentException("unrecognized type: "+t.type);
        }
    }
    
    private boolean isValue(Token t) {
        boolean result;
        
        switch(t.type) {
        case CLOSE_ARRAY:
        case CLOSE_OBJECT:
        case COLON:
        case COMMA:
        case OPEN_ARRAY:
        case OPEN_OBJECT:
        case SYMBOL:
        case EOF:
            result = false;
            break;
        case DOUBLE:
        case FALSE:
        case LONG:
        case NULL:
        case STRING:
        case TRUE:
            result = true;
            break;
        default:
            throw new IllegalArgumentException("unrecognized type: "+t.type);
        }
        
        return result;
    }
    
    private Token read() throws IOException {
        Token result=peek();
        peek = null;
        return result;
    }
    
    private Token peek() throws IOException {
        if(peek == null) {
            int cp=getch();
            while(Character.isSpaceChar(cp))
                cp = getch();
            
            if(cp == -1)
                peek = new Token(Token.Type.EOF, "$");
            else
            if(cp == '{')
                peek = new Token(Token.Type.OPEN_OBJECT, "{");
            else
            if(cp == '}')
                peek = new Token(Token.Type.CLOSE_OBJECT, "}");
            else
            if(cp == '[')
                peek = new Token(Token.Type.OPEN_ARRAY, "[");
            else
            if(cp == ']')
                peek = new Token(Token.Type.CLOSE_ARRAY, "]");
            else
            if(cp == ':')
                peek = new Token(Token.Type.COLON, ":");
            else
            if(cp == ',')
                peek = new Token(Token.Type.COMMA, ",");
            else
            if(cp == 't')
                peek = keywordOrSymbol(Token.Type.TRUE, cp, "rue");
            else
            if(cp == 'f')
                peek = keywordOrSymbol(Token.Type.FALSE, cp, "alse");
            else
            if(cp == 'n')
                peek = keywordOrSymbol(Token.Type.NULL, cp, "ull");
            else
            if(cp == '\"') {
                StringBuilder buf=new StringBuilder();
                for(cp=getch();cp!='"';cp=getch()) {
                    if(cp == -1)
                        throw new ParseJsonException("Unexpected EOF in string constant");
                    else
                    if(cp == '\\') {
                        int cp2=getch();
                        if(cp2 == -1)
                            throw new ParseJsonException("Unexpected EOF in escape sequence in string constant");
                        else
                        if(cp2 == '"')
                            buf.append('"');
                        else
                        if(cp2 == '\\')
                            buf.append('\\');
                        else
                        if(cp2 == '/')
                            buf.append('/');
                        else
                        if(cp2 == 'b')
                            buf.append('\b');
                        else
                        if(cp2 == 'f')
                            buf.append('\f');
                        else
                        if(cp2 == 'n')
                            buf.append('\n');
                        else
                        if(cp2 == 'r')
                            buf.append('\r');
                        else
                        if(cp2 == 't')
                            buf.append('\t');
                        else
                        if(cp2 == 'u') {
                            StringBuilder ubuf=new StringBuilder();
                            for(int i=0;i<4;i++) {
                                int u=getch();
                                if(u>='0' && u<='9')
                                    ubuf.append((char) u);
                                else
                                if(u>='a' && u<='f')
                                    ubuf.append((char) u);
                                else
                                if(u>='A' && u<='F')
                                    ubuf.append((char) u);
                                else
                                    throw new ParseJsonException("Invalid character in unicode escape sequence in string constant: \\u"+ubuf.toString()+new String(Character.toChars(u)));
                            }
                            int uval=Integer.parseInt(ubuf.toString(), 16);
                            buf.append((char)(uval & 0xFFFF));
                        }
                        else
                            throw new ParseJsonException("Invalid escape sequence in string constant: \\"+new String(Character.toChars(cp2)));
                    }
                    else
                        buf.appendCodePoint(cp);
                }
                peek = new Token(Token.Type.STRING, buf.toString());
            } else
            if(Character.isDigit(cp) || cp=='-' || cp=='.') {
                StringBuilder buf=new StringBuilder();
                
                if(cp == '-') {
                    buf.appendCodePoint(cp);
                    cp = getch();
                    if(cp == -1)
                        throw new ParseJsonException("Unexpected EOF in numeric constant");
                }
                
                if(cp == '0') {
                    buf.appendCodePoint(cp);
                    cp = getch();
                } else
                if(cp>='1' && cp<='9') {
                    while(cp>='0' && cp<='9') {
                        buf.appendCodePoint(cp);
                        cp = getch();
                    }
                } else
                if(cp == '.') {
                    // This is fine. Just wait for numbers.
                }
                else
                    throw new ParseJsonException("Unexpected character in numeric constant: "+new String(Character.toChars(cp)));

                boolean decimal=false;
                
                if(cp == '.') {
                    decimal = true;
                    buf.appendCodePoint(cp);
                    cp = getch();
                    while(cp>='0' && cp<='9') {
                        buf.appendCodePoint(cp);
                        cp = getch();
                    }
                    if(buf.length() == 1)
                        throw new ParseJsonException("Expected digits around decimal point in numeric constant: "+buf.toString());
                }
                
                if(cp=='e' || cp=='E') {
                    decimal = true;
                    buf.appendCodePoint(cp);
                    cp = getch();
                    if(cp=='-' || cp=='+') {
                        buf.appendCodePoint(cp);
                        cp = getch();
                    }
                    while(cp>='0' && cp<='9') {
                        buf.appendCodePoint(cp);
                        cp = getch();
                    }
                    if(Character.toLowerCase(buf.charAt(buf.length()-1)) == 'e')
                        throw new ParseJsonException("Expected digits after scientific notation in numeric constant: "+buf.toString());
                }
                
                if(decimal)
                    peek = new Token(Token.Type.DOUBLE, buf.toString());
                else
                    peek = new Token(Token.Type.LONG, buf.toString());

                ungetch(cp);
            } else
            if(Character.isLetter(cp) || cp=='_' || cp=='$') {
                StringBuilder buf=new StringBuilder();
                buf.appendCodePoint(cp);
                
                cp = getch();
                while(Character.isLetter(cp) || Character.isDigit(cp) || cp=='_' || cp=='$') {
                    buf.appendCodePoint(cp);
                    cp = getch();
                }
                
                peek = new Token(Token.Type.SYMBOL, buf.toString());
                
                ungetch(cp);
            }
            else
                throw new ParseJsonException("Unrecognized character: "+new String(Character.toChars(cp)));
        }
        return peek;
    }
    
    private Token keywordOrSymbol(Token.Type type, int cp0, String s) throws IOException {
        StringBuilder buf=new StringBuilder();
        buf.appendCodePoint(cp0);

        int index=0;
        while(index < s.length()) {
            int cp=s.codePointAt(index);
            int ch=getch();
            if(cp == ch) {
                buf.appendCodePoint(cp);
                index = index+Character.charCount(cp);
            }
            else {
                while(Character.isLetter(ch) || Character.isDigit(ch) || ch=='$' || ch=='_') {
                    buf.appendCodePoint(ch);
                    ch = getch();
                }
                ungetch(ch);
                break;
            }
        }

        Token result;
        if(index >= s.length())
            result = new Token(type, buf.toString());
        else
            result = new Token(Token.Type.SYMBOL, buf.toString());
        
        return result;
    }
    
    private int getch() throws IOException {
        int result;
        
        int a=getReader().read();
        if(a != -1) {
            char ch1=(char)(a & 0xFFFF);
            if(Character.isHighSurrogate(ch1)) {
                int b=getReader().read();
                if(b != -1) {
                    char ch2=(char)(b & 0xFFFF);
                    if(Character.isLowSurrogate(ch2))
                        result = Character.toCodePoint(ch1, ch2);
                    else
                        result = ch1;
                }
                else
                    result = ch1;
            }
            else
                result = ch1;
        }
        else
            result = -1;
        
        return result;
    }
    
    private void ungetch(int ch) throws IOException {
        if(ch != -1) {
            int length=Character.toChars(ch, cpbuf, 0);
            if(length == 2) {
                getReader().unread(cpbuf[1]);
                getReader().unread(cpbuf[0]);
            } else
            if(length == 1)
                getReader().unread(cpbuf[0]);
            else
                throw new IllegalArgumentException("invalid codepoint length: "+length);
        }
    }

    private PushbackReader getReader() {
        return reader;
    }
    
    public void close() throws IOException {
        getReader().close();
    }
}
