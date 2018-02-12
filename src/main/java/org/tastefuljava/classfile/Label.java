package org.tastefuljava.classfile;

import java.util.ArrayList;
import java.util.List;

public class Label {
    private boolean defined;
    private int location;
    private List<LabelRef> refs;

    public Label() {
    }

    public boolean isDefined() {
        return defined;
    }

    public int getLocation() {
        return location;
    }

    void define(int newLoc) {
        defined = true;
        location = newLoc;
    }

    void fixupRefs(CodeBuilder cb) {
        if (refs != null) {
            for (LabelRef ref: refs) {
                ref.fixup(cb, location);
            }
        }
    }

    void addRef(LabelRef ref) {
        if (refs == null) {
            refs = new ArrayList<>();
        }
        refs.add(ref);
    }
}
