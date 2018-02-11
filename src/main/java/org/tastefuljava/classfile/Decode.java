package org.tastefuljava.classfile;

public class Decode {

    public static void main(String[] args) {
        try {
            for (String arg: args) {
                ClassFile cf = new ClassFile();
                cf.load(arg);
                cf.print(System.out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
