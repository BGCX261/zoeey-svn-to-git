/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: LoadHelper.java 75 2010-03-16 02:21:41Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.zoeey.core.common.ZObject;
import org.zoeey.core.loader.fileupload.FileItem;
import org.zoeey.core.loader.fileupload.UploadConfig;
import org.zoeey.core.loader.fileupload.Uploader;
import org.zoeey.core.route.ParamsParserAble;
import org.zoeey.core.route.Query;
import org.zoeey.core.route.exceptions.RouterConnectException;
import org.zoeey.core.util.ArrayHelper;
import org.zoeey.core.util.FieldEntry;
import org.zoeey.core.util.IpHelper;
import org.zoeey.core.util.QueryStringHelper;
import org.zoeey.core.util.StringHelper;
import org.zoeey.core.util.TextFileHelper;
import org.zoeey.core.util.UrlHelper;

/**
 * 传入资源读取器
 * @author MoXie(SysTem128@GMail.Com)
 */
public class LoadHelper {

    /**
     * 请求对象
     */
    private HttpServletRequest request;
    /**
     * GET 方式传递的参数
     */
    private Collection<Entry<String, String>> queryGETCollection = null;
    /**
     * POST 方式传递的参数
     */
    private Collection<Entry<String, String>> queryPOSTCollection = null;
    /**
     * 是否为
     */
    private boolean isMultipart = false;
    /**
     * 上传组件
     */
    private Uploader uploader = null;
    /**
     * 配置信息
     */
    UploadConfig config = null;

    /**
     * 处理包<b>不</b>含文件上传的提交
     * @param request
     * @throws IOException
     */
    public LoadHelper(HttpServletRequest request) throws IOException {
        this.request = request;
        this.isMultipart = false;
        queryGETCollection = new ArrayList<Entry<String, String>>();
        queryPOSTCollection = new ArrayList<Entry<String, String>>();
        initGet();
        initPost();
    }

    /**
     * 处理包含文件上传的提交
     * @param request
     * @param config
     * @throws IOException
     */
    public LoadHelper(HttpServletRequest request, UploadConfig config) throws IOException {
        this.request = request;
        queryGETCollection = new ArrayList<Entry<String, String>>();
        queryPOSTCollection = new ArrayList<Entry<String, String>>();
        initGet();
        if (config != null) {
            this.config = config;
        } else {
            this.config = new UploadConfig();
        }
        initMultipartPost();

    }

    /**
     * 设置路由信息，参数可从GET中取出
     * @param parser  分析器
     * @param query    Router 请求项
     * @throws RouterConnectException
     */
    public void setRoute(ParamsParserAble parser, Query query) throws RouterConnectException {
        setRoute(parser.parse(query).getEntryCollection());
    }

    /**
     * 设置路由信息，参数可从GET中取出
     * @param fieldList
     * @throws RouterConnectException
     */
    public void setRoute(Collection<Entry<String, String>> fieldList) throws RouterConnectException {
        for (Entry<String, String> field : fieldList) {
            queryGETCollection.add(new FieldEntry<String, String>(field.getKey(),//
                    UrlHelper.decode(field.getValue(), request.getCharacterEncoding())));
        }
    }

    /**
     * 初始化 GET 方式传递的字段
     */
    private void initGet() {
        String queryString = request.getQueryString();
        if (queryString == null) {
            return;
        }
        queryGETCollection.addAll(QueryStringHelper.getEntryList(queryString, request.getCharacterEncoding()));
    }

