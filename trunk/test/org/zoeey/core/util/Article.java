/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: Article.java 75 2010-03-16 02:21:41Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.util;

import org.zoeey.core.drive.Actions;
import org.zoeey.core.drive.annotations.RunSn;
import org.zoeey.core.loader.RequestMethod;
import org.zoeey.core.loader.annotations.Request;
import org.zoeey.core.validator.annotations.AllowNull;
import org.zoeey.core.validator.annotations.Length;
import org.zoeey.core.validator.annotations.MsgSn;
import org.zoeey.core.validator.annotations.NotNull;
import org.zoeey.core.validator.annotations.Range;
import org.zoeey.core.zdo.FieldType;
import org.zoeey.core.zdo.annotations.Field;
import org.zoeey.core.zdo.annotations.Table;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
@Table(name = "article")
public class Article {

    private int id = 0;
    private int[] idList = {};
    private String author = "MoXie";
    private int age = 10;
    private String title = "this is title";
    private int page = 1;

    /**
     * 年龄的允许范围在1至200。
     * @param age
     */
    @RunSn({Actions.DISPLAY, Actions.ADD, Actions.EDIT})
    @Request(name = "age", method = RequestMethod.POST)
    @NotNull(msgSn = "validator.edit.age.fail.isnull",
    msgNative = "年龄的允许范围在1至200。")
    @Range(min = 1, max = 200)
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * <pre>
     * 作者名允许为空，但当有作者时其名称长度应在6到36位字符之间。
     *
     * msgNative 建议写法:
     * [zh-cn]|作者验证错误。|[en]Incorrect Author.
     * 语言包可以使用此种类型嵌入在代码内，便于分析。
     * </pre>
     * @param author
     */
    @RunSn({Actions.DISPLAY, Actions.ADD, Actions.EDIT})
    @Request(name = "author", method = RequestMethod.POST)
    @MsgSn(value = "validator.edit.author.fail",
    msgNative = "作者名允许为空，但当有作者时其名称长度应在6到36位字符之间。")
    @AllowNull()
    @Length(min = 6, max = 36)
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 作者名允许为空，但当有作者时其名称长度应在6到36位字符之间。
     *
     * msgNative 建议写法:
     * [zh-cn]|作者验证错误。|[en]Incorrect Author.
     * 语言包可以使用此种类型嵌入在代码内，便于分析。
     * @param id
     */
    // 使用标识
    @RunSn({Actions.DISPLAY, Actions.EDIT})
    // 值获取
    @Request(name = "id", method = RequestMethod.REQUEST)
    // 过滤
    @MsgSn(value = "validator.edit.id.fail",
    msgNative = "文章编号错误！")
    @Length(min = 1, max = 11)
    @Range(min = 1, max = Integer.MAX_VALUE)
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 批量修改时使用
     * @param idList
     */
    //使用标识
    @RunSn({Actions.MODIFY})
    // 值获取
    @Request(name = "id[]", method = RequestMethod.REQUEST)
    // 过滤
    @MsgSn(value = "validator.edit.idlist.fail",
    msgNative = "文章编号错误！")
    @Length(min = 1, max = 11)
    @Range(min = 1, max = Integer.MAX_VALUE)
    public void setIdList(int[] idList) {
        this.idList = idList;
    }

    /**
     * 页码
     * 可为空默认为 1
     * @param page
     */
    @RunSn({Actions.LIST, Actions.MODIFY})
    @Request(name = "page", method = RequestMethod.REQUEST)
    @AllowNull
    @Range(min = 1, max = Integer.MAX_VALUE)
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 标题长度允许范围在3至200。
     * @param title
     */
    @RunSn({Actions.DISPLAY, Actions.ADD, Actions.EDIT})
    @Request(name = "title", method = RequestMethod.POST)
    @Length(min = 3, max = 200, msgSn = "validator.edit.title.fail.nointherange",
    msgNative = "标题长度允许范围在3至200。")
    public void setTitle(String title) {
        this.title = title;
    }

    // 持久化
    /**
     *
     * @return
     */
    @Field(name = "author")
    public int getId() {
        return id;
    }

    // 持久化
    /**
     *
     * @return
     */
    @Field(name = "id")
    public int[] getIdList() {
        return idList;
    }

    /**
     *
     * @return
     */
    @Field(name = "author")
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return
     */
    @Field(name = "age", type = FieldType.number)
    public int getAge() {
        return age;
    }

    /**
     *
     * @return
     */
    @Field(name = "title")
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    public int getPage() {
        return page;
    }
}
