/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: Loader.java 76 2010-03-19 03:21:03Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.loader;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.zoeey.core.common.Supervisor;
import org.zoeey.core.common.WideCast;
import org.zoeey.core.common.ZObject;
import org.zoeey.core.loader.annotations.Request;
import org.zoeey.core.loader.exceptions.LoaderException;
import org.zoeey.core.loader.fileupload.FileItem;
import org.zoeey.core.loader.fileupload.UploadConfig;
import org.zoeey.core.resource.ResourceExceptionMsg;
import org.zoeey.core.route.ParamsParserAble;
import org.zoeey.core.route.ParamsStandardParser;
import org.zoeey.core.route.Query;
import org.zoeey.core.route.RouterRule;
import org.zoeey.core.route.exceptions.RouterConnectException;
import org.zoeey.core.util.FileHelper;
import org.zoeey.core.util.ReflectCacheHelper;
import org.zoeey.core.validator.SwitchLabel;
import org.zoeey.core.validator.standards.FileItemVali;
import org.zoeey.core.validator.ValiAble;
import org.zoeey.core.validator.ValiFileAble;

/**
 * 数据读取器
 * @author MoXie
 */
public class Loader {

    /**
     * 读取辅助类
     */
    private LoadHelper loadHelper = null;
    /**
     * 全局监控者
     */
    private Supervisor svisor = null;
    /**
     * 验证器
     */
    private ValiAble[] valis = null;
    /**
     * 富化器
     */
    private Richer richer = null;

    /**
     * 数据读取
     * @param request   请求对象
     * @param config    上传设置
     * @throws IOException
     */
    public Loader(HttpServletRequest request, UploadConfig config) //
            throws IOException {
        loadHelper = new LoadHelper(request, config);
        svisor = new Supervisor();
    }

    /**
     * 数据读取
     * @param request   请求对象
     * @throws IOException
     */
    public Loader(HttpServletRequest request) //
            throws IOException {
        loadHelper = new LoadHelper(request);
        svisor = new Supervisor();
    }

    /**
     * 设置全局监控者和验证器
     * @param svisor
     * @param valis
     */
    public void setValiVisor(Supervisor svisor, ValiAble[] valis) {
        this.svisor = svisor;
        this.valis = valis.clone();
    }

    /**
     * 设置全局监控者
     * @param svisor
     * @return
     */
    public Loader setSvisor(Supervisor svisor) {
        this.svisor = svisor;
        return this;
    }

    /**
     * 设置验证器
     * @param valis
     * @return
     */
    public Loader setValis(ValiAble[] valis) {
        if (valis != null) {
            this.valis = valis.clone();
        }
        return this;
    }

    /**
     * 设置富化者
     * @param richer
     * @return
     */
    public Loader setRicher(Richer richer) {
        this.richer = richer;
        return this;
    }

    /**
     * 设置路由信息，参数可从GET中取出
     * @param query
     * @param rule
     * @throws RouterConnectException
     */
    public void setRoute(Query query, String rule) throws RouterConnectException {
        ParamsParserAble parser = new ParamsStandardParser();
        parser.connect(rule);
        loadHelper.setRoute(parser, query);
    }

    /**
     * 设置路由信息，参数可从GET中取出
     * @param query
     * @param routerRule
     * @throws RouterConnectException
     */
    public void setRoute(Query query, RouterRule routerRule) throws RouterConnectException {
        ParamsParserAble parser = new ParamsStandardParser();
        parser.connect(routerRule);
        loadHelper.setRoute(parser, query);
    }

    /**
     * 设置路由信息，参数可从GET中取出
     * @param parser
     * @param query
     * @param routerRule
     * @throws RouterConnectException
     */
    public void setRoute(ParamsParserAble parser, Query query, RouterRule routerRule) //
            throws RouterConnectException {
        parser.connect(routerRule);
        parser.parse(query);
        loadHelper.setRoute(parser.getEntryCollection());
    }

