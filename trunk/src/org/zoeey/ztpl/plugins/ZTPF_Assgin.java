/*
 * MoXie (SysTem128@GMail.Com) 2009-5-3 21:09:04
 * $Id: ZTPF_Assgin.java 67 2009-09-05 18:25:37Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.plugins;

import org.zoeey.ztpl.FunctionAble;
import org.zoeey.ztpl.ParamsMap;
import org.zoeey.ztpl.Ztpl;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class ZTPF_Assgin implements FunctionAble {

    public String registerFunction(ParamsMap params, Ztpl tpl) {
        tpl.assign(params.getZObject("key").toString(),params.get("value"));
        return "";
    }
}
