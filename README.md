# Jsonification
JSON is a simple data format. Working with JSON should be simple, too. Jsonification is a simple, dependency-free JSON processing library that tries to make working with JSON in Java simple and natural, no matter what your task or program architecture is.

## Overview
Jsonification provides ways to parse and emit JSON incrementally, which is perfect for working with large JSON documents, or as a tree, which makes manipulating small JSON documents like API responses a senap. For example, parsing [an example Twitter API response](https://dev.twitter.com/rest/reference/get/users/show) and pulling out a few fields is just a few simple lines of code:

    String userJsonResponse=getUserDataFromTwitter("twitterdev");
    JsonObject o=Json.parse(userJsonResponse).asObject();
    long id=o.get("id").asScalar().asNumber().longVal(); // 2244994945
    String screenName=o.get("screen_name").asScalar().asString().stringVal();
    int followers=o.get("followers_count").asScalar().asNumber().intVal(); // 143916
    boolean following=o.get("following").asScalar().asBoolean().booleanVal(); // false
    
If you're working with JSON trees, Jsonification also makes working with JSON `null` -- a common difficulty in other libraries -- by representing it with a custom value instead of using Java's native `null` value. This allows you to manipulate JSON without worrying about `NullPointerException`s. To continue the above example, these are all ways you could determine if the user has ever sent a tweet by checking if the user's most recent tweet is `null`:

    if(o.get("status").isNull()) {
        // The user has never tweeted
    }
    
    if(o.get("status") == Json.NULL) {
        // The user has never tweeted
    }
    
    if(o.get("status").getType() == JsonValue.Type.NULL) {
        // The user has never tweeted
    }
    
Jsonification is also careful to use its own exceptions instead of Java's builtin exceptions to make telling the difference between JSON processing errors and program logic errors easier. For example, If you did try to read through the "status" attribute above and it was `null`, Jsonification would throw a `JsonNullException` instead of a `NullPointerException`. All Jsonification exceptions inherit from `JsonException`, which means you can isolate and handle JSON processing errors easily.

    String tweet;
    try {
        tweet = o.get("status").asObject().get("text").asScalar().asString().stringVal();
    }
    catch(JsonNullException e) {
        // Oops! One of the values we dereferenced above was a JSON null.
        tweet = null;
    }
    catch(JsonException e) {
        // Oops! Some other JSON processing-related exception has occurred.
        tweet = null;
    }

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
    
### Value Model

In this mode, Jsonification will generate parse events from an existing `JsonValue` as if they were being read from input. This approach allows users to construct values and play them back as events for consistency during serialization. Just like any other `JsonParser`, this parser can be converted into an event parser.

    try {
        try (JsonParser p=Json.newValueParser(value)) {
            boolean parsed=p.parse(new JsonParser.Handler() {
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
    }
    catch(IOException e) {
        // An error occurred
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

For example, if you were working directly with the [Twitter REST API](https://dev.twitter.com/rest/public) and had just received [an API response containing a user object](https://dev.twitter.com/rest/reference/get/users/show), then this snippet would allow you to pull out specific fields quickly:

    String userJsonResponse=getUserDataFromTwitter("twitterdev");
    JsonObject o=Json.parse(userJsonResponse).asObject();
    long id=o.get("id").asScalar().asNumber().longVal(); // 2244994945
    String screenName=o.get("screen_name").asScalar().asString().stringVal();
    int followers=o.get("followers_count").asScalar().asNumber().intVal(); // 143916
    boolean following=o.get("following").asScalar().asBoolean().booleanVal(); // false
    
## Maven

The latest version of Jsonification is available from Maven Central at the following coordinates:

    <dependency>
        <groupId>com.sigpwned</groupId>
        <artifactId>jsonification</artifactId>
        <version>3.0.5</version>
    </dependency>
