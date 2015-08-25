/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: RouterTest.java 65 2009-08-10 06:39:41Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import junit.framework.TestCase;
import org.zoeey.core.util.TimeMeasurer;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class RouterTest extends TestCase {

    /**
     *
     * @param testName
     */
    public RouterTest(String testName) {
        super(testName);
    }

    /**
     *
     */
    public void testBubble() {
//        System.out.println("testBubble");
        boolean isOver = false;
        String[] arr = {"1", "123", "21345", "321321", "21345", "32321321"};
        do {
            isOver = true;
            String val = null;
            for (int i = arr.length - 1; i > 0; i--) {
                val = arr[i];
                if (val.length() > arr[i - 1].length()) {
                    arr[i] = arr[i - 1];
                    arr[i - 1] = val;
                    isOver = false;
                }
            }
        } while (!isOver);
//        assertEquals(Arrays.asList(bubbleArray).toString(), "[32321321, 321321, 21345, 21345, 123, 1]");
    }

    /**
     *
     */
    public void testComparator() {
//        System.out.println("testComparator");
        String[] arr = {"1", "123", "1111111111111", "321", "21345", "32321321"};
        List<String> list = Arrays.asList(arr);
        Collections.<String>sort(list, new Comparator<String>() {

            public int compare(String strA, String strB) {
                if (strA.length() == strB.length()) {
                    return 0;
                }
                return strA.length() > strB.length() ? -1 : 1;
            }
        });
//        assertEquals(list.toString(), "[1111111111111, 32321321, 21345, 123, 321, 1]");
    }

    /**
     *
     * @throws Exception
     */
    public void testSorts() throws Exception {
        System.out.println("testSorts");
        TimeMeasurer tm = new TimeMeasurer();
        tm.start("Bubble");
//        for (int i = 0; i < 1000000; i++) {
        testBubble();
//        }
        tm.stop("Bubble");
        System.out.println(tm.spend("Bubble"));
        /**
         * 1,000,000
         * 555
         * 516
         * 533
         */
        tm.start("Comparato");
//        for (int i = 0; i < 1000000; i++) {
        testComparator();
//        }
        tm.stop("Comparato");
        System.out.println(tm.spend("Comparato"));
        /**
         * 1,000,000
         * 1336
         * 1605
         * 1656
         */
    }

    /**
     *
     * @throws Exception
     */
    public void testParse() throws Exception {
        System.out.println("testParse");
        TimeMeasurer tm = new TimeMeasurer();
        tm.start("add");
        for (int i = 500; i > 0; i--) {
            Router.add("/article" + i, "org.zoeey.core.route.ArticlePublish");
            Router.add("/article" + i, new ArticlePublish());
        }
        Router.add("/manage/article", new ArticlePublish());
        Router.add("/article", new ArticlePublish());
        Router.add("/articleCCC", new ArticlePublish());
        tm.stop("add");
        tm.start("sort");
        for (int i = 0; i < 10; i++) {
            Router.sort();
        }
        tm.stop("sort");
        tm.start("parse");
        for (int i = 0; i < 10000; i++) {
            Router.parse("/article5000/add/1");
            Router.parse("/article4999/del/2,3,5");
            Router.parse("/article0/modify/5,6,7");
            Router.parse("/article1/modify/2,3,6");
            Router.parse("/article2500/edit/8");
        }
        tm.stop("parse");
        assertEquals(Router.parse("/article25000/edit/8").getPattern(), "/article250");
        assertEquals(Router.parse("/article2/edit/8").getPattern(), "/article2");
        assertEquals(Router.parse("/article250.php?id=100").getPattern(), "/article250");
        System.out.println(tm.spend("sort"));
        System.out.println(tm.spend("parse"));
        /**
         * 10,000
         * 1677
         * 1670
         * 1732
         */
    }
}
