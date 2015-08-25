/*
 * MoXie (SysTem128@GMail.Com) 2009-4-30 22:09:44
 * $Id: CompileTracker.java 72 2010-01-23 02:16:24Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.compiler;

import java.util.Map;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.zoeey.ztpl.TemplateAble;
import org.zoeey.ztpl.Ztpl;

/**
 * 编译跟踪器
 * @author MoXie(SysTem128@GMail.Com)
 */
public class CompileTracker {

    /**
     * 局部变量索引位置
     * 1、2和3号位置分别被
     * {@link TemplateAble#publish(java.io.Writer, java.util.Map, org.zoeey.ztpl.Ztpl)  publish()}
     * 的三个参数
     * {@link BytecodeHelper#VAR_INDEX_WRITER writer}、
     * {@link BytecodeHelper#VAR_INDEX_PARAMSMAP paramsMap}和
     * {@link BytecodeHelper#VAR_INDEX_ZTPL ztpl} 使用
     */
    private int varPos = 3;
    /**
     * 局部变量索引表
     */
    private Map<Object, Integer> varMap = null;
    private Ztpl ztpl = null;
    /**
     * 字节码辅助类
     */
    private ByteCodeHelper byteCodeHelper = null;
    /**
     * 编译跟踪器
     */
    private MethodVisitor mv = null;

    /**
     *
     * @param ztpl
     * @param mv
     */
    public CompileTracker(Ztpl ztpl, MethodVisitor mv) {
        this.ztpl = ztpl;
        this.mv = mv;
//        varIndexMap = new HashMap<Object, Integer>();
        byteCodeHelper = new ByteCodeHelper(mv);
    }

    /**
     * 下一变量索引位置
     * @return
     */
    public int nextVarPos() {
        varPos++;
        return varPos;
    }

    /**
     * 当前变量索引位置
     * @return
     */
    public int getVarPos() {
        return varPos;
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean isVarPos(Object key) {
        return varMap.containsKey(key);
    }

    /**
     *
     * @param key
     * @return
     */
    public Integer getVarPos(Object key) {
        return varMap.get(key);
    }

    /**
     * 使用Writer写内容
     * @param str 需要输出的字符串
     */
    public void write(String str) {
        byteCodeHelper.startWrite();
        byteCodeHelper.visitObject(str);
        byteCodeHelper.endWrite();
    }

    /**
     * 使用Writer写出变量
     * @param key 变量名
     */
    public void writeVar(String key) {
        byteCodeHelper.startWrite();
        byteCodeHelper.get(key);
        byteCodeHelper.endWrite();
    }

    /**
     * 当变量不为null时使用Writer写出变量
     * @param key
     */
    public void writeVarJugeNull(String key) {
        // if ($key != null)
        byteCodeHelper.get(key);
        Label l0 = new Label();
        mv.visitJumpInsn(Opcodes.IFNULL, l0);
        writeVar(key);
        // end if
        mv.visitLabel(l0);
    }

    /**
     * 将变量放入资源表({@link BytecodeHelper#VAR_INDEX_PARAMSMAP})
     * @param key
     * @param value
     */
    public void put(Object key, Object value) {
        byteCodeHelper.loadVar(ByteCodeHelper.VAR_INDEX_PARAMSMAP);
        byteCodeHelper.visitObject(key);
        byteCodeHelper.visitObject(value);
        byteCodeHelper.doPut();
    }

    /**
     * 新建局部参数表，当前局部变量索引前进一位<br />
     * 对应代码： var paramsMap_* = new ParamsMap();<br />
     * ex. {echo varA="a" varB="b"} <br />
     * 这里创建的变量将存放 map{"varA":"a","varB":"b"}
     *
     */
    public int newLocalMap() {
        byteCodeHelper.newMap(nextVarPos());//
        return varPos;
    }

    /**
     * 新的局部变量，当前局部变量索引前进一位<br />
     * @param obj 变量值
     * @return  新建局部变量的索引
     */
    public int newLocal(Object obj) {
        byteCodeHelper.visitObject(obj);
        byteCodeHelper.storeObj(nextVarPos());
        return varPos;
    }

    /**
     * 向局部变量（ParamsMap）中设定值
     * 对应代码：paramsMap.put(key,value);
     * @param pos  ParamsMap位置
     * @param key  键
     * @param valuePos  值，变量索引
     */
    public void localMapPut(int pos, String key, int valuePos) {
        byteCodeHelper.loadVar(pos);
        byteCodeHelper.visitObject(key);
        byteCodeHelper.loadVar(valuePos);
        byteCodeHelper.doPut();
    }

    /**
     * 新建StringBuilder，当前局部变量索引前进一位<br />
     * 对应代码： var strBuilder_* = new StringBuilder();
     * @return 新建StringBuilder的变量索引
     */
    public int newStrBuilder() {
        byteCodeHelper.newStrBuilder(nextVarPos());
        return varPos;
    }

    /**
     * 追加字符串到指定StringBuilder内
     * @param pos   StringBuilder 变量索引
     * @param str   字符串
     */
    public void strBuilderAppendStr(int pos, String str) {
        byteCodeHelper.strBuilderAppendStr(pos, str);
    }

    /**
     * 执行指定StringBuilder，ToString操作
     * @param pos StringBuilder 变量索引
     * @return
     */
    public int strBuilderToString(int pos) {
        byteCodeHelper.strBuilderToString(pos);
        return varPos;
    }

    /**
     * 执行编译函数
     * @param className
     * @param paramPos 
     */
    public void executeFunction(String className, int paramPos) {
        byteCodeHelper.executeFunction(className, paramPos, nextVarPos());
    }
}
