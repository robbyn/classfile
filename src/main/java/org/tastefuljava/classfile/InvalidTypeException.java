package org.tastefuljava.classfile;

public class InvalidTypeException extends ClassFileException {

    public InvalidTypeException() {
    }

    public InvalidTypeException(String type) {
        super("Invalid type: " + type);
    }
}