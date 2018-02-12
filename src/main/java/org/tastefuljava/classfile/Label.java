package org.tastefuljava.classfile;

import java.util.ArrayList;
import java.util.List;

public class Label {
    private boolean defined;
    private int location;
    private final List<LabelRef> refs = new ArrayList<>();

    public Label() {
    }

    public boolean isDefined() {
        return defined;
    }

    public int getLocation() {
        return location;
    }

    public Label copy(int offset) {
        Label copy = new Label();
        copy.location = location + offset;
        for (LabelRef ref: refs) {
            copy.refs.add(ref.copy(offset));
        }
        return copy;
    }

    void define(int newLoc) {
        defined = true;
        location = newLoc;
    }

    void fixupRefs(CodeBuilder cb) {
        for (LabelRef ref: refs) {
            ref.fixup(cb, location);
        }
    }

    void addRef(LabelRef ref) {
        refs.add(ref);
    }
}
