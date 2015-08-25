/*
 * MoXie (SysTem128@GMail.Com) 2009-8-6 15:04:05
 * $Id: RouterParserException.java 68 2009-11-01 07:56:13Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route.exceptions;

/**
 *
 * @author MoXie
 */
public class RouterParserException extends Exception {


    /**
     * Creates a new instance of <code>NoRouterEntyClassException</code> without detail message.
     */
    public RouterParserException() {
    }

    /**
     * Constructs an instance of <code>NoRouterEntyClassException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RouterParserException(String msg) {
        super("${" + msg + "}");
    }

    /**
     *
     * @param msg
     * @param className
     */
    public RouterParserException(String msg, String className) {
        super("${" + msg + "}" + className);
    }
}
