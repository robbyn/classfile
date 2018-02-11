package org.tastefuljava.classfile;
import java.io.*;
import java.util.*;

public class ConstantPool {
    /** constant tag */
    private static final int CP_CLASS = 7;
    /** constant tag */
    private static final int CP_FIELDREF = 9;
    /** constant tag */
    private static final int CP_METHODREF = 10;
    /** constant tag */
    private static final int CP_INTERFACEMETHODREF = 11;
    /** constant tag */
    private static final int CP_STRING = 8;
    /** constant tag */
    private static final int CP_INTEGER = 3;
    /** constant tag */
    private static final int CP_FLOAT = 4;
    /** constant tag */
    private static final int CP_LONG = 5;
    /** constant tag */
    private static final int CP_DOUBLE = 6;
    /** constant tag */
    private static final int CP_NAMEANDTYPE = 12;
    /** constant tag */
    private static final int CP_UTF8 = 1;

    /** list of the constant pool entries */
    private List<Entry> entries = new ArrayList<>();

    public ConstantPool() {
        entries.add(NullEntry.INSTANCE);
    }

    public int getCount() {
        return entries.size();
    }

    public String toString(short index) {
        Entry entry = (Entry)entries.get(index);
        return entry.toString(this);
    }

    public short addUtf8(String value) {
        return addEntry(new Utf8Entry(value));
    }

    public short addClass(String className) {
        return addEntry(new ClassEntry(addUtf8(className)));
    }

    public short addNameAndType(short nameIndex, short typeIndex) {
        return addEntry(new NameAndTypeEntry(nameIndex, typeIndex));
    }

    public short addNameAndType(String name, String descr) {
        return addNameAndType(addUtf8(name), addUtf8(descr));
    }

    public short addFieldref(short classIndex, short natIndex) {
        return addEntry(new FieldrefEntry(classIndex, natIndex));
    }

    public short addFieldref(short classIndex, short nameIndex,
            short typeIndex) {
        return addFieldref(classIndex, addNameAndType(nameIndex, typeIndex));
    }

    public short addFieldref(short classIndex, String field, String type) {
        return addFieldref(classIndex, addNameAndType(field, type));
    }

    public short addFieldref(String className, String field, String type) {
        return addFieldref(addClass(className), field, type);
    }

    public short addMethodref(short classIndex, short natIndex) {
        return addEntry(new MethodrefEntry(classIndex, natIndex));
    }

    public short addMethodref(short classIndex, short nameIndex,
            short typeIndex) {
        return addMethodref(classIndex, addNameAndType(nameIndex, typeIndex));
    }

    public short addMethodref(short classIndex, String field, String type) {
        return addMethodref(classIndex, addUtf8(field), addUtf8(type));
    }

    public short addMethodref(String className, String field, String type) {
        return addMethodref(addClass(className), field, type);
    }

    public short addInterfaceMethodref(short classIndex, short natIndex) {
        return addEntry(new InterfaceMethodrefEntry(classIndex, natIndex));
    }

    public short addInterfaceMethodref(short classIndex, short nameIndex,
            short typeIndex) {
        return addInterfaceMethodref(classIndex,
                addNameAndType(nameIndex, typeIndex));
    }

    public short addInterfaceMethodref(short classIndex, String field,
            String type) {
        return addInterfaceMethodref(classIndex, addUtf8(field), addUtf8(type));
    }

    public short addInterfaceMethodref(String className, String field,
                String type) {
        return addInterfaceMethodref(addClass(className), field, type);
    }

    public short addInteger(int value) {
        return addEntry(new IntegerEntry(value));
    }

    public short addFloat(float value) {
        return addEntry(new FloatEntry(value));
    }

    public short addLong(long value) {
        return addEntry(new LongEntry(value));
    }

    public short addDouble(double value) {
        return addEntry(new DoubleEntry(value));
    }

    public short addString(String value) {
        return addEntry(new StringEntry(addUtf8(value)));
    }

    public String getUtf8(short index) {
        Utf8Entry entry = (Utf8Entry)entries.get(index);
        return entry.value;
    }

