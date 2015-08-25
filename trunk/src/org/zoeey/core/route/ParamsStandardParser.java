/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: ParamsStandardParser.java 75 2010-03-16 02:21:41Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import org.zoeey.core.route.exceptions.RouterConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.zoeey.core.resource.ResourceExceptionMsg;
import org.zoeey.core.util.ArrayHelper;
import org.zoeey.core.util.FieldEntry;
import org.zoeey.core.util.StringHelper;

/**
 * <pre>
 * 路由参数标准分析器
 * 格式规则较为严谨，使用 / 分割不同的参数 。默认多值参数使用逗号分隔。
 * ex.
 *  RouterRule rule = RouterRule.newRule("/:action")//
 *                // 正则匹配已出现的变量
 *                .add("action", "(list)", "/:page/:label")//
 *                // 已出现变量在某集合内
 *                .add("action", new String[]{"view"}, "/:id/:title")//
 *                .add("action", new String[]{"edit", "delete"}, "/:id")//
 *                // 参数个数是某值
 *                .endOn(5, "/:id/:page/:label/:highlight")//
 *                // 全QueryString正则匹配
 *                .add("/track/([^/]+)/?$", "/:trackSn");
 * </pre>
 * @see RouterRule 路由规则
 * @author MoXie(SysTem128@GMail.Com)
 */
public class ParamsStandardParser implements ParamsParserAble {

    /**
     * 字段列表
     */
    private Collection<Entry<String,String>> fieldList = null;
    /**
     * 路由规则
     */
    private RouterRule routerRule = null;
    /**
     * 参数分隔符
     */
    private final char sepCh = '/';
    /**
     * 参数解析位置
     */
    private int paramIndex = 0;

    /**
     * 参数分析器
     */
    public ParamsStandardParser() {
        paramItemList = new ArrayList<ParameterItem>();
    }
    /**
     * 参数寄存
     */
    private List<ParameterItem> paramItemList = null;

    /**
     * <pre>
     * 装载参数描述
     * /:action/:id[,]
     * 变量名称 大小写字母、数字、下划线、减号等
     * [] 用于描述多参数，中括号间可自定义字符，用于分割值。
     * </pre>
     */
    public ParamsParserAble connect(String route) {
        routerRule = RouterRule.newRule(route);
        return this;
    }

    /**
     * 装载路由规则
     * @param routerRule
     * @return
     */
    public ParamsParserAble connect(RouterRule routerRule) {
        this.routerRule = routerRule;
        return this;
    }

    /**
     * 分析路由规则
     * @param route 路由规则
     * @throws RouterConnectException
     */
    private void parseRule(String route) throws RouterConnectException {
        if (route == null) {
            return;
        }
        /**
         * 剔除首尾斜杠
         */
        if (route.startsWith("/")) {
            route = route.substring(1);
        }
        if (route.endsWith("/")) {
            route = route.substring(0, route.length() - 1);
        }
        String[] params = StringHelper.split(route, sepCh);
        char defSep = ';';
        char ch = 0;
        for (String param : params) {
            ch = 0;
            /**
             * 不是参数名
             */
            if (!param.startsWith(":")) {
                throw new RouterConnectException(ResourceExceptionMsg.ROUTER_CONNECT_ERROR, param);
            }
            if (param.endsWith("]")) {
                /**
                 * :[]
                 */
                if (param.length() < 3) {
                    throw new RouterConnectException(ResourceExceptionMsg.ROUTER_CONNECT_ERROR, param);
                }
                if (param.endsWith("[]")) {
                    paramItemList.add(new ParameterItem(StringHelper.subString(param, 1, param.length() - 2), defSep));
                } else if (param.charAt(param.length() - 3) == '[') {
                    paramItemList.add(new ParameterItem(StringHelper.subString(param, 1, param.length() - 3), param.charAt(param.length() - 2)));
                } else {
                    throw new RouterConnectException(ResourceExceptionMsg.ROUTER_CONNECT_ERROR, param);
                }
            } else {
                paramItemList.add(new ParameterItem(param.substring(1), ch));
            }

        }
    }

