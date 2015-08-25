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
 * 路由规则项
 */
public class RuleItem {

    /**
     * 普通规则
     */
    private String rule; // add("/:action")
    /**
     * 已出现变量
     */
    private String varName;
    /**
     * 正则表达式
     * .add("action", "(list)", "/:page/:label")
     */
    private String regexp;
    /**
     * 数据集合
     * .add("action", new String[]{"view"}, "/:id/:title")
     */
    private String[] strs;
    /**
     * 参数总个数
     * .add(5, "/:id/:page/:label/:highlight")
     */
    private int paramCount;
    /**
     * 规则类型
     */
    private RuleType type;
    /**
     * 是否终止
     */
    private boolean isEnd;

    /**
     * <pre>
     * 普通规则
     *  add("/:action");
     * </pre>
     * @param rule  规则描述
     * @param isEnd 中断
     */
    public RuleItem(String rule, boolean isEnd) {
        this.rule = rule;
        type = RuleType.TYPE_NORMAL;
        this.isEnd = isEnd;
    }

    /**
     * <pre>
     * 正则匹配已出现的变量
     * .add("action", "(list)", "/:page/:label");
     * </pre>
     * 
     * @param varName 变量名称  
     * @param regexp 正则表达式
     * @param rule  规则描述
     * @param isEnd 中断
     */
    public RuleItem(String varName, String regexp, String rule, boolean isEnd) {
        this.varName = varName;
        this.regexp = regexp;
        this.rule = rule;
        type = RuleType.TYPE_VARREGEXP;
        this.isEnd = isEnd;
    }

    /**
     * <pre>
     * 已出现变量在某集合内
     * .add("action", new String[]{"view"}, "/:id/:title");
     * </pre>
     * @param varName 变量名称
     * @param strs  字符串集
     * @param rule  规则描述
     * @param isEnd 中断
     */
    public RuleItem(String varName, String[] strs, String rule, boolean isEnd) {
        this.rule = rule;
        this.varName = varName;
        this.strs = strs.clone();
        type = RuleType.TYPE_VARINSET;
        this.isEnd = isEnd;
    }

    /**
     * <pre>
     * 参数个数是某值
     * .add(5, "/:id/:page/:label/:highlight");
     * </pre>
     * @param rule  规则描述
     * @param paramCount 参数个数
     * @param isEnd 中断
     */
    public RuleItem(int paramCount, String rule, boolean isEnd) {
        this.paramCount = paramCount;
        this.rule = rule;
        type = RuleType.TYPE_VARCOUNT;
        this.isEnd = isEnd;
    }

    /**
     * <pre>
     * 全QueryString正则匹配
     * add("/track/([^/]+)/?$", "/:trackSn");
     * </pre>
     * @param regexp 正则表达式
     * @param rule  规则描述 
     * @param isEnd 中断
     */
    public RuleItem(String regexp, String rule, boolean isEnd) {
        this.regexp = regexp;
        this.rule = rule;
        type = RuleType.TYPE_ALLREGEXP;
        this.isEnd = isEnd;
    }

    /**
     * 参数数量
     * @return
     */
    public int getParamCount() {
        return paramCount;
    }

    /**
     * 正则
     * @return
     */
    public String getRegexp() {
        return regexp;
    }

    /**
     * 规则
     * @return
     */
    public String getRule() {
        return rule;
    }

    /**
     * 数据集
     * @return
     */
    public String[] getStrs() {
        return strs;
    }

    /**
     * 规则类型
     * @return
     */
    public RuleType getType() {
        return type;
    }

    /**
     * 变量名
     * @return
     */
    public String getVarName() {
        return varName;
    }

    /**
     * 是否终止
     * @return
     */
    public boolean isIsEnd() {
        return isEnd;
    }
}
