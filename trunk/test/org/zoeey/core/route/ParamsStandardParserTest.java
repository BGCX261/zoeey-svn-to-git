/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: ParamsStandardParserTest.java 75 2010-03-16 02:21:41Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import junit.framework.TestCase;
import org.zoeey.core.route.exceptions.RouterConnectException;
import org.zoeey.core.route.exceptions.RouterEntryPublisherClassNoFoundException;
import org.zoeey.core.util.JsonHelper;
import org.zoeey.core.util.TimeMeasurer;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class ParamsStandardParserTest
        extends TestCase {

    /**
     *
     * @param testName
     */
    public ParamsStandardParserTest(String testName) {
        super(testName);
    }

    /**
     *
     * @throws RouterEntryPublisherClassNoFoundException
     * @throws RouterConnectException 
     */
    public void testParseParams()
            throws RouterEntryPublisherClassNoFoundException //
            , RouterConnectException {

        RouterRule rule = RouterRule.newRule("/:action")//
                // 正则匹配已出现的变量
                .add("action", "(list)", "/:page/:label")//
                // 已出现变量在某集合内
                .add("action", new String[]{"view"}, "/:id/:title")//
                .add("action", new String[]{"edit", "delete"}, "/:id")//
                // 参数个数是某值
                .endOn(5, "/:id/:page/:label/:highlight")//
                // 全QueryString正则匹配
                .add("/track/([^/]+)/?$", "/:trackSn");
        Map<String, String> resultMap = new HashMap<String, String>();
        ParamsParserAble parser = null;
        Query query = null;
        /**
         * 正则匹配已出现的变量
         */
        parser = new ParamsStandardParser();
        query = new Query(RouterEntry.newPublishEntry(new ArticlePublish(),//
                "/article"), "/article/list/1");
        parser.connect(rule);
        parser.parse(query);
        for (Entry<String,String> entry : parser.getEntryCollection()) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        assertEquals("/:action/:page/:label", parser.getRuleExpression());
        assertEquals(JsonHelper.encode(resultMap), "{\"action\":\"list\",\"page\":\"1\"}");
        /**
         * 已出现变量在某集合内
         */
        parser = new ParamsStandardParser();
        parser.connect(rule);
        query = new Query(RouterEntry.newPublishEntry(new ArticlePublish(),//
                "/article"), "/article/view/1/this_is_your_article_title");
        parser.parse(query);
        resultMap.clear();
        for (Entry<String,String> entry : parser.getEntryCollection()) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        assertEquals("/:action/:id/:title", parser.getRuleExpression());
        assertEquals(JsonHelper.encode(resultMap), "{\"action\":\"view\""
                + ",\"title\":\"this_is_your_article_title\",\"id\":\"1\"}");
        /**
         * 参数个数是某值
         */
        rule = RouterRule.newRule("/:action")//
                // 正则匹配已出现的变量
                .add("action", "(list)", "/:page/:label")//
                // 已出现变量在某集合内
                .add("action", new String[]{"view"}, "/:id")// 注意这里
                .add("action", new String[]{"edit", "delete"}, "/:id")//
                // 参数个数是某值
                .endOn(5, "/:page/:label/:highlight")//
                // 全QueryString正则匹配
                .add("track/([^/]+)/?$", "/:trackSn");
        parser = new ParamsStandardParser();
        parser.connect(rule);
        query = new Query(RouterEntry.newPublishEntry(new ArticlePublish(),//
                "/article"), "/article/view/1/10/3/highlightWords");
        parser.parse(query);
        resultMap.clear();
        for (Entry<String,String> entry : parser.getEntryCollection()) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        assertEquals("/:action/:id/:page/:label/:highlight", parser.getRuleExpression());
        assertEquals(JsonHelper.encode(resultMap),//

                "{\"action\":\"view\",\"label\":\"3\",\"highlight\":\"highlightWords\""
                + ",\"page\":\"10\",\"id\":\"1\"}");
        /**
         * 全QueryString正则匹配
         */
        parser = new ParamsStandardParser();
        parser.connect(rule);
        query = new Query(RouterEntry.newPublishEntry(new ArticlePublish(),//
                "/article"), "/article/track/9c81ff86425cf62acaeb5a07e9f990e1"); // Md5.md5("http://zoeey.org/")
        parser.parse(query);
        resultMap.clear();
        for (Entry<String,String>  entry : parser.getEntryCollection()) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        assertEquals("/:action/:trackSn", parser.getRuleExpression());
        assertEquals(JsonHelper.encode(resultMap), "{\"action\":\"track\""
                + ",\"trackSn\":\"9c81ff86425cf62acaeb5a07e9f990e1\"}");


        /**
         *
         */
        HashMap<String, Object> expResult = new HashMap<String, Object>();
        /**
         * 
         */
        parser = new ParamsStandardParser();
        parser.connect("/:action/:id/:title");
        query = new Query(RouterEntry.newPublishEntry(new ArticlePublish(),//
                "/article"), "/article/display/1/this_is_your_article_title");
        parser.parse(query);
        expResult.put("action", "display");
        expResult.put("id", "1");
        expResult.put("title", "this_is_your_article_title");
        assertEquals(expResult, parser.getMap());
        expResult.clear();
        /**
         *
         */
        parser = new ParamsStandardParser();
        parser.connect("/:action/:id/:title");
        query = new Query(RouterEntry.newPublishEntry(new ArticlePublish(),
                "/article"), "/article/edit/10101011/this_is_another_article_title");
        parser.parse(query);
        expResult.put("action", "edit");
        expResult.put("id", "10101011");
        expResult.put("title", "this_is_another_article_title");
        assertEquals(expResult, parser.getMap());
        expResult.clear();
        /**
         * 
         */
        parser = new ParamsStandardParser();
        parser.connect("/:action/:id[;]");
        query = new Query(RouterEntry.newPublishEntry(new ArticlePublish(),
                "/article"), "/article/del/10;10;10;11");
        parser.parse(query);
        expResult.put("action", "del");
        expResult.put("id", new String[]{"10", "10", "10", "11"});
        assertEquals(expResult.get("action"), parser.getValue("action"));
        assertEquals(Arrays.toString(parser.getValues("id")),
                Arrays.toString(new String[]{"10", "10", "10", "11"}));
        expResult.clear();
        /**
         *
         */
        parser = new ParamsStandardParser();
        parser.connect("/:action/:id[,]");
        query = new Query(RouterEntry.newPublishEntry(new ArticlePublish(),
                "/article"), "/article/del/10,10,10,11");
        parser.parse(query);
        expResult.put("action", "del");
        expResult.put("id", new String[]{"10", "10", "10", "11"});
        assertEquals(expResult.get("action"), parser.getValue("action"));
        assertEquals(Arrays.toString(parser.getValues("id")),//
                Arrays.toString(new String[]{"10", "10", "10", "11"}));
        expResult.clear();
        /**
         * 
         */
        parser = new ParamsStandardParser();
        parser.connect("/:action/:id[-]");
        query = new Query(RouterEntry.newPublishEntry(new ArticlePublish(), "/article"),//
                "/article/del/10-10-10-11");
        parser.parse(query);
        expResult.put("action", "del");
        expResult.put("id", new String[]{"10", "10", "10", "11"});
        assertEquals(expResult.get("action"), parser.getValue("action"));
        assertEquals(Arrays.toString(parser.getValues("id")), //
                Arrays.toString(new String[]{"10", "10", "10", "11"}));
        /**
         * 
         */
        TimeMeasurer tm = new TimeMeasurer();
        tm.start();

        for (int i = 0; i < 10; i++) {
            parser = new ParamsStandardParser();
            parser.connect("/:action/:id[-]");
            query = new Query(RouterEntry.newPublishEntry(new ArticlePublish(),//
                    "/article"), "/article/del/10-10-10-11");
            parser.parse(query);
            parser.getMap();
        }
        tm.stop();
//        System.out.println(tm.spend());
        /**
         *  10,000
         *  1228
         *  1011
         *  969
         */
        expResult.clear();
        expResult = null;
        /**
         * 容错。
         */
        parser.connect("/:action/");
        parser.connect(":action/");
    }
}
