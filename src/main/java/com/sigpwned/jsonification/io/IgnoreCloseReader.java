package com.sigpwned.jsonification.io;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class IgnoreCloseReader extends Reader {
    private final Reader delegate;
    
    public IgnoreCloseReader(Reader delegate) {
        this.delegate = delegate;
    }

    public int hashCode() {
        return getDelegate().hashCode();
    }

    public int read(CharBuffer target) throws IOException {
        return getDelegate().read(target);
    }

    public int read() throws IOException {
        return getDelegate().read();
    }

    public int read(char[] cbuf) throws IOException {
        return getDelegate().read(cbuf);
    }

    public boolean equals(Object obj) {
        return getDelegate().equals(obj);
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        return getDelegate().read(cbuf, off, len);
    }

    public long skip(long n) throws IOException {
        return getDelegate().skip(n);
    }

    public boolean ready() throws IOException {
        return getDelegate().ready();
    }

    public boolean markSupported() {
        return getDelegate().markSupported();
    }

    public void mark(int readAheadLimit) throws IOException {
        getDelegate().mark(readAheadLimit);
    }

    public void reset() throws IOException {
        getDelegate().reset();
    }

    public void close() throws IOException {
        getDelegate().close();
    }

    public String toString() {
        return getDelegate().toString();
    }

    private Reader getDelegate() {
        return delegate;
    }
}
