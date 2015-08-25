/*
 * MoXie (SysTem128@GMail.Com) 2009-8-6 16:14:11
 * $Id: RouterConfigLoader.java 70 2010-01-02 20:08:07Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.zoeey.core.resource.ResourceExceptionMsg;
import org.zoeey.core.route.RouterConfig.Entry;
import org.zoeey.core.route.annotations.Mapping;
import org.zoeey.core.route.exceptions.RouterEntryPublisherClassNoFoundException;
import org.zoeey.core.route.exceptions.RouterParserException;

/**
 * 路由配置文件读取
 * @author MoXie
 */
class RouterConfigLoader {

    /**
     * 锁定创建
     */
    private RouterConfigLoader() {
    }

    /**
     * 读取路由配置文件
     * @param configFile
     * @throws RouterParserException
     */
    public static void load(File configFile) //
            throws RouterParserException {
        try {
            RouterConfigParser parser = new RouterConfigParser();
            parser.parse(configFile);
            RouterConfig context = parser.getContext();
            /**
             * init
             */
            for (String init : context.getInitList()) {
                Router.add(init);
            }
            /**
             * publish
             */
            for (Entry item : context.getPublishList()) {
                Router.add(item.getPattern(), item.getPublish());
            }
            loadAnnotation(context.getAnnotList());
            /**
             * 排序
             */
            Router.sort();
        } catch (RouterParserException ex) {
            throw new RouterParserException(ex.getMessage(), RouterConfigLoader.class.getName());
        } catch (IOException ex) {
            throw new RouterParserException(ex.getMessage(), RouterConfigLoader.class.getName());
        } catch (ParserConfigurationException ex) {
            throw new RouterParserException(ex.getMessage(), RouterConfigLoader.class.getName());
        } catch (RouterEntryPublisherClassNoFoundException ex) {
            throw new RouterParserException(ex.getMessage(), RouterConfigLoader.class.getName());
        } catch (SAXException ex) {
            throw new RouterParserException(ex.getMessage(), RouterConfigLoader.class.getName());
        }
    }

    /**
     * 解析Annotation声明的路径
     * @param annotList
     * @throws RouterEntryPublisherClassNoFoundException
     */
    @SuppressWarnings("unchecked")
    private static void loadAnnotation(List<String> annotList) //
            throws RouterEntryPublisherClassNoFoundException {
        try {
            Class<PublishAble> clazz = null;
            Mapping mapping = null;
            for (String annot : annotList) {
                clazz = (Class<PublishAble>) Class.forName(annot);
                if (clazz != null //
                        && PublishAble.class.isAssignableFrom(clazz) //
                        && clazz.isAnnotationPresent(Mapping.class)) {
                    mapping = clazz.getAnnotation(Mapping.class);
                    if (mapping.pattern().length > 0) {
                        for (String pattern : mapping.pattern()) {
                            Router.add(pattern, clazz);
                        }
                    }
                } else {
                    throw new RouterEntryPublisherClassNoFoundException(ResourceExceptionMsg.ROUTER_PUBLISHER_CLASS_NOFOUND_EXCEPTION);
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new RouterEntryPublisherClassNoFoundException(ResourceExceptionMsg.ROUTER_PUBLISHER_CLASS_NOFOUND_EXCEPTION);
        }
    }
}
