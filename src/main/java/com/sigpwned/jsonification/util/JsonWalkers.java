package com.sigpwned.jsonification.util;

import java.io.IOException;

import com.sigpwned.jsonification.JsonGenerator;
import com.sigpwned.jsonification.JsonWalker;
import com.sigpwned.jsonification.exception.GenerateJsonException;

public class JsonWalkers {
    public static JsonWalker.Handler newGeneratorHandler(final JsonGenerator g) {
        return new JsonWalker.Handler() {
            @Override
            public void scalar(String name, String value) {
                try {
                    g.scalar(name, value);
                }
                catch(IOException e) {
                    throw new GenerateJsonException(e);
                }
            }
            
            @Override
            public void scalar(String name, boolean value) {
                try {
                    g.scalar(name, value);
                }
                catch(IOException e) {
                    throw new GenerateJsonException(e);
                }
            }
            
            @Override
            public void scalar(String name, double value) {
                try {
                    g.scalar(name, value);
                }
                catch(IOException e) {
                    throw new GenerateJsonException(e);
                }
            }
            
            @Override
            public void scalar(String name, long value) {
                try {
                    g.scalar(name, value);
                }
                catch(IOException e) {
                    throw new GenerateJsonException(e);
                }
            }
            
            @Override
            public void openObject(String name) {
                try {
                    g.openObject(name);
                }
                catch(IOException e) {
                    throw new GenerateJsonException(e);
                }
            }
            
            @Override
            public void nil(String name) {
                try {
                    g.nil(name);
                }
                catch(IOException e) {
                    throw new GenerateJsonException(e);
                }
            }
            
            @Override
            public void closeObject() {
                try {
                    g.closeObject();
                }
                catch(IOException e) {
                    throw new GenerateJsonException(e);
                }
            }
            
            @Override
            public void openArray(String name) {
                try {
                    g.openArray(name);
                }
                catch(IOException e) {
                    throw new GenerateJsonException(e);
                }
            }
            
            @Override
            public void closeArray() {
                try {
                    g.closeArray();
                }
                catch(IOException e) {
                    throw new GenerateJsonException(e);
                }
            }
        };
    }
}
