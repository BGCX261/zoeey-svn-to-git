/*
 * MoXie (SysTem128@GMail.Com) 2009-8-21 17:12:14
 * $Id: CompilerTest.java 71 2010-01-07 05:49:47Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.compiler;

import org.zoeey.ztpl.*;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zoeey.core.common.TestUtil;
import org.zoeey.core.util.DirInfo;

/**
 *
 * @author MoXie
 */
public class CompilerTest {

    /**
     *
     */
    public CompilerTest() {
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
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class Compiler.
     * @throws Exception
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");

        File file = new File(TestUtil.getResource("org/zoeey/ztpl/samples/article.tpl.html"));
//        Compiler cp = new Compiler(new ZtplConfig.Delimiter("<!--{", "}-->"));
    }
}
