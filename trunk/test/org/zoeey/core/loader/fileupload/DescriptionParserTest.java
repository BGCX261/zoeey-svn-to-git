/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: DescriptionParserTest.java 67 2009-09-05 18:25:37Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.loader.fileupload;

import java.util.Map;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zoeey.core.util.JsonHelper;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class DescriptionParserTest extends TestCase {

    /**
     *
     */
    public DescriptionParserTest() {
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
     * Test of getParam method, of class StringParser.
     */
    @Test
    public void testGetParam() {
        System.out.println("getParam");
        String source = null;
        Map paramMap = null;
        /**
         * 
         */
        source = "Content-Disposition: form-data; name=\"cFile\"; filename=\"\"\n" +
                "Content-Type: application/octet-stream";
        paramMap = DescriptionParser.getParam(source, true);
        assertEquals(JsonHelper.encode(paramMap), "{\"content-disposition\":\"form-data;\"," +
                "\"content-type\":\"application\\/octet-stream\",\"filename\":\"\",\"name\":\"cFile\"}");
        /**
         *
         */
        source = "Content-Disposition: form-data; name=\"cFile\"; filename=\"bug.png\"\n" +
                "Content-Type: application/octet-stream";
        paramMap = DescriptionParser.getParam(source, true);
        assertEquals(JsonHelper.encode(paramMap),
                "{\"content-disposition\":\"form-data;\",\"content" +
                "-type\":\"application\\/octet-stream\",\"filename\":\"bug.png\",\"name\":\"cFile\"}");

        /**
         *
         */
        source = "this is a string im=\"M\\oX\\\"ie\" im2=\"MoX\\\"ie\" " +
                "file = \"bug.png\" " +
                "name='haha' sex:male file2 = \"bug2.png\" " +
                "name2='haha2' ";
        paramMap = DescriptionParser.getParam(source, true);
        assertEquals(JsonHelper.encode(paramMap), "{\"sex\":\"male\"," +
                "\"im\":\"M\\\\oX\\\"ie\",\"name2\":\"haha2\"," +
                "\"file\":\"bug.png\",\"file2\":\"bug2.png\"," +
                "\"name\":\"haha\",\"im2\":\"MoX\\\"ie\"}");
        /**
         *
         */
        source = "Content-Disposition: form-data; name=\"bugFile\"; filename=\"test\\\\resource\\\\bug.png\"Content-Type: image/png ";
        paramMap = DescriptionParser.getParam(source, true);
        assertEquals(JsonHelper.encode(paramMap), "{\"content-disposition\":\"form-data;\"," +
                "\"content-type\":\"image\\/png\"," +
                "\"filename\":\"test\\\\resource\\\\bug.png\"," +
                "\"name\":\"bugFile\"}");
        /**
         * 
         */
        source = "Content-Disposition: form-data; name=\"bugFile\"; filename=\"test\\\\resource\\\\bug.png\"Content-Type: image/png abc";
        paramMap = DescriptionParser.getParam(source, true);
        assertEquals(JsonHelper.encode(paramMap), "{\"content-disposition\":\"form-data;\"," +
                "\"content-type\":\"image\\/png\"," +
                "\"filename\":\"test\\\\resource\\\\bug.png\"," +
                "\"name\":\"bugFile\"}");


    }
}

