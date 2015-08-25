/*
 * MoXie (SysTem128@GMail.Com) 2009-3-22 0:04:18
 * $Id: JsLoaderTest.java 79 2010-05-14 09:24:37Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.util.combiner.js;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.zoeey.core.common.TestUtil;
import org.zoeey.core.util.FileHelper;
import org.zoeey.core.util.TextFileHelper;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class JsLoaderTest extends TestCase {

    /**
     *
     */
    public JsLoaderTest() {
    }
    /**
     * Test of load method, of class JsLoader.
     */
    private static String fileRoot = TestUtil.getResourceDir().concat("/jsLoader");

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
     *
     * @throws IOException
     * @throws JsFileException
     * @throws InterruptedException
     */
    @Test
    public void testLoad() throws IOException, JsFileException, InterruptedException {
        JsLoader jsLoader = new JsLoader();
        jsLoader.setIsDebug(true);
        jsLoader.setCacheDirRoot(fileRoot.concat("/test"));
        JsContext context = new JsContext();
        String _jsRoot = fileRoot + "/script";
        TextFileHelper.write(new File(_jsRoot.concat("/force.js")), "file_force.js");
        TextFileHelper.write(new File(_jsRoot.concat("/single_1.js")), "file_single_1.js");
        TextFileHelper.write(new File(_jsRoot.concat("/single_2.js")), "file_single_2.js");
        TextFileHelper.write(new File(_jsRoot.concat("/group_1.js")), "file_group_1.js");
        TextFileHelper.write(new File(_jsRoot.concat("/group_2.js")), "file_group_2.js");
        TextFileHelper.write(new File(_jsRoot.concat("/group_3.js")), "file_group_3.js");

        context.setFileRoot(_jsRoot);
        // force
        context.addForceFile(new JsFile("/force.js").setFileRoot(_jsRoot));
        context.addForceFile(new JsFile("/force.js").setFileRoot(_jsRoot));
        // namedfile
        jsLoader.merge("single_2"); // 在combinText前即可
        jsLoader.merge("single_1"); // 在combinText前即可
        context.addNamedFile(new JsFile("/single_1.js").setFileRoot(_jsRoot).setName("single_1"));
        context.addNamedFile(new JsFile("/single_2.js").setFileRoot(_jsRoot).setName("single_2"));

        // group_1 : ["group_1.js","group_2.js"]
        // group_2 : ["group_3.js","group_1.js"]
        context.addGroupFile("group_1", new JsFile("/group_1.js").setFileRoot(_jsRoot));
        context.addGroupFile("group_1", new JsFile("/group_2.js").setFileRoot(_jsRoot));
        context.addGroupFile("group_2", new JsFile("/group_3.js").setFileRoot(_jsRoot));
        context.addGroupFile("group_2", new JsFile("/group_2.js").setFileRoot(_jsRoot));

        jsLoader.merge(new String[]{"group_1", "group_2"});
        jsLoader.setContext(context);
        StringWriter strWriter = new StringWriter();
        jsLoader.display(strWriter, -1);
        jsLoader.cleanCache("/");
        String combinedJs = strWriter.toString();

        assertTrue(combinedJs.indexOf("group_2") > -1);
        assertTrue(combinedJs.indexOf("group_1") > -1);
        if (combinedJs.indexOf("file_single_2") > combinedJs.indexOf("file_single_1")
                || combinedJs.indexOf("file_group_１") > combinedJs.indexOf("file_group_2")) {
            fail("加载顺序不对！");
        }

    }

    /**
     *
     * @throws IOException
     * @throws JsFileException
     * @throws InterruptedException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    @Test
    public void testLoadFromFile() throws IOException, JsFileException, InterruptedException, SAXException, ParserConfigurationException {
        JsLoader jsLoader = new JsLoader();
        JsContext context = new JsContext();
        File configFile = new File(fileRoot.concat("/JsContainer.xml"));
        FileHelper.copy(new File(TestUtil.getResource("org/zoeey/core/util/combiner/js/JsContainer.xml").replace("build/classes", "src")), configFile);
        context = JsLoader.loadConfig(configFile);
        context.setFileRoot(fileRoot);
        //
        jsLoader.merge("articleCom_2_0");
        jsLoader.merge("article.edit");
        jsLoader.setCacheDirRoot(fileRoot + "/script/cacahe/");
        jsLoader.setContext(context);
        /**
         * generate js files
         */
//        TextFileHelper.write(new File(fileRoot.concat("/script/common/debuger.js")), "/* debuger.js */");
//        TextFileHelper.write(new File(fileRoot.concat("/script/common/jquery.js")), "/* jquery.js */");
//        TextFileHelper.write(new File(fileRoot.concat("/script/common/main.lib.js")), "/* main.lib.js */");
//        TextFileHelper.write(new File(fileRoot.concat("/script/common/jquery.1.2.3.4.5.6.js")), "/* jquery.1.2.3.4.5.6.js */");
//        TextFileHelper.write(new File(fileRoot.concat("/script/common/main.1.2.3.4.5.6.lib.js")), "/* main.1.2.3.4.5.6.lib.js */");
//        TextFileHelper.write(new File(fileRoot.concat("/script/common/jquery.1.2.3.4.5.6.js")), "/* jquery.1.2.3.4.5.6.js */");
//        TextFileHelper.write(new File(fileRoot.concat("/script/common/some.1.2.3.4.5.6.lib.js")), "/* some.1.2.3.4.5.6.lib.js */");
//        TextFileHelper.write(new File(fileRoot.concat("/script/webRoot/js/article/edit.js")), "/* webRoot/js/article/edit.js */");
//        TextFileHelper.write(new File(fileRoot.concat("/script/article/edit.js")), "/* article/edit.js */");
//        TextFileHelper.write(new File(fileRoot.concat("/script/article/common.js")), "/* article/common.js */");
//        TextFileHelper.write(new File(fileRoot.concat("/script/webRoot/Script/edition2.0/article/common.js")), //
//                "/* webRoot/Script/edition2.0/article/common.js */");
        String str = TextFileHelper.read(jsLoader.fetch(1));
//        System.out.println(JsonHelper.encode(jsLoader.getValiedNameList()));
//        System.out.println(str);
        assertTrue(str.indexOf("article/common.js") > -1 //
                && str.indexOf("article/common.js") > -1 //
                && str.indexOf("article/edit.js") > -1 //
                );
    }

    /**
     *
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws JsFileException
     * @throws InterruptedException
     */
    @Test
    public void testJsLoader() throws SAXException, ParserConfigurationException, IOException, JsFileException, InterruptedException {
        String cacheDir = fileRoot;
        String configFile = cacheDir + "/JsContainer.xml";
        //
        JsLoader jsLoader = new JsLoader();
        // 缓存在哪里？
        jsLoader.setCacheDirRoot(cacheDir);
        // 设置文件是什么？
        jsLoader.setConfig(new File(configFile));
        // 是否显示调试信息
        jsLoader.setIsDebug(true);
        // 除过强制加载文件外还想加载什么
        jsLoader.merge("articleCom_2_0");
        jsLoader.merge("article.edit");
        //
        StringWriter out = new StringWriter();
        // 显示出来，注意超时时间
        jsLoader.display(out, 3);
        assertTrue(out.toString().indexOf("article/common.js") > -1 //
                && out.toString().indexOf("article/common.js") > -1 //
                && out.toString().indexOf("article/edit.js") > -1 //
                );
        out.flush();
        out.close();

    }
}