    /**
     * <pre>
     * 解析参数
     * </pre>
     * @param query 请求项
     * @return
     * @throws RouterConnectException 
     */
    public ParamsParserAble parse(Query query) throws RouterConnectException {
        /**
         * 整理资源
         */
        fieldList = new ArrayList<Entry<String,String>>();
        String paramString = query.getQueryString();
        //
        if (paramString != null && paramString.length() > 0 && routerRule != null) {
            if (paramString.startsWith("/")) {
                paramString = paramString.substring(1);
            }
            String[] params = paramString.split("/");
            int paramsSize = params.length;
            String value = null;
            String[] values = null;
            ParameterItem paramEntry;
            char _sep;
            int paramItemSize = 0;
            String _name = null;
            boolean isAdded = false;
            RulesLoop:
            for (RuleItem ruleItem : routerRule.getRuleList()) {

                /**
                 * 装载规则
                 */
                isAdded = false;
                switch (ruleItem.getType()) {
                    case TYPE_NORMAL:
                        parseRule(ruleItem.getRule());
                        isAdded = true;
                        break;
                    case TYPE_VARREGEXP:
                        value = getValue(ruleItem.getVarName());
                        if (value != null && value.matches(ruleItem.getRegexp())) {
                            parseRule(ruleItem.getRule());
                            isAdded = true;
                        }
                        break;
                    case TYPE_VARINSET:
                        values = getValues(ruleItem.getVarName());
                        if (values != null && ArrayHelper.inArray(ruleItem.getStrs(), values)) {
                            parseRule(ruleItem.getRule());
                            isAdded = true;
                        }
                        break;
                    case TYPE_VARCOUNT:
                        if (paramsSize == ruleItem.getParamCount()) {
                            parseRule(ruleItem.getRule());
                            isAdded = true;
                        }
                        break;
                    case TYPE_ALLREGEXP:
                        if (paramString.matches(ruleItem.getRegexp())) {
                            parseRule(ruleItem.getRule());
                            isAdded = true;
                        }
                        break;
                    default:
                        break;
                }
                if (isAdded) {
                    paramItemSize = paramItemList.size();
                    /**
                     * 解析值
                     */
                    for (int idx = paramIndex; idx < paramsSize; idx++) {
                        value = params[idx];
                        if (paramItemSize > idx) {
                            paramIndex++;
                            paramEntry = paramItemList.get(idx);
                            _sep = paramEntry.getVarSep();
                            _name = paramEntry.getVarName();
                            if (paramEntry.isArray()) {
                                values = StringHelper.split(value, _sep);
                                if (values != null) {
                                    for (int i = 0; i < values.length; i++) {
                                        fieldList.add(new FieldEntry<String,String>(_name, values[i]));
                                    }
                                }
                            } else {
                                fieldList.add(new FieldEntry<String,String>(_name, value));
                            }
                        }
                    }
                    /**
                     * 判断终止
                     */
                    if (ruleItem.isIsEnd()) {
                        break RulesLoop;
                    }
                }
            }

            //
        }
        return this;
    }

    /**
     * 获取当前规则表达式
     * @return
     */
    public String getRuleExpression() {
        StringBuilder strBuilder = new StringBuilder();
        for (ParameterItem paramItem : paramItemList) {
            strBuilder.append("/:");
            strBuilder.append(paramItem.getVarName());
            if (paramItem.isArray()) {
                strBuilder.append("[");
                strBuilder.append(paramItem.getVarSep());
                strBuilder.append("]");
            }
        }
        return strBuilder.toString();
    }

    /**
     * 获取值
     * @param name
     * @return
     */
    public String getValue(String name) {
        if (fieldList != null) {
            for (Entry<String,String> field : fieldList) {
                if (field.getKey().equalsIgnoreCase(name)) {
                    return field.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 获取值列表
     * @param name
     * @return
     */
    public String[] getValues(String name) {
        if (fieldList == null) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        for (Entry<String,String> field : fieldList) {
            if (field.getKey().equalsIgnoreCase(name)) {
                list.add(field.getValue());
            }
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 获取所有字段列表
     * @return
     */
    public Collection<Entry<String,String>> getEntryCollection() {
        return fieldList;
    }

    /**
     * 获取字段Map
     * 注意：同名多值参数仅取第一个值。
     * @return
     */
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (Entry<String,String> field : fieldList) {
            if (map.containsKey(field.getKey())) {
                continue;
            }
            map.put(field.getKey(), field.getValue());
        }
        return map;
    }

    /**
     * 参数
     */
    private static class ParameterItem {

        /**
         * 变量名称
         */
        private String varName = null;
        /**
         * 分隔符
         */
        private char varSep = 0;

        public ParameterItem(String varName, char varSeq) {
            this.varName = varName;
            this.varSep = varSeq;
        }

        /**
         * 变量名
         * @return
         */
        public String getVarName() {
            return varName;
        }

        /**
         * 是否为数组
         * @return
         */
        public boolean isArray() {
            return varSep != 0;
        }

        /**
         * 变量分隔符
         * @return
         */
        public char getVarSep() {
            return varSep;
        }
    }
}
