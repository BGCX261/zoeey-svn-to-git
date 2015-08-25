/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: ArticleBeansBase.java 62 2009-07-17 09:49:09Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.zdo;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class ArticleBeansBase implements ArticleBeanAddAble {

    private int id;
    private String title;
    private String content;
    private long editTime;

    /**
     *
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public long getEditTime() {
        return editTime;
    }

    /**
     *
     * @param editTime
     */
    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }
}
