/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: RouterEntry.java 70 2010-01-02 20:08:07Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.route;

/**
 * 路由条目
 * 用于存放<b>发布对象</b>和<b>路径匹配模式</b>
 * @author MoXie(SysTem128@GMail.Com)
 */
public class RouterEntry {

    /**
     * 需要发布的类
     * Publish Class
     */
    private PublishAble publish;
    /**
     * 路径匹配模式
     */
    private String pattern;
    /**
     * 路径长度
     */
    private int length;
    private boolean isOnlyForInit = false;

    /**
     * 路由条目
     * @see #newPublishEntry(java.lang.String, java.lang.String)
     * @param clazz 
     * @param pattern
     */
    public RouterEntry(PublishAble clazz, String pattern) {
        this.publish = clazz;
        initPattern(pattern);
    }

    /**
     * 路由条目 
     * @param publish 发布对象
     * @param pattern 为null或长度为0时仅执行init方法，否则在访问pattern时执行publish方法
     * @return
     */
    public static RouterEntry newPublishEntry(PublishAble publish, String pattern) {
        return new RouterEntry(publish, pattern);
    }

    /**
     * 路由条目
     * 初始化时执行init，且执行后被销毁。
     * @param publish 需要发布的类
     * @return
     */
    public static RouterEntry newInitEntry(PublishAble publish) {
        return new RouterEntry(publish, null);
    }

    /**
     * 获取发布对象
     * @return 需要发布的类
     */
    public PublishAble getPublish() {
        return publish;
    }

    /**
     * @return 路径长度
     */
    protected int getLength() {
        return length;
    }

    /**
     * 匹配模式
     * @return 
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * 是否仅为(1声)执行Init方法
     * @return
     */
    public boolean isOnlyForInit() {
        return isOnlyForInit;
    }

    /**
     * 初始：匹配模式与模式长度
     * @param pattern
     */
    private void initPattern(String pattern) {
        if (pattern != null) {
            this.pattern = pattern;
            this.length = pattern.length();
        } else {
            this.pattern = null;
            this.length = 0;
        }
        isOnlyForInit = this.length > 0 ? false : true;
    }
}
