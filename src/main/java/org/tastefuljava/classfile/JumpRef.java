package org.tastefuljava.classfile;

public class JumpRef extends LabelRef {
    private final int jumpLocation;

    public JumpRef(int jumpLocation) {
        this.jumpLocation = jumpLocation;
    }

    @Override
    public void fixup(CodeSegment code, int location) {
        code.fixupShort(jumpLocation+1, location-jumpLocation);
    }

    @Override
    public LabelRef copy(int offset) {
        return new JumpRef(jumpLocation + offset);
    }
}
