/*
 * MoXie (SysTem128@GMail.Com) 2009-5-2 19:55:34
 * $Id: ByteCodeHelper.java 72 2010-01-23 02:16:24Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.compiler;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.zoeey.core.common.ZObject;

/**
 * 字节码写入辅助
 * @author MoXie(SysTem128@GMail.Com)
 */
public class ByteCodeHelper {

    /**
     * Writer 的变量位置
     */
    public static final int VAR_INDEX_WRITER = 1;
    /**
     * ParamsMap 的变量位置
     */
    public static final int VAR_INDEX_PARAMSMAP = 2;
    /**
     * Ztpl 的变量位置
     */
    public static final int VAR_INDEX_ZTPL = 3;
    /**
     *  方法访问者
     */
    private MethodVisitor mv = null;

    /**
     * 字节码辅助工具
     * @param mv 
     */
    public ByteCodeHelper(MethodVisitor mv) {
        this.mv = mv;
    }

    /**
     * 准备写入
     */
    public void startWrite() {
        mv.visitVarInsn(Opcodes.ALOAD, VAR_INDEX_WRITER);
    }

    /**
     * 完成写入
     */
    public void endWrite() {
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/Writer", "append",//
                "(Ljava/lang/CharSequence;)Ljava/io/Writer;");
        mv.visitInsn(Opcodes.POP);
    }

    /**
     * 取出变量
     * @param pos ParamMap变量索引
     */
    public void loadVar(int pos) {
        mv.visitVarInsn(Opcodes.ALOAD, pos);
    }

    /**
     * 执行向ParamMap添加值 <b>需要两个参数</b>
     */
    public void doPut() {
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        mv.visitInsn(Opcodes.POP);
    }

    /**
     * 获取资源表内的变量
     * @param key 键
     */
    public void get(String key) {
        mv.visitVarInsn(Opcodes.ALOAD, VAR_INDEX_PARAMSMAP);
        visitObject(key);
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
    }

    /**
     * 访问对象
     * @param obj
     * @return
     */
    public ByteCodeHelper visitObject(Object obj) {
        if (obj == null) {
            mv.visitInsn(Opcodes.ACONST_NULL);
        } else {
            @SuppressWarnings("unchecked")
            Class<Object> clazz = (Class<Object>) obj.getClass();
            if (int.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz)) {
                visitInt(new ZObject(obj).toInteger());
            } else if (double.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)) {
                visitDouble(new ZObject(obj).toDouble());
            } else if (Boolean.class.isAssignableFrom(clazz)) {
                visitBoolean(new ZObject(obj).toBoolean());
            } else {
                mv.visitLdcInsn(obj);
            }
        }
        return this;
    }

    /**
     * 访问布尔
     * @param bool
     * @return
     */
    private ByteCodeHelper visitBoolean(boolean bool) {
        if (bool) {
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
        } else {
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
        }
        return this;
    }

    private ByteCodeHelper visitDouble(double db) {
        mv.visitLdcInsn(db);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
        return this;
    }

    /**
     * 访问数字
     * @param num
     * @return
     */
    private ByteCodeHelper visitInt(int num) {
        /**
         * 访问数字
         * @param num
         */
        int label = 0;
        switch (num) {
            case -1:
                mv.visitInsn(Opcodes.ICONST_M1);
                break;
            case 0:
                mv.visitInsn(Opcodes.ICONST_0);
                break;
            case 1:
                mv.visitInsn(Opcodes.ICONST_1);
                break;
            case 2:
                mv.visitInsn(Opcodes.ICONST_2);
                break;
            case 3:
                mv.visitInsn(Opcodes.ICONST_3);
                break;
            case 4:
                mv.visitInsn(Opcodes.ICONST_4);
                break;
            case 5:
                mv.visitInsn(Opcodes.ICONST_5);
                break;
            default:
                if (num <= Byte.MAX_VALUE && num >= Byte.MIN_VALUE) {
                    mv.visitIntInsn(Opcodes.BIPUSH, num);
                } else if (num <= Short.MAX_VALUE && num >= Short.MIN_VALUE) {
                    mv.visitIntInsn(Opcodes.SIPUSH, num);
                } else if (num <= Integer.MAX_VALUE && num >= Integer.MIN_VALUE) {
                    mv.visitLdcInsn(num);
                    /**
                     *  以下不会发生
                     */
                } else if (num <= Long.MAX_VALUE && num >= Long.MIN_VALUE) {
                    mv.visitLdcInsn(num);
                    label = 1;
                } else {
                    // todo throw something
                }
        }
        if (label == 1) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
        } else {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        }
        return this;
    }

    /**
     * 新建局部参数表 <br />
     * 对应代码：var paramsMap_{*} = new ParamsMap() <br />
     * ex. {echo varA="a" varB="b"} <br />
     * 这里创建的变量将存放 map{"varA":"a","varB":"b"}
     * @param pos  存放位置
     * @return  当前辅助对象
     */
    public ByteCodeHelper newMap(int pos) {
        mv.visitTypeInsn(Opcodes.NEW, "org/zoeey/ztpl/ParamsMap");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/zoeey/ztpl/ParamsMap", "<init>", "()V");
        mv.visitVarInsn(Opcodes.ASTORE, pos);
        return this;
    }

    /**
     * 新建StringBuilder
     * @param pos StringBuilder变量索引
     * @return
     */
    public ByteCodeHelper newStrBuilder(int pos) {
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V");
        mv.visitVarInsn(Opcodes.ASTORE, pos);
        return this;
    }

    /**
     * 向StringBuidler中追加字符串
     * @param builderPos    StringBuilder变量索引
     * @param str   
     * @return
     */
    public ByteCodeHelper strBuilderAppendStr(int builderPos, String str) {
        mv.visitVarInsn(Opcodes.ALOAD, builderPos);
        mv.visitLdcInsn(str);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
        mv.visitInsn(Opcodes.POP);
        return this;
    }

    /**
     * StringBuilder#toString()
     * @param builderPos
     * @param var
     * @return
     */
    public ByteCodeHelper strBuilderAppendVar(int builderPos, String var) {
        if (var != null) {
            mv.visitVarInsn(Opcodes.ALOAD, builderPos);
            mv.visitVarInsn(Opcodes.ALOAD, VAR_INDEX_PARAMSMAP);
            mv.visitLdcInsn(var);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
            mv.visitInsn(Opcodes.POP);
        }
        return this;
    }

    /**
     * StringBuilder#toString()
     * @param pos
     * @return 
     */
    public ByteCodeHelper strBuilderToString(int pos) {
        mv.visitVarInsn(Opcodes.ALOAD, pos);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitInsn(Opcodes.POP);
        return this;
    }

    /**
     * 新建函数对象
     * @param clazz
     * @param mapVar
     * @param pos
     */
    public void executeFunction(String clazz, int mapVar, int pos) {
        String classPath = clazz.replace('.', '/');
        startWrite();
        /**
         * 传键对象并初始化
         */
        mv.visitTypeInsn(Opcodes.NEW, classPath);
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, classPath, "<init>", "()V");
        mv.visitVarInsn(Opcodes.ALOAD, mapVar);
        mv.visitVarInsn(Opcodes.ALOAD, VAR_INDEX_ZTPL);
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "org/zoeey/ztpl/FunctionAble",//
                "registerFunction", //
                "(Lorg/zoeey/ztpl/ParamsMap;Lorg/zoeey/ztpl/Ztpl;)Ljava/lang/String;");
        endWrite();
    }
//    /**
//     * 新建函数对象
//     * @param clazz
//     * @param mapVar
//     * @param var
//     */
//    public void executeBlockStart(String clazz, int mapVar, int var) {
//        String classPath = clazz.replace('.', '/');
//        startWrite();
//        /**
//         * 传键对象并初始化
//         */
//        mv.visitTypeInsn(Opcodes.NEW, classPath);
//        mv.visitInsn(Opcodes.DUP);
//        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, classPath, "<init>", "()V");
//        mv.visitVarInsn(Opcodes.ASTORE, var);
//        //
//        mv.visitVarInsn(Opcodes.ALOAD, var);
//        mv.visitVarInsn(Opcodes.ALOAD, mapVar);
//        mv.visitVarInsn(Opcodes.ALOAD, VAR_INDEX_ZTPL);
//        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "org/zoeey/ztpl/FunctionAble",//
//                "registerFunction", //
//                "(Lorg/zoeey/ztpl/ParamsMap;Lorg/zoeey/ztpl/Ztpl;)Ljava/lang/String;");
//        endWrite();
//    }

    public ByteCodeHelper storeObj(int pos) {
        mv.visitVarInsn(Opcodes.ASTORE, pos);
        return this;
    }
}
