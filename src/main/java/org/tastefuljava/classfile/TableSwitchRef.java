package org.tastefuljava.classfile;

public class TableSwitchRef extends LabelRef {
    private final int opLocation;
    private final int refLocation;

    public TableSwitchRef(int opLocation, int refLocation) {
        this.opLocation = opLocation;
        this.refLocation = refLocation;
    }

    @Override
    public void fixup(CodeBuilder cb, int location) {
        cb.fixupInt(refLocation, location - opLocation);
    }

    @Override
    public LabelRef copy(int offset) {
        return new TableSwitchRef(opLocation + offset, refLocation + offset);
    }
}
