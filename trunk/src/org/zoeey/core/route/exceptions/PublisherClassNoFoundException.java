/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: PublisherClassNoFoundException.java 68 2009-11-01 07:56:13Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route.exceptions;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class PublisherClassNoFoundException extends Exception {


    /**
     * Creates a new instance of <code>PublishClassNoFoundException</code> without detail message.
     */
    public PublisherClassNoFoundException() {
    }

    /**
     * Constructs an instance of <code>PublishClassNoFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public PublisherClassNoFoundException(String msg) {
        super("${" + msg + "}");
    }

    /**
     *
     * @param msg
     * @param className
     */
    public PublisherClassNoFoundException(String msg, String className) {
        super("${" + msg + "}" + className);
    }
}
