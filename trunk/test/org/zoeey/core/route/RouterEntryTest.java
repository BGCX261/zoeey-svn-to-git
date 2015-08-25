/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: RouterEntryTest.java 62 2009-07-17 09:49:09Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import junit.framework.TestCase;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class RouterEntryTest extends TestCase {

    /**
     *
     * @param testName
     */
    public RouterEntryTest(String testName) {
        super(testName);
    }

    /**
     * Test of getClazz method, of class RouterEntry.
     */
    public void testGetClazz() {

        // publisher
        System.out.println("publisher");
        RouterEntry entry = RouterEntry.newPublishEntry(new ArticlePublish(), "/article");
        System.out.println(entry.getLength());
        // initor
        System.out.println("initor");
        entry = RouterEntry.newInitEntry(new ArticlePublish());
        // publisher no found
        System.out.println("publisher no found");
        entry = RouterEntry.newInitEntry(new ArticlePublish());
        System.out.println(entry.getLength());


    }
}
