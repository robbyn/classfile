package org.tastefuljava.classfile;

public abstract class LabelRef {
    public abstract void fixup(CodeSegment code, int location);
    public abstract LabelRef copy(int offset);
}
