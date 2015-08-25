/*
 * MoXie (SysTem128@GMail.Com) 2010-3-16 13:36:23
 * $Id$
 * 
 * Copyright &copy; 2008-2010 Zoeey.Org . All rights are reserved.
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.compiler;

import java.io.File;
import org.zoeey.core.util.ArrayHelper;
import org.zoeey.core.util.EncryptHelper;
import org.zoeey.core.util.FileHelper;
import org.zoeey.core.util.StringHelper;

/**
 *
 * @author MoXie
 */
class CompileHelper {

    /**
     * 获取可定义为包或类名称的字符串
     * @param str
     * @return
     */
    public static String genIdentifier(String prefix, String str) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(prefix);
        str = FileHelper.backToslash(str);
        if (str.indexOf("/") > -1) {
            String[] strs = StringHelper.split(str, '/');
            str = ArrayHelper.join(ArrayHelper.reverse(strs), "/");
            strs = null;
        }
        char ch = Character.UNASSIGNED;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (i > 200) {
                break;
            }
            ch = str.charAt(i);
            if (Character.isJavaIdentifierPart(ch)) {
                strBuilder.append(ch);
            } else {
                strBuilder.append("_");
            }
        }
        return strBuilder.toString();
    }

    /**
     * 获取可定义为包或类名称的字符串
     * @param tplFile
     * @return
     */
    public static String genClassName(String prefix, File tplFile) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(prefix);
        String str = tplFile.getName();
        char ch = Character.UNASSIGNED;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (i > 50) {
                break;
            }
            ch = str.charAt(i);
            if (Character.isJavaIdentifierPart(ch)) {
                strBuilder.append(ch);
            } else {
                strBuilder.append("_");
            }
        }
        strBuilder.append(EncryptHelper.md5(tplFile.getAbsolutePath()));
        return strBuilder.toString();
    }
}
