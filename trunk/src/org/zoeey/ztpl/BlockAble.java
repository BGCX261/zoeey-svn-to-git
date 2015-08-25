/*
 * MoXie (SysTem128@GMail.Com) 2009-5-1 3:09:26
 * $Id: BlockAble.java 72 2010-01-23 02:16:24Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl;

/**
 * 块函数接口
 * @author MoXie(SysTem128@GMail.Com)
 */
public interface BlockAble {

    /**
     * 注册块函数开始
     * @param params  模板参数
     * @param tpl 模板引擎对象
     * @param repeat 循环次数 0、循环初始； 1..n、循环执行； -1、循环结束
     */
    public void registerBlock(ParamsMap  params, Ztpl tpl, boolean repeat);

    /**
     * 是否继续重复
     * @return  
     */
    public boolean isRepeat();
}
