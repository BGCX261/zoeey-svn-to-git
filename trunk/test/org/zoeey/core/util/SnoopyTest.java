/*
 * MoXie (SysTem128@GMail.Com) 2009-3-10 23:42:44
 * $Id: SnoopyTest.java 75 2010-03-16 02:21:41Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zoeey.core.util.Snoopy.RequestHeaders;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class SnoopyTest extends TestCase {

    /**
     *
     */
    public SnoopyTest() {
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
     * Test of getContent method, of class Snoopy.
     * @throws Exception
     */
    @Test
    public void testGetContent_3args() throws Exception {
        System.out.println("getContent_3args");
        /**
         * 参数
         */
        Map<String, String> params = new HashMap<String, String>();
        params.put("test1", "test1");
        params.put("test2", "test2");
        params.put("test22", "test22");
        params.put("中文", "有换行的中文\r\n有换行的中文\r\n");
        /**
         * Google 搜索
         */
        /**
        params.put("aq", "f");
        params.put("btnG", "Google Search");
        params.put("hl", "en");
        params.put("oq", "");
        params.put("q", "zoeey.org");
         */
        /**
         * 请求头
         */
        RequestHeaders headers = new RequestHeaders();
        /**
         * out.println(TextFileHelper.read(request.getInputStream()));
         */
        headers.setReferer("http://zoeey.org/");
        headers.setUserAgent("Mozilla/5.0");
        URL url = new URL("http://zoeey.org/");
//        assertTrue(Snoopy.getContent(url, headers).indexOf("MoXie") > 0);
        Snoopy snoopy = new Snoopy(url);
        snoopy.setHeaders(headers);
        snoopy.setQueryString(UrlBuilder.build(params));

        assertTrue(snoopy.doPost().indexOf("Zoeey Framework") > -1);
        snoopy.setQueryString(UrlBuilder.build(params.entrySet(), "GB2312"));
        assertTrue(snoopy.doPost().indexOf("Zoeey Framework") > -1);
    }

    /**
     * Test of getContent method, of class Snoopy.
     * @throws Exception
     */
    @Test
    public void testGetContent() throws Exception {
        System.out.println("getContent");
        URL url = new URL("http://example.com");
        Snoopy snoopy = new Snoopy(url);
        assertEquals(snoopy.doGet().length(), 438);

    }
}
