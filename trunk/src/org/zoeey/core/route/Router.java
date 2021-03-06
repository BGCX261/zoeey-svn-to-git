/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: Router.java 77 2010-03-29 01:44:01Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.zoeey.core.resource.ResourceExceptionMsg;
import org.zoeey.core.route.annotations.Mapping;
import org.zoeey.core.route.exceptions.PublisherClassNoFoundException;
import org.zoeey.core.route.exceptions.RouterEntryPublisherClassNoFoundException;
import org.zoeey.core.route.exceptions.RouterParserException;
import org.zoeey.core.util.JsonEncoder;

/**
 * 路由导航，资源管理
 * @author MoXie(SysTem128@GMail.Com)
 */
public class Router {

    /**
     * 锁定创建
     */
    private Router() {
    }
    /**
     * 只需要初始化的发布对象
     */
    private static List<RouterEntry> initorList;
    /**
     * 需要发布的对象
     */
    private static List<RouterEntry> publisherList;
    /**
     * 已初始化的发布对象
     */
    private static List<String> initedList = null;
    /**
     * 是否已转换为Map
     */
    private static boolean IS_TO_MAPED = false;

    static {
        // 仅需要初始化
        initorList = new ArrayList<RouterEntry>(50);
        // 还需要publish
        publisherList = new ArrayList<RouterEntry>(50);
        // 已初始化
        initedList = new ArrayList<String>();
    }

    /**
     *  新增一个只需要初始化（init）的发布类
     * @param className
     * @throws RouterEntryPublisherClassNoFoundException
     */
    public static void add(String className)
            throws RouterEntryPublisherClassNoFoundException {
        add(null, className);
    }

    /**
     * 新增一个需要初始化（init）和发布（publish）的发布类
     * @param pattern  匹配模式
     * @param clazz 发布类
     * @throws RouterEntryPublisherClassNoFoundException
     */
    public static void add(String pattern, Class<PublishAble> clazz)
            throws RouterEntryPublisherClassNoFoundException {
        if (PublishAble.class.isAssignableFrom(clazz)) {
            try {
                add(RouterEntry.newPublishEntry(clazz.newInstance(), pattern));
            } catch (InstantiationException ex) {
                throw new RouterEntryPublisherClassNoFoundException(ResourceExceptionMsg.ROUTER_PUBLISHER_CLASS_NOFOUND_EXCEPTION);
            } catch (IllegalAccessException ex) {
                throw new RouterEntryPublisherClassNoFoundException(ResourceExceptionMsg.ROUTER_PUBLISHER_CLASS_NOFOUND_EXCEPTION);
            }
        } else {
            throw new RouterEntryPublisherClassNoFoundException(ResourceExceptionMsg.ROUTER_PUBLISHER_CLASS_NOFOUND_EXCEPTION);
        }
    }

    /**
     * 新增一个需要初始化（init）和发布（publish）的发布类
     * @param pattern   匹配模式
     * @param className 发布类
     * @throws RouterEntryPublisherClassNoFoundException
     */
    @SuppressWarnings("unchecked")
    public static void add(String pattern, String className)
            throws RouterEntryPublisherClassNoFoundException {
        try {
            Class<PublishAble> clazz = (Class<PublishAble>) Class.forName(className, //
                    true, Router.class.getClassLoader());
            add(pattern, clazz);
        } catch (ClassNotFoundException ex) {
            throw new RouterEntryPublisherClassNoFoundException(ResourceExceptionMsg.ROUTER_PUBLISHER_CLASS_NOFOUND_EXCEPTION);
        }

    }

    /**
     * 新增一个需要初始化（init）和发布（publish）的发布对象
     * @param publish   发布对象
     * @throws RouterEntryPublisherClassNoFoundException
     */
    public static void addAnnoted(PublishAble publish)
            throws RouterEntryPublisherClassNoFoundException {
        Mapping mapping;
        if (publish != null //
                && publish.getClass().isAnnotationPresent(Mapping.class)) {
            mapping = publish.getClass().getAnnotation(Mapping.class);
            if (mapping.pattern().length > 0) {
                for (String pattern : mapping.pattern()) {
                    add(pattern, publish);
                }
            }
        } else {
            throw new RouterEntryPublisherClassNoFoundException(ResourceExceptionMsg.ROUTER_PUBLISHER_CLASS_NOFOUND_EXCEPTION);
        }
    }

    /**
     * 新增一个需要初始化（init）和发布（publish）的发布对象
     * <b>【推荐】</b>
     * @param pattern   匹配模式
     * @param publish   发布对象
     * @throws RouterEntryPublisherClassNoFoundException
     * @see #add(java.lang.String, java.lang.String)
     */
    public static void add(String pattern, PublishAble publish)
            throws RouterEntryPublisherClassNoFoundException {
        add(RouterEntry.newPublishEntry(publish, pattern));
    }

    /**
     * 新增一个只需初始化（init）的发布对象
     * <b>【推荐】</b>
     * @param publish   发布对象
     * @throws RouterEntryPublisherClassNoFoundException
     */
    public static void add(PublishAble publish)
            throws RouterEntryPublisherClassNoFoundException {
        add(RouterEntry.newInitEntry(publish));
    }

    /**
     * 新增路由条目
     * <b>【推荐】</b>
     * @param entry Router单元
     */
    public static void add(RouterEntry entry) {
        if (entry.isOnlyForInit()) {
            initorList.add(entry);
        } else {
            publisherList.add(entry);
        }

    }

    /**
     * 清理所有仅需初始化（init）的发布对象
     */
    public static void clearInitor() {
        initorList.clear();
    }

    /**
     * 清理所有需init和publish的发布类
     */
    public static void clearPublisherList() {
        initorList.clear();
    }

