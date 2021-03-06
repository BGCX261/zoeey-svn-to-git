/*
 * MoXie (SysTem128@GMail.Com) 2009-6-22 15:24:39
 * $Id: PublishAble.java 70 2010-01-02 20:08:07Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 发布类接口
 * @author MoXie
 */
public interface PublishAble {

    /**
     * 初始化方法，仅在载入时执行一次
     */
    public void init();

    /**
     * 发布方法
     * @param request   请求对象
     * @param response  应答对象
     * @param query     路由请求项
     */
    public void publish(HttpServletRequest request, HttpServletResponse response, Query query);
}
