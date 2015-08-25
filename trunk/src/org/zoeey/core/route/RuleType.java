/*
 * MoXie (SysTem128@GMail.Com) 2009-8-18 16:30:32
 * $Id: RouterRule.java 68 2009-11-01 07:56:13Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

/**
 * 规则类型
 */
public enum RuleType {

    /**
     * <pre>
     * 普通规则
     * add("/:action");
     * </pre>
     */
    TYPE_NORMAL,
    /**
     * <pre>
     * 正则匹配已出现的变量
     * .add("action", "(list)", "/:page/:label");
     * </pre>
     */
    TYPE_VARREGEXP, // 
    /**
     * <pre>
     * 已出现变量在某集合内
     * .add("action", new String[]{"view"}, "/:id/:title");
     * </pre>
     */
    TYPE_VARINSET,
    /**
     * <pre>
     * 参数个数是某值
     * .add(5, "/:id/:page/:label/:highlight");
     * </pre>
     */
    TYPE_VARCOUNT,
    /**
     * <pre>
     * 全QueryString正则匹配
     * .add("/track/([^/]+)/?$", "/:trackSn");
     * </pre>
     */
    TYPE_ALLREGEXP,
}
