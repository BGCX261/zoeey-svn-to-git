/*
 * MoXie (SysTem128@GMail.Com) 2009-3-20 8:06:27
 */
package org.zoeey.core.util.combiner.js;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.zoeey.core.common.TestUtil;
import static org.junit.Assert.*;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class JsConfigParserTest {

    /**
     *
     */
    public JsConfigParserTest() {
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
     * Test of newParser method, of class JsConfigParser.
     * @throws IOException 
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws JsFileException
     */
    @Test
    public void testNewParser() throws IOException, SAXException,
            ParserConfigurationException, JsFileException {
        System.out.println("newParser");
        JsConfigParser parser = new JsConfigParser();
        String filePath = TestUtil.getResource("org/zoeey/core/util/combiner/js/JsContainer.xml");
        parser.parse(new File(filePath));
        JsContext context = parser.getContext();
        /**
         * 根路径
         */
        assertEquals(context.getFileRoot(), "d:/webRoot/scripts/");
        StringBuffer strBuffer = new StringBuffer();
        for (JsFile js : context.getSafeRootList()) {
            strBuffer.append(js.getFilePath());
            strBuffer.append(',');
        }

        assertEquals(strBuffer.toString(), "d:/somedir/webRoot/special/script/edit.js" +
                ",/webRoot/scripts/,d:/webRoot/scripts/webRootStatic/script/" +
                ",e:/someotherdir/webRoot/special/script/edit.js" +
                ",/webRoot/scripts_sec/,e:/staticRoot/webRootStatic/script/,");
        /**
         * 强制加载的文件
         */
        strBuffer = new StringBuffer();
        for (JsFile js : context.getForceFileList()) {
            strBuffer.append(js.getFilePath());
            strBuffer.append(',');
        }
        assertEquals(strBuffer.toString(), "d:/webRoot/scripts/common/debuger.js," +
                "d:/webRoot/scripts/common/jquery.js," +
                "d:/webRoot/scripts/common/main.lib.js," +
                "/webRootStatic/script/common/some.lib.js," +
                "/webRootStatic/script_sec/common/jquery.1.2.3.4.5.6.js," +
                "/webRootStatic/script_sec/common/main.1.2.3.4.5.6.lib.js," +
                "/webRootStatic/script_sec/common/some.1.2.3.4.5.6.lib.js,");
        /**
         * 跳转的路径
         */
        strBuffer = new StringBuffer();
        for (JsFile js : context.getRedirectList().values()) {
            strBuffer.append(js.getName());
            strBuffer.append(js.getFilePath());
            strBuffer.append(',');
        }
//        assertEquals(strBuffer.toString(), "cross_yh_weather" +
//                "http://someapi.com/weather.jsp,my_weatherd:/webRoot/scripts/weather.jsp,");
        /**
         * 单独的文件
         */
        strBuffer = new StringBuffer();
        for (JsFile js : context.getSingleFileMap().values()) {
            strBuffer.append(js.getName());
            strBuffer.append(js.getFilePath());
            strBuffer.append(',');
        }
        assertEquals(strBuffer.toString(), "specialPage_nationalDay/webRoot/special/script/edit.js" +
                ",articleEdit/webRoot/js/article/edit.js" +
                ",articleCom_2_0/webRoot/Script/edition2.0/article/common.js" +
                ",articleComd:/webRoot/scripts/article/common.js" +
                ",articleEdit_2_0/webRoot/Script/edition2.0/article/edit.js,");
        /**
         * 组文件
         */
        strBuffer = new StringBuffer();
        Iterator<Entry<String, List<JsFile>>> iterator = context.getGroupFileMap().entrySet().iterator();
        Entry<String, List<JsFile>> entry;
        List<JsFile> jsList;
        strBuffer.append('[');
        while (iterator.hasNext()) {
            entry = iterator.next();
            strBuffer.append('{');
            strBuffer.append(entry.getKey());
            strBuffer.append(':');
            jsList = entry.getValue();
            for (JsFile js : jsList) {
                strBuffer.append('{');
                strBuffer.append(js.getName());
                strBuffer.append(':');
                strBuffer.append(js.getFilePath());
                strBuffer.append('}');
            }
            strBuffer.append("},");
        }
        strBuffer.append(']');
        assertEquals(strBuffer.toString(), "[{article.edit:{null:d:/webRoot/scripts/article/common.js}" +
                "{null:d:/webRoot/scripts/article/edit.js}}" +
                ",{customer.default.article.edit:{null:{myDir}default/article/common.js}{null:ImSpecial/edit.js}}" +
                ",{manage.article.edit:{null:article/common.js}{articleEidtorJs:d:/webRoot/scripts/article/edit.js}},]");
        /**
         * 所有命名文件
         */
        strBuffer = new StringBuffer();
        Iterator<Entry<String, JsFile>> iteratorNamed = context.getNamedFileMap().entrySet().iterator();
        Entry<String, JsFile> entryNamed;
        while (iteratorNamed.hasNext()) {
            entryNamed = iteratorNamed.next();
            strBuffer.append(entryNamed.getKey());
            strBuffer.append(':');
            strBuffer.append(entryNamed.getValue().getFilePath());
        }
        assertEquals(strBuffer.toString(), "articleEdit:/webRoot/js/article/edit.jsarticleEidtorJs:d:/webRoot/scripts/article/edit.js" +
                "articleCom:d:/webRoot/scripts/article/common.js" +
                "specialPage_nationalDay:/webRoot/" +
                "special/script/edit.js" +
                "articleCom_2_0:/webRoot/Script/edition2.0/article/common.js" +
                "articleEdit_2_0:/webRoot/Script/edition2.0/article/edit.js");
    }
}
