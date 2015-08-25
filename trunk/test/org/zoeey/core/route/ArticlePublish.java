/*
 * MoXie (SysTem128@GMail.Com) 2009-7-14 17:45:12
 * $Id: ArticlePublish.java 70 2010-01-02 20:08:07Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.zoeey.core.route.annotations.Mapping;

/**
 *
 * @author MoXie
 */
@Mapping(pattern = {"/article"})
public class ArticlePublish implements PublishAble {

    public void init() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void publish(HttpServletRequest request, HttpServletResponse response, Query query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
