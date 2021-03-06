/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: AllowNull.java 65 2009-08-10 06:39:41Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.validator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 允许为空
 * 当允许为空时值如果为null则立即返回true
 * @author MoXie(SysTem128@GMail.Com)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AllowNull {
    
    // **
}
