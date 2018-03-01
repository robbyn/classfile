package org.tastefuljava.classfile;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeBuilder extends CodeSegment {
    private final List<ExceptionInfo> exceptions = new ArrayList<>();
    private final List<AttributeInfo> attributes = new ArrayList<>();

    public CodeBuilder(ConstantPool cp, int locals) {
        super(cp, null, locals);
    }

    public void load(DataInput input) throws IOException {
        stackMax = input.readShort();
        stackTop = 0;
        localMax = input.readShort();
        localTop = 0;
        buf = new byte[input.readInt()];
        input.readFully(buf);
        count = buf.length;
        exceptions.clear();
        int n = input.readShort() & 0xFFFF;
        for (int i = 0; i < n; ++i) {
            ExceptionInfo ei = new ExceptionInfo();
            ei.load(input);
            exceptions.add(ei);
        }
        attributes.clear();
        attributes.addAll(AttributeInfo.loadList(input));
    }

    public void store(DataOutput output) throws IOException {
        /* fixup label references */
        commit();
        output.writeShort(stackMax);
        output.writeShort(localMax);
        output.writeInt(count);
        output.write(buf, 0, count);
        output.writeShort(exceptions.size());
        for (ExceptionInfo ei: exceptions) {
            ei.store(output);
        }
        AttributeInfo.storeList(output, attributes);
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final DataOutputStream dos = new DataOutputStream(baos)) {
            store(dos);
        }
        return baos.toByteArray();
    }

    public void addException(ExceptionInfo ei) {
        exceptions.add(ei);
    }

    public void addException(short startPc, short endPc, short handlerPc,
            short typeIndex) {
        addException(new ExceptionInfo(startPc, endPc, handlerPc, typeIndex));
    }

    public void addException(short startPc, short endPc, short handlerPc,
            String typeName) {
        addException(startPc, endPc, handlerPc, cp.addClass(typeName));
    }

}
