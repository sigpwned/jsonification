package com.sigpwned.jsonification.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sigpwned.jsonification.Json;
import com.sigpwned.jsonification.JsonError;
import com.sigpwned.jsonification.JsonFactory;
import com.sigpwned.jsonification.JsonValue;
import com.sigpwned.jsonification.JsonTreeGenerator;
import com.sigpwned.jsonification.exception.GenerateJsonException;

public class DefaultJsonTreeGenerator extends AbstractJsonGenerator implements JsonTreeGenerator {
    private static class Container {
        public final JsonValue value;
        public String name;
        public Container(JsonValue value) {
            this.value = value;
        }
    }
    
    private final JsonFactory factory;
    private final List<Container> containers;
    private JsonValue value;
    
    public DefaultJsonTreeGenerator() {
        this(Json.getDefaultFactory());
    }

    public DefaultJsonTreeGenerator(JsonFactory factory) {
        this.factory = factory;
        this.containers = new ArrayList<>();
    }

    @Override
    protected void doOpenObject(Scope scope, String name) throws IOException {
        switch(scope.type) {
        case OBJECT:
            top().name = name;
            // Fall through...
        case ROOT:
        case ARRAY:
            push(new Container(getFactory().newObject()));
            break;
        default:
            throw new JsonError("unhandled scope: "+scope);
        }
    }

    @Override
    protected void doCloseObject(Scope scope) throws IOException {
        assign(pop().value.asObject());
    }

    @Override
    protected void doOpenArray(Scope scope, String name) throws IOException {
        switch(scope.type) {
        case OBJECT:
            top().name = name;
            // Fall through...
        case ROOT:
        case ARRAY:
            push(new Container(getFactory().newArray()));
            break;
        default:
            throw new JsonError("unhandled scope: "+scope);
        }
    }

    @Override
    protected void doCloseArray(Scope scope) throws IOException {
        assign(pop().value.asArray());
    }

    @Override
    protected void doValue(Scope scope, String name, String value) throws IOException {
        switch(scope.type) {
        case OBJECT:
            top().name = name;
            // Fall through...
        case ROOT:
        case ARRAY:
            assign(Json.newValue(value));
            break;
        default:
            throw new JsonError("unhandled scope: "+scope);
        }
    }

    @Override
    protected void doValue(Scope scope, String name, long value) throws IOException {
        switch(scope.type) {
        case OBJECT:
            top().name = name;
            // Fall through...
        case ROOT:
        case ARRAY:
            assign(Json.newValue(value));
            break;
        default:
            throw new JsonError("unhandled scope: "+scope);
        }
    }

    @Override
    protected void doValue(Scope scope, String name, double value) throws IOException {
        switch(scope.type) {
        case OBJECT:
            top().name = name;
            // Fall through...
        case ROOT:
        case ARRAY:
            assign(Json.newValue(value));
            break;
        default:
            throw new JsonError("unhandled scope: "+scope);
        }
    }

    @Override
    protected void doValue(Scope scope, String name, boolean value) throws IOException {
        switch(scope.type) {
        case OBJECT:
            top().name = name;
            // Fall through...
        case ROOT:
        case ARRAY:
            assign(Json.newValue(value));
            break;
        default:
            throw new JsonError("unhandled scope: "+scope);
        }
    }

    @Override
    protected void doNil(Scope scope, String name) throws IOException {
        switch(scope.type) {
        case OBJECT:
            top().name = name;
            // Fall through...
        case ROOT:
        case ARRAY:
            assign(Json.newNull());
            break;
        default:
            throw new JsonError("unhandled scope: "+scope);
        }
    }
    
    private void assign(JsonValue value) {
        if(empty())
            this.value = value;
        else {
            Container top=top();
            switch(top.value.getType()) {
            case ARRAY:
                top.value.asArray().add(value);
                break;
            case OBJECT:
                top.value.asObject().set(top.name, value);
                break;
            case NULL:
            case SCALAR:
                throw new JsonError("unexpected container type: "+top.value.getType());
            default:
                throw new JsonError("unhandled container type: "+top.value.getType());
            }
        }
    }
    
    private Container pop() {
        return getContainers().remove(getContainers().size()-1);
    }
    
    private void push(Container container) {
        getContainers().add(container);
    }

    private Container top() {
        return getContainers().get(getContainers().size()-1);
    }
    
    private boolean empty() {
        return getContainers().size() == 0;
    }
    
    private JsonFactory getFactory() {
        return factory;
    }

    @Override
    public JsonValue getValue() {
        if(value == null)
            throw new GenerateJsonException("value not set");
        JsonValue result=value;
        value = null;
        return result;
    }

    private List<Container> getContainers() {
        return containers;
    }

    @Override
    public void close() throws IOException {
        // Nothing to do!
    }
}
