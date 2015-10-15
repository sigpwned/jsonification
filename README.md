# jsonification
JSON is a simple data format. Working with JSON should be simple, too. Jsonification tries to make working with JSON in Java simple and natural, no matter what your task or program architecture is.

## Reading JSON

Jsonification offers three ways to parse JSON data. The underlying parser is the same in all cases, so they will all parse JSON in exactly the same standards-compliant way. The different approaches allow the user to use the code in different styles.

### Streaming Push

In this mode, Jsonification will "push" parse events to a handler class via method calls. This parser only holds in memory the data from the current event being processed, so it's an excellent way to process very large JSON documents or fragments. However, because the user has no control or context for the JSON events other than the sequence in which they are processed, parsing JSON in this way frequently requires the user to use complex processing logic to interpret the JSON data. This approach is great when the JSON being parsed is large but simple.

    try {
        boolean parsed=Json.parse(reader, new JsonParser.Handler() {
            @Override
            public void openObject(String name) {
            }

            @Override
            public void closeObject() {
            }

            @Override
            public void openArray(String name) {
            }

            @Override
            public void closeArray() {
            }

            @Override
            public void scalar(String name, boolean value) {
            }

            @Override
            public void scalar(String name, long value) {
            }

            @Override
            public void scalar(String name, double value) {
            }

            @Override
            public void scalar(String name, String value) {
            }

            @Override
            public void nil(String name) {
            }
        });
        if(parsed) {
            // One complete JSON value was successfully parsed from the input
        }
        else {
            // EOF has been reached on the input
        }
    }
    catch(ParseJsonException e) {
        // There was a JSON syntax error
    }
    catch(IOException e) {
        // An I/O problem occurred
    }

### Streaming Pull

In this mode, Jsonification allows the user to "pull" parse events on demand via a method call. This parser also only holds in memory the data from the current event being processed, so it's also an excellent way to process very large JSON documents or fragments. Because this approach allows the user to pull events on demand, it can make JSON processing much simpler. This approach is effective when the JSON being parsed is large but the shape of the data is not known ahead of time because it allows the user more freedom to apply common parsing techniques, like recursive descent.

Simply reading JSON events using this approach is straightforward:

    try {
        try (JsonEventParser p=new JsonEventParser(reader)) {
            for(JsonEvent e=p.next();e!=null;e=p.next()) {
                switch(e.getType()) {
                case OPEN_OBJECT:
                    break;
                case CLOSE_OBJECT:
                    break;
                case OPEN_ARRAY:
                    break;
                case CLOSE_ARRAY:
                    break;
                case SCALAR:
                {
                    switch(e.getValue().getFlavor()) {
                    case NUMBER:
                        break;
                    case BOOLEAN:
                        break;
                    case STRING:
                        break;
                    case NULL:
                        break;
                    default:
                        throw new RuntimeException("unrecognized JSON scalar value type: "+e.getValue().getFlavor());
                    }
                } break;
                case NULL:
                    break;
                default:
                    throw new RuntimeException("unrecognized JSON event type: "+e.getType());
                }
            }
            // EOF has been reached on the input
        }
    }
    catch(ParseJsonException e) {
        // There was a JSON syntax error
    }
    catch(IOException e) {
        // An I/O problem occurred
    }

The event parser also offers methods for a more direct idiom for parsing JSON that is more convenient when you know the shape of the data you're parsing. Because the parsing is incremental, this approach works even for very large JSON documents. This also allows the user to define methods or classes that parse a known data type from a stream on demand, which often simplifies parsing tremendously.

    try {
        try (JsonEventParser p=new JsonEventParser(reader)) {
            p.openObject();

            long id=p.scalar("id").getValue().asNumber().longVal();

            String name=p.scalar("name").getValue().asString().stringVal();

            Set<String> tags=new LinkedHashSet<>();
            p.openArray("tags");
            while(p.peek().getType() != JsonEvent.Type.CLOSE_ARRAY)
                tags.add(p.scalar().getValue().asString().stringVal();
            p.closeArray();
 
            p.closeObject();

            // Assert that EOF has been reached. Not required.
            p.eof();
        }
    }
    catch(ParseJsonException e) {
        // There was a JSON syntax error, or unexpected input was received
    }
    catch(IOException e) {
        // An I/O problem occurred
    }

There is a corresponding event-based approach to writing JSON that is described below.

### Tree Model

In this mode, Jsonification will parse a single JSON document or fragment into a `JsonValue` object. This approach loads an entire document into memory, so it should not be used with documents that are large or of an unknown size, but it's a great way to work with small JSON snippets quickly and easily.

    try {
        JsonValue value=Json.parse(reader);
        if(value != null) {
            // One complete JSON value was successfully parsed from the input
        }
        else {
            // EOF has been reached on the input
        }
    }
    catch(ParseJsonException e) {
        // There was a JSON syntax error
    }
    catch(IOException e) {
        // An I/O problem occurred
    }

## Writing JSON

Jsonification offers two ways to emit JSON data. Like the approaches Jsonification offers to read JSON, the two approaches to writing JSON are designed to allow users to manipulate JSON in ways that feel most natural in the context of their own programs.

### Streaming Push

In this mode, Jsonification allows the users to "push" parse events to be written on demand via method call. This generator writes events directly to the underlying stream, so it's an excellent way to generate very large JSON documents or fragments without keeping the entire document in memory.

    try {
        try (JsonGenerator g=new JsonGenerator(writer)) {
            g.openObject();

            g.scalar("id", id);

            g.scalar("name", name);

            g.openArray("tags");
            for(String tag : tags)
            g.closeArray();
 
            g.closeObject();
        }
    }
    catch(GenerateJsonException e) {
        // The user attempted to generate JSON that would not be syntactically valid
    }
    catch(IOException e) {
        // An I/O problem occurred
    }

There is a corresponding event-based approach to reading JSON that is described above.

### Tree Model

In this mode, Jsonification will generate a JSON document to an underlying stream. This approach requires the entire JSON document to exist in memory, so it's not suitable for large documents, but for small documents or data already loaded into memory, it's a great way to work with JSON.

    try {
        try (JsonTreeGenerator g=new JsonTreeGenerator(writer)) {
            g.emit(value);
        }
    }
    catch(IOException e) {
        // An I/O problem occurred
    }

## Manipulating JSON Trees

Jsonification's `JsonValue` class was carefully designed to make manipulating JSON simple and natural. Custom `JsonExceptions` are used (as opposed to builtin exceptions like `NullPointerException` or `ClassCastException`) to make it easier for the user to recognize and fix issues related to JSON processing (as opposed to errors in other program logic) quickly.
