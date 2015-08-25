/*
 * MoXie (SysTem128@GMail.Com) 2009-5-3 0:08:41
 * $Id: ZtplClassWriter.java 71 2010-01-07 05:49:47Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.compiler;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.zoeey.ztpl.Ztpl;
import org.zoeey.ztpl.ZtplConstant;

 

/**
 * 类书写器
 * @author MoXie(SysTem128@GMail.Com)
 */
class ZtplClassWriter {

    /**
     * 
     */
    private ClassWriter cw = null;
    /**
     *
     */
    private MethodVisitor mv = null;
    /**
     * 
     */
    private CompileTracker ct = null;
    /**
     *
     */
    private Ztpl ztpl = null;

    /**
     * 定义类书写器
     * @param className
     */
    public ZtplClassWriter(String className, Ztpl ztpl) {
        startClassWrite(className);
        startMethodWrite();

    }

    /**
     * 开始写Class
     */
    private void startClassWrite(String className) {
        cw = new ClassWriter(0);
        /**
         *
         */
        cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER,//
                ZtplConstant.CLASS_URI + className, null, "java/lang/Object", //
                new String[]{ZtplConstant.TEMPLATE_INTERFACE});
        {
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            //
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
    }

    /**
     * 开始写Method
     */
    private void startMethodWrite() {
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "publish",//
                "(Ljava/io/Writer;Ljava/util/Map;Lorg/zoeey/ztpl/Ztpl;)V", //
                null, new String[]{"java/io/IOException"});
        mv.visitCode();
        ct = new CompileTracker(ztpl, mv);
    }

    /**
     *
     * @return
     */
    public CompileTracker getCompileTracker() {
        return ct;
    }

    /**
     * 关闭 method 写入
     */
    private void endMethodWrite() {
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(ct.nextVarPos(), ct.nextVarPos());
        mv.visitEnd();
    }

    /**
     * 关闭 class 写入
     */
    private void endClassWrite() {
        cw.visitEnd();
    }

    /**
     * 关闭写入
     */
    public void endWrite() {
        endMethodWrite();
        endClassWrite();
    }

    /**
     * 获取代码
     * @return
     */
    public byte[] getCode() {
        return cw.toByteArray();
    }
}
