/*
 * MoXie (SysTem128@GMail.Com) 2009-4-30 22:21:59
 * $Id: ZTPM_Capitalize.java 72 2010-01-23 02:16:24Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.plugins;

import org.zoeey.core.common.ZObject;
import org.zoeey.core.util.StringHelper;
import org.zoeey.ztpl.ModifierAble;
import org.zoeey.ztpl.ParamsMap;
import org.zoeey.ztpl.Ztpl;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class ZTPM_Capitalize implements ModifierAble {

    /**
     *
     * @param <T>
     * @param val
     * @param params
     * @param tpl
     * @return
     */
    public Object registerModifier(Object val, ParamsMap params, Ztpl tpl) {
        return StringHelper.firstToUpperCase(ZObject.conv(val).toString());
    }
}
