/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: ParamsParserAble.java 75 2010-03-16 02:21:41Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import org.zoeey.core.route.exceptions.RouterConnectException;

/**
 * 路由参数分析接口
 * @see ParamsStandardParser
 * @author MoXie(SysTem128@GMail.Com)
 */
public interface ParamsParserAble {

    /**
     * 装载参数描述
     * @param route
     * @return 
     */
    public ParamsParserAble connect(String route);

    /**
     * 装载路由规则
     * @param routerRule 
     * @return
     */
    public ParamsParserAble connect(RouterRule routerRule);

    /**
     * 解析参数
     * @param query 请求项
     * @return
     * @throws RouterConnectException 
     */
    public ParamsParserAble parse(Query query) throws RouterConnectException;

    /**
     * 获取当前规则表达式
     * @return
     */
    public String getRuleExpression();

    /**
     * 获取值
     * @param name
     * @return
     */
    public String getValue(String name);

    /**
     * 获取值列表
     * @param name
     * @return
     */
    public String[] getValues(String name);

    /**
     * 获取字段列表
     * @return
     */
    public Collection<Entry<String,String> > getEntryCollection();

    /**
     * 获取字段Map
     * @return
     */
    public Map<String, String> getMap();
}