    /**
     * 设置路由信息，参数可从GET中取出
     * @param fieldList
     * @throws RouterConnectException
     */
    public void setRoute(List<Entry<String, String>> fieldList) //
            throws RouterConnectException {
        loadHelper.setRoute(fieldList);
    }

    /**
     * 填充数据模型
     * @see Request
     * @param <T>   
     * @param model 数据模型
     * @throws LoaderException
     */
    public <T> void load(T model) throws LoaderException {
        // <editor-fold defaultstate="collapsed" desc="从模型中提取字段名并载入数据。">
        if (model == null) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_MODEL_NULL);
        }
        ReflectCacheHelper relectCache = ReflectCacheHelper.get(model.getClass());
        Map<Method, Request> methodMap = relectCache.<Request>getAnnotationPresentedMethodMap(Request.class);
        /**
         * request 标注
         */
        Request request;
        /**
         * Bean SET 方法的参数列表
         */
        Class<?>[] argTypes;
        /**
         * Bean SET 方法的第一个参数
         */
        Class<?> argType;
        /**
         * 参数是否为数组
         */
        boolean isArray;
        /**
         * 字段名
         */
        String fieldName;

        /**
         * 两种参数值
         */
        String value;
        String[] values;
        FileItem valueFile;
        FileItem[] valueFiles;
        /**
         * SET 方法标注列表
         */
        Class<? extends Annotation>[] annots;
        /**
         * 验证器验证结果
         */
        boolean isPass = true;
        boolean isRetain = true;
        Method method = null;
        RequestMethod valuedMethod = null;
        for (Entry<Method, Request> entry : methodMap.entrySet()) {
            method = entry.getKey();
            isRetain = true;
            isArray = false;
            argTypes = method.getParameterTypes();
            if (argTypes == null || argTypes.length != 1) {
                continue;
            } else {
                argType = argTypes[0];
                isArray = argType.isArray();
            }
            request = entry.getValue();
            /**
             * 初始化各种参数值
             */
            value = null;
            values = null;
            valueFile = null;
            valueFiles = null;
            valuedMethod = null;
            fieldName = request.name();
            /**
             * 取值
             */
            selectMethod:
            for (RequestMethod reqMethod : request.method()) {
                switch (reqMethod) {
                    case REQUEST:
                        if (isArray) {
                            values = loadHelper.getRequests(fieldName);
                        } else {
                            value = loadHelper.getRequest(fieldName);
                        }
                        break;
                    case GET:
                        if (isArray) {
                            values = loadHelper.getGets(fieldName);
                        } else {
                            value = loadHelper.getGet(fieldName);
                        }
                        break;
                    case POST:
                        if (isArray) {
                            values = loadHelper.getPosts(fieldName);
                        } else {
                            value = loadHelper.getPost(fieldName);
                        }
                        break;
                    case COOKIE:
                        if (isArray) {
                            values = loadHelper.getCookies(fieldName);
                        } else {
                            value = loadHelper.getCookie(fieldName);
                        }
                        break;
                    case SESSION:
                        if (isArray) {
                            /**
                             * session 不可传递数组
                             */
                            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_ARGINVALID);
                        } else {
                            value = loadHelper.getSession(fieldName);
                        }
                        break;
                    case FILE:
                        if (isArray) {
                            valueFiles = loadHelper.getFiles(fieldName);
                        } else {
                            valueFile = loadHelper.getFile(fieldName);
                        }
                        break;
                    case HEADER:
                        if (isArray) {
                            values = loadHelper.getHeaders(fieldName);
                        } else {
                            value = loadHelper.getHeader(fieldName);
                        }
                        break;
                    case CLIENT_IP:
                        if (isArray) {
                            /**
                             * CLIENT_IP 不可传递数组
                             */
                            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_ARGINVALID);
                        } else {
                            value = String.valueOf(loadHelper.getClientIp());
                        }
                        break;
                    default:
                        break;
                }
                if (RequestMethod.FILE != reqMethod) {
                    if (isArray) {
                        valuedMethod = reqMethod;
                        if (values != null && values.length > 0) {
                            break selectMethod;
                        }
                    } else {
                        valuedMethod = reqMethod;
                        if (value != null) {
                            break selectMethod;
                        }
                    }
                } else {
                    if (isArray) {
                        valuedMethod = reqMethod;
                        if (valueFiles != null && valueFiles.length > 0) {
                            break selectMethod;
                        }
                    } else {
                        valuedMethod = reqMethod;
                        if (valueFile != null) {
                            break selectMethod;
                        }
                    }
                }
            } // RequestMethod选择结束

            if (valuedMethod == null) {
                continue;
            }
            /**
             * 富化
             */
            if (richer != null) {
                richer.setLoader(this);
                richer.setSvisor(svisor);
                if (RequestMethod.FILE == valuedMethod) {
                    if (isArray) {
                        valueFiles = richer.rich(fieldName, valueFiles);
                    } else {
                        valueFile = richer.rich(fieldName, valueFile);
                    }
                } else {
                    if (isArray) {
                        values = richer.rich(fieldName, values);
                    } else {
                        value = richer.rich(fieldName, value);
                    }
                }
            }

            /**
             * 初始验证结果
             */
            isPass = true;
            /**
             * 
             */
            if (RequestMethod.FILE == valuedMethod) {
                /**
                 * 
                 */
                annots = relectCache.getMethodAnnotationTypes(method);
                ValiFileAble valiFile = new FileItemVali();
                if (valiFile.accept(annots)) {
                    if (isArray) {
                        isPass = valiFile.vali(svisor, relectCache.getMethodAnnotationMap(method), valueFiles);
                    } else {
                        isPass = valiFile.vali(svisor, relectCache.getMethodAnnotationMap(method), valueFile);
                    }
                    if (valiFile.swit() == SwitchLabel.ASSERT || valiFile.swit() == SwitchLabel.ALLOWNULL_ASSERT) {
                        break;
                    }
                }
                if (isPass) {
                    if (isArray) {
                        insFileValue(model, method, argType, valueFiles);
                    } else {
                        insFileValue(model, method, argType, valueFile);
                    }
                }
            } else {
                insValueLoop:
                do {
                    if (valis != null) {
                        annots = relectCache.getMethodAnnotationTypes(method);
                        ValiAble vali;
                        for (int i = 0; i < valis.length; i++) {
                            vali = valis[i];
                            if (vali != null) {
                                if (vali.accept(annots)) {
                                    if (isArray) {
                                        isPass &= vali.vali(svisor, relectCache.getMethodAnnotationMap(method), values);
                                    } else {
                                        isPass &= vali.vali(svisor, relectCache.getMethodAnnotationMap(method), value);
                                    }
                                    isRetain &= vali.isRetain();
                                    if (vali.swit() == SwitchLabel.ASSERT) {
                                        break;
                                    }
                                    if (vali.swit() == SwitchLabel.ALLOWNULL_ASSERT) {
                                        break insValueLoop;
                                    }
                                }

                            }
                        }
                    }
                    if (isPass || isRetain) {
                        if (isArray) {
                            insValue(model, method, argType, values);
                        } else {
                            insValue(model, method, argType, value);
                        }
                    }
                } while (false);
            }

        }
        // </editor-fold>
    }

    /**
     * 装载单字符串
     * @param method    单参数方法
     * @param argType   参数类型
     * @param value     值
     * @throws LoadMethodsException
     */
    private void insValue(Object model, Method method, Class<?> argType, String value)
            throws LoaderException {
        try {
            if (value == null) {
                return;
            }
            if (String.class.isAssignableFrom(argType)) {
                method.invoke(model, value);
            } else if (Integer.class.isAssignableFrom(argType) || int.class.isAssignableFrom(argType)) {
                method.invoke(model, new ZObject(value).toInteger());
            } else if (Long.class.isAssignableFrom(argType) || long.class.isAssignableFrom(argType)) {
                method.invoke(model, new ZObject(value).toLong());
            } else if (Double.class.isAssignableFrom(argType) || double.class.isAssignableFrom(argType)) {
                method.invoke(model, new ZObject(value).toDouble());
            } else if (Short.class.isAssignableFrom(argType) || short.class.isAssignableFrom(argType)) {
                method.invoke(model, new ZObject(value).toShort());
            } else {
                throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_ARGINVALID);
            }
        } catch (IllegalAccessException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_NOACCESS);
        } catch (IllegalArgumentException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_ARGINVALID);
        } catch (InvocationTargetException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_EXUNCATCHED);
        }
    }

    /**
     * 装载多字符串值
     * @param method    单参数方法
     * @param argType   参数类型
     * @param values    值
     * @throws LoadMethodsException
     */
    private void insValue(Object model, Method method, Class<?> argType, String[] values)
            throws LoaderException {
        try {
            if (values == null) {
                return;
            }
            argType = argType.getComponentType();
            if (String.class.isAssignableFrom(argType)) {
                method.invoke(model, new Object[]{values});
            } else if (Integer.class.isAssignableFrom(argType) || int.class.isAssignableFrom(argType)) {
                method.invoke(model, new Object[]{WideCast.castToInteger(values)});
            } else if (Long.class.isAssignableFrom(argType) || long.class.isAssignableFrom(argType)) {
                method.invoke(model, new Object[]{WideCast.castToLong(values)});
            } else if (Double.class.isAssignableFrom(argType) || double.class.isAssignableFrom(argType)) {
                method.invoke(model, new Object[]{WideCast.castToDouble(values)});
            } else if (Short.class.isAssignableFrom(argType) || short.class.isAssignableFrom(argType)) {
                method.invoke(model, new Object[]{WideCast.castToShort(values)});
            } else {
                throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_ARGINVALID);
            }
        } catch (IllegalAccessException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_NOACCESS);
        } catch (IllegalArgumentException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_ARGINVALID);
        } catch (InvocationTargetException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_EXUNCATCHED);
        }
    }

    /**
     * 装载单文件值
     * @param method 单参数方法
     * @param argType   参数类型
     * @param value     值
     * @throws LoadMethodsException
     */
    private void insFileValue(Object model, Method method, Class<?> argType, FileItem value) //
            throws LoaderException {
        try {
            if (value == null) {
                return;
            }
            if (FileItem.class.isAssignableFrom(argType)) {
                method.invoke(model, value);
            } else {
                /**
                 * 仅允许使用 FileItem 类型
                 */
                throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_ARGINVALID);
            }
        } catch (IllegalAccessException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_NOACCESS);
        } catch (IllegalArgumentException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_ARGINVALID);
        } catch (InvocationTargetException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_EXUNCATCHED);
        }
    }

    /**
     * 装载多文件值
     * @param method
     * @param argType
     * @param values
     * @throws LoadMethodsException
     */
    private void insFileValue(Object model, Method method, Class<?> argType, FileItem[] values) //
            throws LoaderException {
        try {
            if (values == null) {
                return;
            }
            argType = argType.getComponentType();
            if (FileItem.class.isAssignableFrom(argType)) {
                method.invoke(model, new Object[]{values});
            } else {
                /**
                 * 仅允许使用 FileItem 类型
                 */
                throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_ARGINVALID);
            }
        } catch (IllegalAccessException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_NOACCESS);
        } catch (IllegalArgumentException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_ARGINVALID);
        } catch (InvocationTargetException ex) {
            throw new LoaderException(ResourceExceptionMsg.LOADER_METHOD_EXUNCATCHED);
        }
    }

    /**
     * loaderHelper Adapter
     */
    /**
     * 获取GET方式传递的参数，包括由Router传入的参数。
     * @param name 字段名
     * @return  没有找到该字段时返回 null
     */
    public String getGet(String name) {
        return loadHelper.getGet(name);
    }

    /**
     * 获取POST方式传递的参数，包括文件上传时的文本字段(type=text)
     * @param name
     * @return 没有找到该字段时返回 null
     */
    public String getPost(String name) {
        return loadHelper.getPost(name);
    }

    /**
     * 获取 COOKIE 方式传递的字段
     * @param name 字段名称
     * @return 没有找到该字段时返回 null
     */
    public String getCookie(String name) {
        return loadHelper.getCookie(name);
    }

    /**
     * 获取session内存储的字段
     * @param name  字段名称
     * @return  没有找到该字段时返回 null
     */
    public String getSession(String name) {
        return loadHelper.getSession(name);
    }

    /**
     * 获取文件字段
     * @param name 字段名称
     * @return 没有找到该字段时返回 null
     */
    public FileItem getFile(String name) {
        return loadHelper.getFile(name);
    }

    /**
     * 分别获取 GET 或 POST 或 COOKIE 方式传递的字段
     * @param name  字段名称
     * @return  没有找到该字段时返回 null
     */
    public String getRequest(String name) {
        return loadHelper.getRequest(name);
    }

    /**
     * 获取 <b>GET</b> 方式传递的同键多值参数
     * @param name  字段名称
     * @return  没有找到该字段时返回 空数组(new String[0])
     */
    public String[] getGets(String name) {
        return loadHelper.getGets(name);
    }

    /**
     * 获取 <b>POST</b> 方式传递的同键多值参数
     * @param name  字段名称
     * @return  没有找到该字段时返回 空数组(new String[0])
     */
    public String[] getPosts(String name) {
        return loadHelper.getPosts(name);
    }

    /**
     * 获取 <b>COOKIE</b> 方式传递的同键多值参数
     * @param name  字段名称
     * @return  没有找到该字段时返回 空数组(new String[0])
     */
    public String[] getCookies(String name) {
        return loadHelper.getCookies(name);
    }

    /**
     * 获取 <b>GET 或 POST 或 COOKIE</b> 方式传递的同键多值参数
     * @param name  字段名称
     * @return  没有找到该字段时返回 空数组(new String[0])
     */
    public String[] getRequests(String name) {
        return loadHelper.getRequests(name);
    }

    /**
     * 获取单键多值的文件字段
     * @param name  字段名称
     * @return  没有找到该字段时返回 空数组(new FileItem[0])
     */
    public FileItem[] getFiles(String name) {
        return loadHelper.getFiles(name);
    }

    /**
     * 获取请求头字段信息
     * @param name 字段名称
     * @return  没有找到该字段时返回 null
     */
    public String getHeader(String name) {
        return loadHelper.getHeader(name);
    }

    /**
     * 获取请求头部信息
     * @param name 字段名
     * @return  没有找到该字段时返回 null
     */
    public String[] getHeaders(String name) {
        return loadHelper.getHeaders(name);
    }

    /**
     * 获取用户IP地址
     * 注意：并不保证绝对有效可信。
     *      仅支持IPv4
     * @return  未能获取时返回 0
     */
    public long getClientIp() {
        return loadHelper.getClientIp();
    }

    /**
     * 查验提交方式是否为<b>POST</b>
     * @return
     */
    public boolean isPosted() {
        return loadHelper.isPosted();
    }

    /**
     * 是否为多段型数据（文件上传）
     * @return
     */
    public boolean isMultipart() {
        return loadHelper.isMultipart();
    }

    /**
     * 查验提交方式，大小写不敏感
     * @param method 提交方式
     * @return
     */
    public boolean isMethod(String method) {
        return loadHelper.isMethod(method);
    }

    /**
     * 读取辅助类
     * @return
     */
    public LoadHelper getLoadHelper() {
        return loadHelper;
    }

    /**
     * 清理临时文件
     */
    public void clearTempFile() {
        if (!isMultipart()) {
            return;
        }
        Map<String, List<FileItem>> map = loadHelper.getUploader().getFileMap();
        if (map == null) {
            return;
        }
        Collection<List<FileItem>> fileItemListCol = map.values();
        File tempFile;
        for (List<FileItem> fileItemList : fileItemListCol) {
            if (fileItemList != null) {
                for (FileItem fileItem : fileItemList) {
                    if (fileItem != null) {
                        tempFile = fileItem.getTempFile();
                        if (tempFile != null && tempFile.exists()) {
                            FileHelper.tryDelete(tempFile);
                        }
                    }
                }
            }
        }
    }
}
