package org.tastefuljava.classfile;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MethodInfo {
    /** access flag */
    public static final short ACC_PUBLIC = 0x0001;
    /** access flag */
    public static final short ACC_PRIVATE = 0x0002;
    /** access flag */
    public static final short ACC_PROTECTED = 0x0004;
    /** access flag */
    public static final short ACC_STATIC = 0x0008;
    /** access flag */
    public static final short ACC_FINAL = 0x0010;
    /** access flag */
    public static final short ACC_SYNCHRONIZED = 0x0020;
    /** access flag */
    public static final short ACC_NATIVE = 0x0100;
    /** access flag */
    public static final short ACC_ABSTRACT = 0x0400;
    /** access flag */
    public static final short ACC_STRICT = 0x0800;

    private short accessFlags;
    private short nameIndex;
    private short descrIndex;
    private List<AttributeInfo> attributes = new ArrayList<>();

    public MethodInfo() {
    }

    public MethodInfo(int accessFlags, short nameIndex, short descrIndex) {
        this.accessFlags = (short)accessFlags;
        this.nameIndex = nameIndex;
        this.descrIndex = descrIndex;
    }

    public short getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(short newValue) {
        accessFlags = newValue;
    }

    public short getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(short newValue) {
        nameIndex = newValue;
    }

    public short getDescrIndex() {
        return descrIndex;
    }

    public void setDescrIndex(short newValue) {
        descrIndex = newValue;
    }

    public AttributeInfo[] getAttributes() {
        return attributes.toArray(new AttributeInfo[attributes.size()]);
    }

    public void addAttribute(AttributeInfo attr) {
        attributes.add(attr);
    }

    public AttributeInfo findAttribute(short nameIndex) {
        for (AttributeInfo attr: attributes) {
            if (attr.getNameIndex() == nameIndex) {
                return attr;
            }
        }
        return null;
    }

    public void load(DataInput input) throws IOException {
        accessFlags = input.readShort();
        nameIndex = input.readShort();
        descrIndex = input.readShort();
        attributes = AttributeInfo.loadList(input);
    }

    public void store(DataOutput output) throws IOException {
        output.writeShort(accessFlags);
        output.writeShort(nameIndex);
        output.writeShort(descrIndex);
        AttributeInfo.storeList(output, attributes);
    }

    public void print(ConstantPool cp, PrintStream out) throws IOException {
        out.println("access_flags = " + flagsToString(accessFlags));
        out.println("name_index = " + cp.getUtf8(nameIndex)
                + "(" + nameIndex + ")");
        out.println("descriptor_index = " + cp.getUtf8(descrIndex)
                + "(" + descrIndex + ")");
        AttributeInfo.printList(cp, out, attributes);
    }

    private static String flagsToString(int flags) {
        StringBuilder buf = new StringBuilder();
        if ((ACC_PUBLIC & flags) != 0) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append("ACC_PUBLIC");
        }
        if ((ACC_PRIVATE & flags) != 0) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append("ACC_PRIVATE");
        }
        if ((ACC_PROTECTED & flags) != 0) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append("ACC_PROTECTED");
        }
        if ((ACC_STATIC & flags) != 0) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append("ACC_STATIC");
        }
        if ((ACC_FINAL & flags) != 0) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append("ACC_FINAL");
        }
        if ((ACC_SYNCHRONIZED & flags) != 0) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append("ACC_SYNCHRONIZED");
        }
        if ((ACC_NATIVE & flags) != 0) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append("ACC_NATIVE");
        }
        if ((ACC_ABSTRACT & flags) != 0) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append("ACC_ABSTRACT");
        }
        if ((ACC_STRICT & flags) != 0) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append("ACC_STRICT");
        }
        return buf.toString();
    }
}
