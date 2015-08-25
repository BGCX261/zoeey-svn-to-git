/*
 * MoXie (SysTem128@GMail.Com) 2009-9-7 11:43:28
 * $Id$
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.zoeey.ztpl.compiler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zoeey.core.common.TestUtil;
import org.zoeey.core.util.TextFileHelper;
import org.zoeey.ztpl.Ztpl;
import org.zoeey.ztpl.ZtplConfig;

/**
 *
 * @author MoXie
 */
public class FunctionStatementTest {

    /**
     *
     */
    public FunctionStatementTest() {
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
     * Test of getName method, of class FunctionStatement.
     * @throws IOException
     */
    @Test
    public void testGetName() throws IOException {
        System.out.println("getName");
//        StringBuilder strBuilder = new StringBuilder();
//        String str = "字符串1";
//        for (int i = 0; i < str.length(); i++) {
//            strBuilder.append(str.charAt(i));
//        }
//        System.out.println(strBuilder.toString());
//        if (true) {
//            return;
//        }
        ZtplConfig config = new ZtplConfig();
        String samplesDir = TestUtil.getResource("org/zoeey/ztpl/samples");
        config.setCacheDir(samplesDir.concat("/cacahe/"));
        config.setCompileDir(samplesDir.concat("/compile/"));
        config.setIsCheckModify(true);
        config.setCharset("UTF-8");
        config.setDelimiter("<!--{", "}-->");
        /**
         *
         */
        File json_encode_file = new File(samplesDir.concat("/json_encode_file.tpl.html"));
        TextFileHelper.write(json_encode_file, "1234567abc<!--{json_encode " //                + "var='StringA' "
                + "var2 var3=123 "
                + "var4=\"StringB\" "
                + "var5 =  1+3+2+1*5/2%3>0 "
                + "var6 =  \"123\"+321+'abc'>3+2 "
                + "var7 =  \"12e3\"+\"space[    ]\"+'abc'>3+2 "
                + "var8   "
                + "var9 = " + "}-->7654321cba");
        Ztpl tpl = new Ztpl(config);
        tpl.setCacheTime(0);
        tpl.setCacheKey("ztpl_");
        tpl.setTplFile(json_encode_file);
        StringWriter strWriter = new StringWriter();
        tpl.fetch(strWriter);
        System.out.println(strWriter.toString());

    }

    /**
     * Test of getParams method, of class FunctionStatement.
     */
    @Test
    public void testGetParams() {
        System.out.println("getParams");
//        FunctionStatement instance = null;
//        ParamsMap expResult = null;
//        ParamsMap result = instance.getParams();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}
