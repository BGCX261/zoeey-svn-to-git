/*
 * MoXie (SysTem128@GMail.Com) 2009-8-5 17:04:22
 * $Id: ValiConfig.java 66 2009-08-20 07:30:24Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.validator;

import org.zoeey.core.util.ParamHelper;

/**
 *
 * @author MoXie
 */
public class ValiConfig {

    private ValiConfig() {
    }
    /**
     * 错误时保留传递进来的值
     */
    public static final int RETAIN = 1;
    /**
     * 当验证错误时使用默认值
     */
    public static final int USEDEFAULT = ParamHelper.genParam(1);
}
