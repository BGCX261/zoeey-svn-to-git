/*
 * MoXie (SysTem128@GMail.Com) 2009-7-15 17:21:10
 * $Id: User.java 71 2010-01-07 05:49:47Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.loader;

import org.zoeey.core.common.Conclusions;
import org.zoeey.core.loader.annotations.Request;
import org.zoeey.core.loader.fileupload.FileItem;
import org.zoeey.core.validator.annotations.Accessory;
import org.zoeey.core.validator.annotations.Conclusion;
import org.zoeey.core.validator.annotations.Email;

/**
 *
 * @author MoXie
 */
public class User {

    private String account;
    private String email;
    private int id;
    private int[] itemIds;
    private String[] historys;
    private FileItem avatar;
    private FileItem[] photos;

    /**
     *
     * @return
     */
    public String getAccount() {
        return account;
    }

    /**
     *
     * @param account
     */
    @Request(name = "account", method = RequestMethod.SESSION)
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    @Request(name = "email", method = RequestMethod.GET)
    @Email(msgSn = "user.email.unmatch", msgNative = "email填写有误请查证后再次提交。")
    @Conclusion(Conclusions.ERROR)
    public void setEmail(String email) {
        this.email = email;
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
    @Request(name = "id", method = RequestMethod.COOKIE)
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public int[] getItemIds() {
        return itemIds;
    }

    /**
     *
     * @param itemIds
     */
    @Request(name = "items", method = RequestMethod.GET)
    public void setItemIds(int[] itemIds) {
        this.itemIds = itemIds;
    }

    /**
     *
     * @return
     */
    public String[] getHistorys() {
        return historys;
    }

    /**
     *
     * @param historys
     */
    @Request(name = "historys", method = RequestMethod.POST)
    public void setHistorys(String[] historys) {
        this.historys = historys;
    }

    /**
     *
     * @return
     */
    public FileItem getAvatar() {
        return avatar;
    }

    /**
     *
     * @param avatar
     */
    @Request(name = "avatar", method = RequestMethod.FILE)
    public void setAvatar(FileItem avatar) {
        this.avatar = avatar;
    }

    /**
     *
     * @return
     */
    public FileItem[] getPhotos() {
        return photos;
    }

    /**
     *
     * @param photos
     */
    @Accessory( //
    types = {"image/png"}, msgSn_type = "user.photos.invType", msgNative_type = "第%d文件类型不再允许范围内。",
    sizeMin = 8, sizeMax = 32 * 1024 * 1024, msgSn_size = "user.photos.invSize", msgNative_size = "第%d文件大小应在8b到32兆间。",
    msgSn = "user.avatar.invfile", msgNative = "第%d文件没有被上传。")
    @Request(name = "photos", method = RequestMethod.FILE)
    public void setPhotos(FileItem[] photos) {
        this.photos = photos;
    }
}