    /**
     * 初始化 POST 方式传递的字段
     */
    private void initPost() throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        if (inputStream == null) {
            return;
        }
        queryPOSTCollection = QueryStringHelper.getEntryList(TextFileHelper.read(inputStream//
                , request.getCharacterEncoding()), request.getCharacterEncoding());
    }

    /**
     * 初始化 POST 方式传递的字段 包含文件上传
     * @throws IOException
     */
    private void initMultipartPost() throws IOException {
        uploader = new Uploader(request, config);
        if (!uploader.isMultipart()) {
            isMultipart = false;
            initPost();
            return;
        }
        isMultipart = true;
    }

    /**
     * 获取上传组件
     * @return
     */
    public Uploader getUploader() {
        return uploader;
    }

    /**
     * 是否为多段型数据（文件上传）
     * @return
     */
    public boolean isMultipart() {
        return this.isMultipart;
    }

    /**
     * 获取GET方式传递的参数，包括由Router传入的参数。
     * @param name 字段名
     * @return  没有找到该字段时返回 null 
     */
    public String getGet(String name) {
        for (Entry<String, String> field : queryGETCollection) {
            if (name.equals(field.getKey())) {
                return field.getValue();
            }
        }
        return null;
    }

    /**
     * 获取POST方式传递的参数，包括文件上传时的文本字段(type=text)
     * @param name
     * @return 没有找到该字段时返回 null 
     */
    public String getPost(String name) {
        if (isMultipart) {
            return uploader.getParamenter(name);
        } else {
            if (name == null) {
                return null;
            }
            for (Entry<String, String> field : queryPOSTCollection) {
                if (name.equals(field.getKey())) {
                    return field.getValue();
                }
            }
            return null;
        }
    }

    /**
     * 获取 COOKIE 方式传递的字段
     * @param name 字段名称
     * @return 没有找到该字段时返回 null 
     */
    public String getCookie(String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 获取session内存储的字段
     * @param name  字段名称
     * @return  没有找到该字段时返回 null 
     */
    public String getSession(String name) {
        HttpSession session = request.getSession();
        if (session != null) {
            return ZObject.conv(session.getAttribute(name)).toString();
        }
        return null;
    }

    /**
     * 获取文件字段
     * @param name 字段名称
     * @return 没有找到该字段时返回 null
     */
    public FileItem getFile(String name) {
        if (isMultipart == false) {
            return null;
        }
        return uploader.getFileItem(name);
    }

    /**
     * 分别获取 GET 或 POST 或 COOKIE 方式传递的字段
     * @param name  字段名称
     * @return  没有找到该字段时返回 null 
     */
    public String getRequest(String name) {
        String str = null;
        str = getGet(name);
        if (str != null) {
            return str;
        }
        str = getPost(name);
        if (str != null) {
            return str;
        }
        str = getCookie(name);
        if (str != null) {
            return str;
        }
        return null;
    }

    /**
     * 获取 <b>GET</b> 方式传递的同键多值参数
     * @param name  字段名称
     * @return  没有找到该字段时返回 null
     */
    public String[] getGets(String name) {
        String[] values = new String[queryGETCollection.size()];
        int i = 0;
        for (Entry<String, String> field : queryGETCollection) {
            if (name.equals(field.getKey())) {
                values[i] = field.getValue();
                i++;
            }
        }
        if (i == 0) {
            return null;
        }
        return ArrayHelper.copyOf(values, i);
    }

    /**
     * 获取 <b>POST</b> 方式传递的同键多值参数
     * @param name  字段名称
     * @return  没有找到该字段时返回 null
     */
    public String[] getPosts(String name) {
        if (!isMultipart) {
            String[] values = new String[queryPOSTCollection.size()];
            int i = 0;
            for (Entry<String, String> field : queryPOSTCollection) {
                if (name.equals(field.getKey())) {
                    values[i] = field.getValue();
                    i++;
                }
            }
            if (i == 0) {
                return null;
            }
            return ArrayHelper.copyOf(values, i);
        } else {
            List<String> list = uploader.getParamenterValueList(name);
            /**
             * fixedbug: list is null
             */
            if (list == null) {
                return null;
            }
            String[] values = new String[list.size()];
            int i = 0;
            for (String str : list) {
                values[i] = str;
                i++;
            }
            if (i == 0) {
                return null;
            }
            return ArrayHelper.copyOf(values, i);
        }
    }

    /**
     * 获取 <b>COOKIE</b> 方式传递的同键多值参数
     * @param name  字段名称
     * @return  没有找到该字段时返回 null
     */
    public String[] getCookies(String name) {

        Cookie[] cookies = request.getCookies();
        String[] values = new String[0];
        if (cookies != null) {
            int i = 0;
            values = new String[cookies.length];
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    values[i] = cookie.getValue();
                    i++;
                }
            }
            if (i == 0) {
                return null;
            }
            return ArrayHelper.copyOf(values, i);
        }
        return null;

    }

    /**
     * 获取 <b>GET 或 POST 或 COOKIE</b> 方式传递的同键多值参数
     * @param name  字段名称
     * @return  没有找到该字段时返回 空数组(new String[0])
     */
    public String[] getRequests(String name) {
        String[] strs = getGets(name);
        if (strs != null) {
            return strs;
        }
        strs = getPosts(name);
        if (strs != null) {
            return strs;
        }
        strs = getCookies(name);
        if (strs != null) {
            return strs;
        }
        return null;
    }

    /**
     * 获取单键多值的文件字段
     * @param name  字段名称
     * @return  没有找到该字段时返回 null
     */
    public FileItem[] getFiles(String name) {
        if (isMultipart == false) {
            return null;
        }
        List<FileItem> list = uploader.getFileItemValueList(name);
        if (list == null) {
            return null;
        }
        return list.toArray(new FileItem[list.size()]);
    }

    /**
     * 获取请求头字段信息
     * @param name 字段名称
     * @return  没有找到该字段时返回 null
     */
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    /**
     * 获取请求头部信息
     * @param name 字段名
     * @return  没有找到该字段时返回 null 
     */
    public String[] getHeaders(String name) {
        List<String> headers = new ArrayList<String>();
        @SuppressWarnings("unchecked")
        Enumeration<String> enumeration = request.getHeaders(name);
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                headers.add(enumeration.nextElement());
            }
        }
        return headers.toArray(new String[headers.size()]);
    }

    /**
     * 获取用户IP地址
     * 注意：并不保证绝对有效可信。
     *      仅支持IPv4
     * @return  未能获取时返回 0L
     */
    public long getClientIp() {
        long clientIp = 0L;
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            /**
             * 多重反向代理
             */
            if (ip != null && ip.indexOf(',') > -1) {
                String[] ips = StringHelper.split(ip, ',');
                if (ips.length > 0) {
                    ip = ips[0];
                }
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // bugfix:127.0.0.1 separator:"."
        if (StringHelper.split(ip, '.').length == 4) {
            clientIp = IpHelper.toLong(ip);
        }
        return clientIp;
    }

    /**
     * 获取载入的Request对象
     * @return
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * 查验提交方式是否为<b>POST</b>
     * @return
     */
    public boolean isPosted() {
        return isMethod("POST");
    }

    /**
     * 查验提交方式，大小写不敏感
     * @param method 提交方式
     * @return
     */
    public boolean isMethod(String method) {
        return request.getMethod().equalsIgnoreCase(method);
    }
}
