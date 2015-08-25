/*
 * MoXie (SysTem128@GMail.Com) 2009-8-6 11:34:01
 * $Id: Mapping.java 70 2010-01-02 20:08:07Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由匹配模式
 * @author MoXie
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Mapping {

    /**
     * 匹配模式
     * @return
     */
    public String[] pattern() default {};
}
