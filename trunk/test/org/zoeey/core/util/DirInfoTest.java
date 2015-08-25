/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: DirInfoTest.java 68 2009-11-01 07:56:13Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.util;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class DirInfoTest extends TestCase {

    /**
     *
     */
    public DirInfoTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    @Override
    public void setUp() {
    }

    /**
     *
     */
    @After
    @Override
    public void tearDown() {
    }

    /**
     * Test of getDir method, of class DirInfo.
     */
    @Test
    public void testGetClassesDir() {
        System.out.println("getClassesDir");
        String result = DirInfo.getClassesDir();
        assertEquals(true, result.endsWith("classes"));
    }

    /**
     *
     */
    @Test
    public void testGetWebInfoDir() {
        System.out.println("getWebInfoDir");
        String result = DirInfo.getWebInfoDir();
        assertEquals(true, result.endsWith("build") || result.endsWith("WEB-INF"));
    }

    /**
     *
     */
    @Test
    public void testGetClassDir() {
        System.out.println("getClassDir");
        String result = DirInfo.getClassDir(this.getClass());
        assertEquals(true, result.endsWith("/classes/org/zoeey/core/util"));
    }
}
