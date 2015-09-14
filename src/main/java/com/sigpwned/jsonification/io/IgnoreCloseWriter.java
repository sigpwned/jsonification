package com.sigpwned.jsonification.io;

import java.io.IOException;
import java.io.Writer;

public class IgnoreCloseWriter extends Writer {
    private final Writer delegate;
    
    public IgnoreCloseWriter(Writer delegate) {
        this.delegate = delegate;
    }

    public int hashCode() {
        return getDelegate().hashCode();
    }

    public void write(int c) throws IOException {
        getDelegate().write(c);
    }

    public void write(char[] cbuf) throws IOException {
        getDelegate().write(cbuf);
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        getDelegate().write(cbuf, off, len);
    }

    public boolean equals(Object obj) {
        return getDelegate().equals(obj);
    }

    public void write(String str) throws IOException {
        getDelegate().write(str);
    }

    public void write(String str, int off, int len) throws IOException {
        getDelegate().write(str, off, len);
    }

    public Writer append(CharSequence csq) throws IOException {
        return getDelegate().append(csq);
    }

    public Writer append(CharSequence csq, int start, int end)
            throws IOException {
        return getDelegate().append(csq, start, end);
    }

    public Writer append(char c) throws IOException {
        return getDelegate().append(c);
    }

    public void flush() throws IOException {
        getDelegate().flush();
    }

    public void close() throws IOException {
        getDelegate().close();
    }

    public String toString() {
        return getDelegate().toString();
    }

    private Writer getDelegate() {
        return delegate;
    }
    
}