    public String getString(short index) {
        StringEntry entry = (StringEntry)entries.get(index);
        return getUtf8(entry.stringIndex);
    }

    public String getClassName(short index) {
        ClassEntry entry = (ClassEntry)entries.get(index);
        return getUtf8(entry.nameIndex);
    }

    public short getNameIndex(short index) {
        NameAndTypeEntry entry = (NameAndTypeEntry)entries.get(index);
        return entry.nameIndex;
    }

    public short getTypeIndex(short index) {
        NameAndTypeEntry entry = (NameAndTypeEntry)entries.get(index);
        return entry.descrIndex;
    }

    public String getName(short index) {
        return getUtf8(getNameIndex(index));
    }

    public String getType(short index) {
        return getUtf8(getTypeIndex(index));
    }

    public short getRefClassIndex(short index) {
        RefEntry entry = (RefEntry)entries.get(index);
        return entry.classIndex;
    }

    public String getRefClassName(short index) {
        RefEntry entry = (RefEntry)entries.get(index);
        return getClassName(entry.classIndex);
    }

    public short getRefNameIndex(short index) {
        RefEntry entry = (RefEntry)entries.get(index);
        return getNameIndex(entry.nameAndTypeIndex);
    }

    public String getRefName(short index) {
        RefEntry entry = (RefEntry)entries.get(index);
        return getName(entry.nameAndTypeIndex);
    }

    public short getRefTypeIndex(short index) {
        RefEntry entry = (RefEntry)entries.get(index);
        return getTypeIndex(entry.nameAndTypeIndex);
    }

    public String getRefType(short index) {
        RefEntry entry = (RefEntry)entries.get(index);
        return getType(entry.nameAndTypeIndex);
    }

    public void load(DataInput input) throws IOException {
        short count = input.readShort();
        entries.clear();
        entries.add(NullEntry.INSTANCE);
        for (int i = 1; i < count; ++i) {
            int tag = input.readByte();
            Entry entry;
            switch (tag) {
            case CP_CLASS:
                entry = new ClassEntry();
                break;

            case CP_FIELDREF:
                entry = new FieldrefEntry();
                break;

            case CP_METHODREF:
                entry = new MethodrefEntry();
                break;

            case CP_INTERFACEMETHODREF:
                entry = new InterfaceMethodrefEntry();
                break;

            case CP_STRING:
                entry = new StringEntry();
                break;

            case CP_INTEGER:
                entry = new IntegerEntry();
                break;

            case CP_FLOAT:
                entry = new FloatEntry();
                break;

            case CP_LONG:
                entry = new LongEntry();
                break;

            case CP_DOUBLE:
                entry = new DoubleEntry();
                break;

            case CP_NAMEANDTYPE:
                entry = new NameAndTypeEntry();
                break;

            case CP_UTF8:
                entry = new Utf8Entry();
                break;

            default:
                throw new StreamCorruptedException(
                        "invalid constant pool tag: " + tag);
            }

            entry.load(input);
            entries.add(entry);
            if (tag == CP_LONG || tag == CP_DOUBLE) {
                entries.add(NullEntry.INSTANCE);
                ++i;
            }
        }
    }

    public void store(DataOutput output) throws IOException {
        output.writeShort(entries.size());
        for (Entry entry: entries) {
            entry.store(output);
        }
    }

    public void print(PrintStream out) {
        out.println("number of entries: " + entries.size());
        int i = 0;
        for (Entry entry: entries) {
            out.println(Integer.toString(i++) + " = " + entry.toString(this));
        }
    }

    private short addEntry(Entry entry) {
        int count = entries.size();
        for (int i = 0; i < count; ++i) {
            if (entry.equals(entries.get(i))) {
                return (short)i;
            }
        }
        entries.add(entry);
        return (short)count;
    }

    private abstract static class Entry {
        abstract void load(DataInput input) throws IOException;
        abstract void store(DataOutput input) throws IOException;
        abstract String toString(ConstantPool cp);
    }

    private static class NullEntry extends Entry {
        private static final NullEntry INSTANCE = new NullEntry();

