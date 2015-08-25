/*
 * MoXie (SysTem128@GMail.Com) 2009-8-17 14:26:06
 * $Id: RouterConnectException.java 68 2009-11-01 07:56:13Z MoXie $
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
public class RouterConnectException extends Exception {

    /**
     * Creates a new instance of <code>RouterConnectError</code> without detail message.
     */
    public RouterConnectException() {
    }

    /**
     * Constructs an instance of <code>RouterConnectError</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RouterConnectException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>RouterConnectError</code> with the specified detail message.
     * @param msg the detail message.
     * @param route 
     */
    public RouterConnectException(String msg, String route) {
        super(msg + route);
    }

    /**
     * Constructs an instance of <code>RouterConnectError</code> with the specified detail message.
     * @param msg the detail message.
     * @param route 
     * @param cause
     */
    public RouterConnectException(String msg, String route, Throwable cause) {
        super(msg + route, cause);
    }
}
