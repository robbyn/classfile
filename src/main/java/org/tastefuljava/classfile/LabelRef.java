package org.tastefuljava.classfile;

public abstract class LabelRef {
    public abstract void fixup(CodeBuilder cb, int location);
    public abstract LabelRef copy(int offset);
}
