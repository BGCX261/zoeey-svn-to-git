/*
 * MoXie (SysTem128@GMail.Com) 2009-5-3 9:48:18
 * $Id: CompileBase.java 71 2010-01-07 05:49:47Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.compiler;

import org.objectweb.asm.Opcodes;


/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public abstract class CompileBase implements Opcodes {

    /**
     *
     */
    protected CompileBase() {
    }
//    /**
//     *
//     * @param mv
//     * @param str
//     */
//    public void print(MethodVisitor mv, String str) {
//        mv.visitVarInsn(ALOAD, 1);
//        mv.visitLdcInsn(str);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/Writer", "append", "(Ljava/lang/CharSequence;)Ljava/io/Writer;");
//        mv.visitInsn(POP);
//    }
//
//    /**
//     *
//     * @param mv
//     * @param key
//     */
//    public void get(MethodVisitor mv, String key) {
//        mv.visitVarInsn(ALOAD, 2);
//        mv.visitLdcInsn(key);
//        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
//    }
}
