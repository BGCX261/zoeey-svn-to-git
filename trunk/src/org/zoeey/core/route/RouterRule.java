/*
 * MoXie (SysTem128@GMail.Com) 2009-8-18 16:30:32
 * $Id: RouterRule.java 70 2010-01-02 20:08:07Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由规则
 * @author MoXie
 */
public class RouterRule {

    /**
     * 规则列表
     */
    private List<RuleItem> ruleList = null;

    /**
     * 路由规则
     */
    public RouterRule() {
        ruleList = new ArrayList<RuleItem>();
    }

    /**
     * 路由规则
     * @param rule  规则描述
     */
    public RouterRule(String rule) {
        this();
        add(rule);
    }

    /**
     * 新建规则
     * @return
     */
    public static RouterRule newRule() {
        return new RouterRule();
    }

    /**
     * 新建普通规则
     * @param rule  路由规则描述
     * @return
     */
    public static RouterRule newRule(String rule) {
        return new RouterRule().add(rule);
    }

    /**
     * 新建规则
     * @param ruleItem  路由规则项
     * @return
     */
    public static RouterRule newRule(RuleItem ruleItem) {
        return new RouterRule().add(ruleItem);
    }

    /**
     * 新增规则
     * @param ruleItem  路由规则项
     * @return
     */
    public RouterRule add(RuleItem ruleItem) {
        ruleList.add(ruleItem);
        return this;
    }

    /**
     * <pre>
     * 普通规则
     * add("/:action")
     * </pre>
     * @param rule  规则描述
     * @return
     */
    public RouterRule add(String rule) {
        ruleList.add(new RuleItem(rule, false));
        return this;
    }

    /**
     * <pre>
     * 正则匹配已出现的变量
     * .add("action", "(list)", "/:page/:label")
     * </pre>
     * @param varName 变量名称
     * @param regexp 正则表达式
     * @param rule  规则描述
     * @return
     */
    public RouterRule add(String varName, String regexp, String rule) {
        ruleList.add(new RuleItem(varName, regexp, rule, false));
        return this;
    }

    /**
     * <pre>
     * 已出现变量在某集合内
     * .add("action", new String[]{"view"}, "/:id/:title")//
     * </pre>
     * @param varName   变量名称
     * @param strs  字符串集
     * @param rule  规则描述
     * @return
     */
    public RouterRule add(String varName, String[] strs, String rule) {
        ruleList.add(new RuleItem(varName, strs, rule, false));
        return this;
    }

    /**
     * <pre>
     * 参数个数是某值
     * .add(5, "/:id/:page/:label/:highlight")
     * </pre>
     * @param paramCount  参数个数
     * @param rule  规则描述
     * @return
     */
    public RouterRule add(int paramCount, String rule) {
        ruleList.add(new RuleItem(paramCount, rule, false));
        return this;
    }

    /**
     * <pre>
     * 全QueryString正则匹配
     *  add("/track/([^/]+)/?$", "/:trackSn");
     * </pre>
     * @param regexp 正则表达式
     * @param rule  规则描述
     * @return
     */
    public RouterRule add(String regexp, String rule) {
        ruleList.add(new RuleItem(regexp, rule, false));
        return this;
    }

    /**
     * <pre>
     * 普通规则，应用后忽略以后的其他规则
     * add("/:action")
     * </pre>
     * @param rule  规则描述
     * @return
     */
    public RouterRule endOn(String rule) {
        ruleList.add(new RuleItem(rule, true));
        return this;
    }

    /**
     * <pre>
     * 正则匹配已出现的变量，应用后忽略以后的其他规则
     * .add("action", "(list)", "/:page/:label")
     * </pre>
     * @param varName 变量名称
     * @param regexp 正则表达式
     * @param rule  规则描述
     * @return
     */
    public RouterRule endOn(String varName, String regexp, String rule) {
        ruleList.add(new RuleItem(varName, regexp, rule, true));
        return this;
    }

    /**
     * <pre>
     * 已出现变量在某集合内，应用后忽略以后的其他规则
     * .add("action", new String[]{"view"}, "/:id/:title")//
     * </pre>
     * @param varName   变量名称
     * @param strs  字符串集
     * @param rule  规则描述
     * @return
     */
    public RouterRule endOn(String varName, String[] strs, String rule) {
        ruleList.add(new RuleItem(varName, strs, rule, true));
        return this;
    }

    /**
     * <pre>
     * 参数个数是某值，应用后忽略以后的其他规则
     * .add(5, "/:id/:page/:label/:highlight")
     * </pre>
     * @param paramCount  参数个数
     * @param rule  规则描述
     * @return
     */
    public RouterRule endOn(int paramCount, String rule) {
        ruleList.add(new RuleItem(paramCount, rule, true));
        return this;
    }

    /**
     * <pre>
     * 全QueryString正则匹配，应用后忽略以后的其他规则
     *  add("/track/([^/]+)/?$", "/:trackSn");
     * </pre>
     * @param regexp 正则表达式
     * @param rule  规则描述
     * @return
     */
    public RouterRule endOn(String regexp, String rule) {
        ruleList.add(new RuleItem(regexp, rule, true));
        return this;
    }

    /**
     * 获取规则列表
     * @return
     */
    public List<RuleItem> getRuleList() {
        return ruleList;
    }
}
