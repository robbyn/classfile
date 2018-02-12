package org.tastefuljava.classfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class AttributeInfo {
    private short nameIndex;
    private byte data[];

    private AttributeInfo() {
    }

    public AttributeInfo(short nameIndex, byte data[]) {
        this.nameIndex = nameIndex;
        this.data = data;
    }

    public short getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(short newValue) {
        nameIndex = newValue;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte newValue[]) {
        data = newValue;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }

    public DataInputStream getDataInput() {
        return new DataInputStream(getInputStream());
    }

    public OutputStream getOutputStream() {
        return new ByteArrayOutputStream() {
            @Override
            public void close() {
                data = toByteArray();
            }
        };
    }

    public DataOutputStream getDataOutput() {
        return new DataOutputStream(getOutputStream());
    }

    public void load(DataInput input) throws IOException {
        nameIndex = input.readShort();
        data = new byte[input.readInt()];
        input.readFully(data);
    }

    public static List<AttributeInfo> loadList(DataInput input)
            throws IOException {
        int count = input.readShort() & 0xFFFF;
        List<AttributeInfo> result = new ArrayList<>(count);
        for (int i = 0; i < count; ++i) {
            AttributeInfo attr = new AttributeInfo();
            attr.load(input);
            result.add(attr);
        }
        return result;
    }

    public void store(DataOutput output) throws IOException {
        output.writeShort(nameIndex);
        output.writeInt(data.length);
        output.write(data);
    }

    public static void storeList(DataOutput output, List<AttributeInfo> list)
            throws IOException {
        output.writeShort(list.size());
        for (AttributeInfo attr: list) {
            attr.store(output);
        }
    }

    public void print(ConstantPool cp, PrintStream out) throws IOException {
        out.println("attribute " + cp.getUtf8(nameIndex)
                + " length: " + data.length);
    }

    public static void printList(ConstantPool cp, PrintStream out,
            List<AttributeInfo> list) throws IOException {
        out.println("Attributes: " + list.size());
        for (AttributeInfo attr: list) {
            attr.print(cp, out);
        }
        out.println();
    }
}
