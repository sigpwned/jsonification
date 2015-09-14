package com.sigpwned.jsonification.parser;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.sigpwned.jsonification.JsonError;
import com.sigpwned.jsonification.JsonEvent;
import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.exception.ParseJsonException;
import com.sigpwned.jsonification.impl.DefaultJsonArray;
import com.sigpwned.jsonification.impl.DefaultJsonObject;
import com.sigpwned.jsonification.value.JsonArray;
import com.sigpwned.jsonification.value.JsonObject;

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
    
    public JsonValue next() throws IOException {
        JsonValue result=null;
        
        JsonEvent event=getParser().next();
        if(event.getType() == JsonEvent.Type.EOF) {
            // We're done here.
            result = null;
        }
        else {
            List<Scope> scopes=new ArrayList<>();
            loop: for(JsonEvent e=event;e.getType()!=JsonEvent.Type.EOF;e=getParser().next())
                switch(e.getType()) {
                case OPEN_OBJECT:
                    scopes.add(new Scope(e.getName(), new DefaultJsonObject()));
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
                    scopes.add(new Scope(e.getName(), new DefaultJsonArray()));
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
                case VALUE:
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
                case EOF:
                    throw new ParseJsonException("Unexpected EOF in value");
                default:
                    throw new JsonError("unrecognized event type: "+e.getType());
                }
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
