package org.tastefuljava.classfile;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClassFileTest {
    
    public ClassFileTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testRunnable()
            throws IllegalAccessException, InstantiationException, IOException {
        ClassFile cf = new ClassFile("TestCase$0001");
        ConstantPool cp = cf.getConstantPool();
        cf.addInterface("java/lang/Runnable");
        cf.setMajorVersion((short)48);

        // constructor
        {
            CodeBuilder cb = new CodeBuilder(cp, 1);
            cb.loadRef(0);
            cb.invokeSpecial("java/lang/Object", "<init>", "()V");
            cb.returnVoid();
            AttributeInfo code = new AttributeInfo(
                    cp.addUtf8("Code"), cb.getBytes());
            MethodInfo mi = new MethodInfo(MethodInfo.ACC_PUBLIC,
                    cp.addUtf8("<init>"), cp.addUtf8("()V"));
            mi.addAttribute(code);
            cf.addMethod(mi);
        }

        { // run()
            CodeBuilder cb = new CodeBuilder(cp, 1);
            cb.getStatic("java/lang/System", "out", "Ljava/io/PrintStream;");
            cb.pushString("Hello world!!!");
            cb.invokeVirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V");
            cb.returnVoid();
            AttributeInfo code = new AttributeInfo(
                    cp.addUtf8("Code"), cb.getBytes());
            MethodInfo mi = new MethodInfo(MethodInfo.ACC_PUBLIC,
                    cp.addUtf8("run"), cp.addUtf8("()V"));
            mi.addAttribute(code);
            cf.addMethod(mi);
        }
        cf.print(System.out);
        Class<? extends Runnable> cls = (Class<? extends Runnable>) cf.define();
        System.out.println("Class defined");
        Runnable r = cls.newInstance();
        System.out.println("Class instanciated");
        r.run();
        System.out.println("Instance ran");
    }
}
