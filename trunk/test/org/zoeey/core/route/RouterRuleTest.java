/*
 * MoXie (SysTem128@GMail.Com) 2009-8-18 16:37:39
 * $Id: RouterRuleTest.java 71 2010-01-07 05:49:47Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zoeey.core.util.ArrayHelper;

/**
 *
 * @author MoXie
 */
public class RouterRuleTest extends TestCase {

    /**
     *
     */
    public RouterRuleTest() {
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
     * Test of newRule method, of class RouterRule.
     */
    @Test
    public void testNewRule() {
        System.out.println("newRule");

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
        int i = 0;
        for (RuleItem item : rule.getRuleList()) {
            i++;
            switch (i) {
                case 1:
                    assertEquals(item.getType(), RuleType.TYPE_NORMAL);
                    assertEquals(item.getRule(), "/:action");
                    break;
                case 2:
                    assertEquals(item.getType(), RuleType.TYPE_VARREGEXP);
                    assertEquals(item.getRegexp(), "(list)");
                    break;
                case 3:
                    assertEquals(item.getType(), RuleType.TYPE_VARINSET);
                    assertEquals(ArrayHelper.diff(item.getStrs(),
                            new String[]{"view"}).length, 0);
                    break;
                case 4:
                    assertEquals(item.getType(), RuleType.TYPE_VARINSET);
                    assertEquals(ArrayHelper.diff(item.getStrs(),
                            new String[]{"edit", "delete"}).length, 0);
                    break;
                case 5:
                    assertEquals(item.getType(), RuleType.TYPE_VARCOUNT);
                    assertEquals(item.getRule(), "/:id/:page/:label/:highlight");
                    break;
                case 6:
                    assertEquals(item.getType(), RuleType.TYPE_ALLREGEXP);
                    assertEquals(item.getRule(), "/:trackSn");
                    break;
            }
        }
        /**
         * 初始为特殊匹配
         */
        rule = new RouterRule();
        rule.add(5, "/:id/:page/:label/:highlight");
        RuleItem _item = rule.getRuleList().get(0);
        assertEquals(_item.getType(), RuleType.TYPE_VARCOUNT);
        assertEquals(_item.getRule(), "/:id/:page/:label/:highlight");
    }
}
