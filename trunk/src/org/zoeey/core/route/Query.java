/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: Query.java 70 2010-01-02 20:08:07Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import org.zoeey.core.util.StringHelper;

/**
 * 路由请求项
 * @author MoXie(SysTem128@GMail.Com)
 */
public class Query {

    /**
     * 路由条目
     */
    private RouterEntry entry;
    /**
     * 请求连接
     */
    private String queryURI;

    /**
     * Router 请求项
     * @param entry
     * @param queryString
     */
    public Query(RouterEntry entry, String queryString) {
        this.entry = entry;
        this.queryURI = queryString;
    }

    /**
     * 获取路由条目
     * @return
     */
    public RouterEntry getEntry() {
        return entry;
    }

    /**
     * 获取发布对象
     * @return
     */
    public PublishAble getPublish() {
        return entry.getPublish();
    }

    /**
     * 获取匹配模式
     * @return
     */
    public String getPattern() {
        return entry.getPattern();
    }

    /**
     * <pre>
     * 获取请求链接
     * 例如：
     *      定义：/{ContextPat}/{module}/{action}/{id}?{key}={value}
     *      返回部分为 /{module}/{action}/{id}
     * </pre>
     * @return
     */
    public String getURI() {
        return queryURI;
    }

    /**
     * <pre>
     * 获取查询字符串
     * 例如：
     *      定义：/{ContextPat}/{module}/{action}/{id}?{key}={value}
     *      匹配模式： /{module}
     *      返回部分为 /{action}/{id}
     * </pre>
     * @return
     */
    public String getQueryString() {
        return StringHelper.subString(queryURI, entry.getPattern().length(), -1);
    }
}