        @Override
        public boolean equals(Object obj) {
            return false;
        }

        @Override
        void load(DataInput input) {
        }

        @Override
        void store(DataOutput output) {
        }

        @Override
        String toString(ConstantPool cp) {
            return "";
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    private static class ClassEntry extends Entry {
        short nameIndex;

        ClassEntry() {
        }

        ClassEntry(short nameIndex) {
            this.nameIndex = nameIndex;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ClassEntry) {
                ClassEntry other = (ClassEntry)obj;
                return this.nameIndex == other.nameIndex;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return nameIndex;
        }

        @Override
        void load(DataInput input) throws IOException {
            nameIndex = input.readShort();
        }

        @Override
        void store(DataOutput output) throws IOException {
            output.writeByte(CP_CLASS);
            output.writeShort(nameIndex);
        }

        @Override
        String toString(ConstantPool cp) {
            return "class " + cp.toString(nameIndex);
        }
    }

    private static abstract class RefEntry extends Entry {
        short classIndex;
        short nameAndTypeIndex;

        RefEntry() {
        }

        RefEntry(short classIndex, short nameAndTypeIndex) {
            this.classIndex = classIndex;
            this.nameAndTypeIndex = nameAndTypeIndex;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() == this.getClass()) {
                RefEntry other = (RefEntry)obj;
                return this.classIndex == other.classIndex
                        && this.nameAndTypeIndex == other.nameAndTypeIndex;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + this.classIndex;
            hash = 59 * hash + this.nameAndTypeIndex;
            return hash;
        }

        @Override
        void load(DataInput input) throws IOException {
            classIndex = input.readShort();
            nameAndTypeIndex = input.readShort();
        }

        @Override
        void store(DataOutput output) throws IOException {
            output.writeByte(getTag());
            output.writeShort(classIndex);
            output.writeShort(nameAndTypeIndex);
        }

        abstract int getTag();

        @Override
        String toString(ConstantPool cp) {
            return cp.getClassName(classIndex).replace('/','.')
                + cp.getName(nameAndTypeIndex) + " "
                + cp.getType(nameAndTypeIndex);
        }
    }

    private static class FieldrefEntry extends RefEntry {
        FieldrefEntry() {
            super();
        }

        FieldrefEntry(short classIndex, short nameAndTypeIndex) {
            super(classIndex, nameAndTypeIndex);
        }

        @Override
        String toString(ConstantPool cp) {
            return "fieldref " + super.toString(cp);
        }

        @Override
        int getTag() {
            return CP_FIELDREF;
        }
    }

    private static class MethodrefEntry extends RefEntry {
        MethodrefEntry() {
            super();
        }

        MethodrefEntry(short classIndex, short nameAndTypeIndex) {
            super(classIndex, nameAndTypeIndex);
        }

        @Override
        String toString(ConstantPool cp) {
            return "methodref " + super.toString(cp);
        }

        @Override
        int getTag() {
            return CP_METHODREF;
        }
    }

    private static class InterfaceMethodrefEntry extends RefEntry {
        InterfaceMethodrefEntry() {
            super();
        }

        InterfaceMethodrefEntry(short classIndex, short nameAndTypeIndex) {
            super(classIndex, nameAndTypeIndex);
        }

        @Override
        String toString(ConstantPool cp) {
            return "interfacemethodref " + super.toString(cp);
        }

        @Override
        int getTag() {
            return CP_INTERFACEMETHODREF;
        }
    }

    private static class StringEntry extends Entry {
        short stringIndex;

        StringEntry() {
        }

        StringEntry(short stringIndex) {
            this.stringIndex = stringIndex;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof StringEntry) {
                StringEntry other = (StringEntry)obj;
                return this.stringIndex == other.stringIndex;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return stringIndex;
        }

        @Override
        void load(DataInput input) throws IOException {
            stringIndex = input.readShort();
        }

        @Override
        void store(DataOutput output) throws IOException {
            output.writeByte(CP_STRING);
            output.writeShort(stringIndex);
        }

        @Override
        String toString(ConstantPool cp) {
            return '"' + cp.toString(stringIndex) + '"';
        }
    }

    private static class IntegerEntry extends Entry {
        int value;

        IntegerEntry() {
        }

        IntegerEntry(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof IntegerEntry) {
                IntegerEntry other = (IntegerEntry)obj;
                return this.value == other.value;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(value);
        }

        @Override
        void load(DataInput input) throws IOException {
            value = input.readInt();
        }

        @Override
        void store(DataOutput output) throws IOException {
            output.writeByte(CP_INTEGER);
            output.writeInt(value);
        }

        @Override
        String toString(ConstantPool cp) {
            return "int " + Integer.toString(value);
        }
    }

    private static class FloatEntry extends Entry {
        float value;

        FloatEntry() {
        }

        FloatEntry(float value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof FloatEntry) {
                FloatEntry other = (FloatEntry)obj;
                return this.value == other.value;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Float.hashCode(value);
        }

        @Override
        void load(DataInput input) throws IOException {
            value = input.readFloat();
        }

        @Override
        void store(DataOutput output) throws IOException {
            output.writeByte(CP_FLOAT);
            output.writeFloat(value);
        }

        @Override
        String toString(ConstantPool cp) {
            return "float " + Float.toString(value);
        }
    }

    private static class LongEntry extends Entry {
        long value;

        LongEntry() {
        }

        LongEntry(long value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof LongEntry) {
                LongEntry other = (LongEntry)obj;
                return this.value == other.value;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Long.hashCode(value);
        }

        @Override
        void load(DataInput input) throws IOException {
            value = input.readLong();
        }

        @Override
        void store(DataOutput output) throws IOException {
            output.writeByte(CP_LONG);
            output.writeLong(value);
        }

        @Override
        String toString(ConstantPool cp) {
            return "long " + Long.toString(value);
        }
    }

    private static class DoubleEntry extends Entry {
        double value;

        DoubleEntry() {
        }

        DoubleEntry(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DoubleEntry) {
                DoubleEntry other = (DoubleEntry)obj;
                return this.value == other.value;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }

        @Override
        void load(DataInput input) throws IOException {
            value = input.readDouble();
        }

        @Override
        void store(DataOutput output) throws IOException {
            output.writeByte(CP_DOUBLE);
            output.writeDouble(value);
        }

        @Override
        String toString(ConstantPool cp) {
            return "double " + Double.toString(value);
        }
    }

    private static class NameAndTypeEntry extends Entry {
        short nameIndex;
        short descrIndex;

        NameAndTypeEntry() {
        }

        NameAndTypeEntry(short nameIndex, short descrIndex) {
            this.nameIndex = nameIndex;
            this.descrIndex = descrIndex;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof NameAndTypeEntry) {
                NameAndTypeEntry other = (NameAndTypeEntry)obj;
                return this.nameIndex == other.nameIndex
                        && this.descrIndex == other.descrIndex;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + this.nameIndex;
            hash = 89 * hash + this.descrIndex;
            return hash;
        }

        @Override
        void load(DataInput input) throws IOException {
            nameIndex = input.readShort();
            descrIndex = input.readShort();
        }

        @Override
        void store(DataOutput output) throws IOException {
            output.writeByte(CP_NAMEANDTYPE);
            output.writeShort(nameIndex);
            output.writeShort(descrIndex);
        }

        @Override
        String toString(ConstantPool cp) {
            return cp.getUtf8(nameIndex) + " " + cp.getUtf8(descrIndex);
        }
    }

    private static class Utf8Entry extends Entry {
        String value;

        Utf8Entry() {
        }

        Utf8Entry(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Utf8Entry) {
                Utf8Entry other = (Utf8Entry)obj;
                return this.value.equals(other.value);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return value == null ? 0 : value.hashCode();
        }

        @Override
        void load(DataInput input) throws IOException {
            value = input.readUTF();
        }

        @Override
        void store(DataOutput output) throws IOException {
            output.writeByte(CP_UTF8);
            output.writeUTF(value);
        }

        @Override
        String toString(ConstantPool cp) {
            return value;
        }
    }
}