    /**
     * 清理所有已初始化的发布类
     * 注意：多次调用init()不会执行在InitedList内的类。
     */
    public static void clearInitedList() {
        initedList.clear();
    }

    /**
     * 清理所有已初始化的发布类
     * 注意：多次调用init不会执行在InitedList内的类。
     * @param clazz
     */
    public static void clearInitedList(Class<PublishAble> clazz) {
        initedList.remove(clazz.getName());
    }

    /**
     * 获取只需要初始（init）的对象列表
     * @return  
     */
    public static List<RouterEntry> getInitorList() {
        return initorList;
    }

    /**
     * 获取需要发布（publish）的对象列表
     * @return 发布对象列表
     */
    public static List<RouterEntry> getPublisherList() {
        return publisherList;
    }

    /**
     * 按照从匹配模式的长度从长到短的顺序排列。
     */
    public static void sort() {
        boolean isContinue = false;
        RouterEntry _entry = null;
        do {
            isContinue = false;
            for (int i = publisherList.size() - 1;
                    i > 0; i--) {
                _entry = publisherList.get(i);
                if (_entry.getLength() > publisherList.get(i - 1).getLength()) {
                    publisherList.set(i, publisherList.get(i - 1));
                    publisherList.set(i - 1, _entry);
                    isContinue = true;
                }
            }
        } while (isContinue);
    }
    /**
     * 转换为map
     */
    private static HashMap<String, RouterEntry> publusherMap;

    /**
     * 转换需要发布的类剔除匹配模式重复的项
     */
    private static void toMap() {
        if (!IS_TO_MAPED) {
            publusherMap = new HashMap<String, RouterEntry>(publisherList.size());
            for (RouterEntry routerEntry : publisherList) {
                publusherMap.put(routerEntry.getPattern(), routerEntry);
            }

        }
    }

    /**
     * 根据匹配模式获取请求项
     * @param pattern 匹配模式
     * @return
     * @throws PublisherClassNoFoundException
     */
    public static Query getQuery(String pattern)
            throws PublisherClassNoFoundException {
        toMap();
        RouterEntry entry = publusherMap.get(pattern);
        if (entry == null) {
            throw new PublisherClassNoFoundException(ResourceExceptionMsg.ROUTER_PUBLISHER_CLASS_NOFOUND_EXCEPTION);
        }

        return new Query(entry, pattern);
    }

    /**
     * 从请求对象中分析出请求项,大小写敏感
     * <b>【推荐】</b>
     * @param request   请求对象
     * @return          请求项
     * @throws PublisherClassNoFoundException
     */
    public static Query parse(HttpServletRequest request)
            throws PublisherClassNoFoundException {
        return parse(request.getPathInfo());
    }

    /**
     * <pre>
     *  分析链接取出请求项,大小写敏感
     * </pre>
     * @param uri 链接
     * @return PublishAble Class    发布类
     * @throws PublisherClassNoFoundException
     */
    public static Query parse(String uri)
            throws PublisherClassNoFoundException {
        return parse(uri, false);
    }

    /**
     * <pre>
     *  分析链接取出query
     *  不区分大小写
     * 注意：不区分大小写的匹配效率远低于大小写敏感匹配，建议使用{@link #parse(java.lang.String) }
     * </pre>
     * @param uri 链接
     * @return PublishAble Class
     * @throws PublisherClassNoFoundException
     */
    public static Query parseIgnoreCase(String uri)
            throws PublisherClassNoFoundException {
        return parse(uri, true);
    }

    /**
     * 分析链接取出query
     * @param uri 链接
     * @param ignoreCase 是否忽略大小 注意：忽略大小写的效率较低。耗时约为大小写敏感的两倍。
     * @return PublishAble Class
     * @throws PublisherClassNoFoundException
     */
    private static Query parse(String uri, boolean ignoreCase)
            throws PublisherClassNoFoundException {
        int uriLen = (uri == null) ? 0 : uri.length();
        RouterEntry entry = null;
        String pattern;

        if (uriLen > 0) {
            int _length = 0;//路径长度
            for (RouterEntry _entry : publisherList) {
                _length = _entry.getLength();
                if (_length > uriLen) {
                    continue;
                }

                pattern = _entry.getPattern();
                if (ignoreCase) {
                    if (pattern.equalsIgnoreCase(uri.substring(0, _length))) {
                        entry = _entry;
                        break;

                    }


                } else {
                    if (pattern.equals(uri.substring(0, _length))) {
                        entry = _entry;
                        break;

                    }


                }
            }
        }
        /**
         * 匹配失败
         */
        if (entry == null) {
            throw new PublisherClassNoFoundException(ResourceExceptionMsg.ROUTER_PUBLISHER_CLASS_NOFOUND_EXCEPTION);
        }

        return new Query(entry, uri);
    }

    /**
     * 读取配置，
     * @param configFile
     * @throws RouterParserException 
     */
    public static void loadConfig(File configFile) //
            throws RouterParserException {
        RouterConfigLoader.load(configFile);

    }

    /**
     * 初始化所有发布类
     */
    public static synchronized void init() {
        List<RouterEntry> routerEntryList = initorList;
        routerEntryList.addAll(publisherList);
        PublishAble publishAble = null;
        for (RouterEntry _entry : routerEntryList) {
            publishAble = _entry.getPublish();
            if (!initedList.contains(publishAble.getClass().getName())) {
                publishAble.init();
                initedList.add(publishAble.getClass().getName());
            }
        }
    }

    /**
     * 从配置文件初始化
     * <b>【推荐】</b>
     * @param configFile 
     * @throws RouterParserException
     */
    public static void init(File configFile)
            //
            throws RouterParserException {
        loadConfig(configFile);
        init();


    }
}
