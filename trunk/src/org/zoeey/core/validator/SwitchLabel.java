/*
 * MoXie (SysTem128@GMail.Com) 2009-7-27 10:56:51
 * $Id: SwitchLabel.java 69 2009-12-15 12:25:04Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.validator;

/**
 * 验证器结合类型
 * @author MoXie
 */
public enum SwitchLabel {

    /**
     * 空字段断言
     */
    ALLOWNULL_ASSERT,
    /**
     * 断言
     * 验证失败后直接跳出
     */
    ASSERT,
    /**
     * 连接
     * 与其他证器合并使用
     */
    JOIN
}
