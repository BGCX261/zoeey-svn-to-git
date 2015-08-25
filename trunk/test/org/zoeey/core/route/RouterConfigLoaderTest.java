/*
 * MoXie (SysTem128@GMail.Com) 2009-8-6 16:40:22
 * $Id: RouterConfigLoaderTest.java 79 2010-05-14 09:24:37Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zoeey.core.util.DirInfo;
import org.zoeey.core.util.EncryptHelper;
import org.zoeey.core.util.JsonHelper;

/**
 *
 * @author MoXie
 */
public class RouterConfigLoaderTest extends TestCase {

    /**
     *
     */
    public RouterConfigLoaderTest() {
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
     * Test of load method, of class RouterConfigLoader.
     * @throws Exception
     */
    @Test
    public void testLoad() //
            throws Exception {
        System.out.println("load");
        File configFile = new File(DirInfo.getClassDir(this.getClass()).replaceFirst("build/classes", "src") + "/RouterConfig.xml");
        RouterConfigLoader.load(configFile);
        List<String> list = new ArrayList<String>();
        for (RouterEntry entry : Router.getPublisherList()) {
            list.add("Pattern:" + entry.getPattern() + ", Class:" + entry.getPublish().getClass().getName());
        }
        if (JsonHelper.encode(list).indexOf("org.zoeey.core.route.ArticlePublish") < 0) {
            fail("Class no found");
        }
    }
}
