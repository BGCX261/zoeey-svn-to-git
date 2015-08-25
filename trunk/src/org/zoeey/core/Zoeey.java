/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: Zoeey.java 78 2010-04-30 02:10:36Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.zoeey.core.common.container.ServletHolder;
import org.zoeey.core.common.container.ServletResource;
import org.zoeey.core.constant.ConfigConstants;
import org.zoeey.core.route.PublishAble;
import org.zoeey.core.route.Query;
import org.zoeey.core.route.Router;
import org.zoeey.core.route.exceptions.PublisherClassNoFoundException;
import org.zoeey.core.route.exceptions.RouterParserException;
import org.zoeey.core.util.DirInfo;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class Zoeey extends HttpServlet {

    /**
     * generated by LuckyUUID
     */
    private static final long serialVersionUID = -9029696980038966688L;
    private boolean isSyncPublish = false;

    /**
     * 初始化设置<br />
     * zoeey_mapping 参数指定了Publish映射文件的位置。<br />
     * 如未设定则使用 WEB-INF/zoeey/mapping.xml<br />
     * 可使用变量 {webRoot}<br />
     * @param config  包括
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            /**
             * from xml file
             */
            /**
             * fixed: glassfish class directory error
             */
            DirInfo.setDeployDir(getServletContext());

            String mappingPath = config.getInitParameter(ConfigConstants.INIT_PARAMS_MAPPING);
            if (mappingPath == null) {
                mappingPath = DirInfo.getWebInfoFile("/zoeey/mapping.xml");
            } else {
                mappingPath = mappingPath.replace("{webRoot}", DirInfo.getWebDir());
            }
            Router.init(new File(mappingPath));
            /**
             *
             */
            isSyncPublish = "true".equalsIgnoreCase(config.getInitParameter("syncPublish"));
        } catch (RouterParserException ex) {
            Logger.getLogger(Zoeey.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * <pre>
     * 需要发布的页面未找到，则自动转发到初始化参数zoeey_nopublish所制定的页面
     * </pre>
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            /**
             * 整理资源
             */
            ServletResource resource = new ServletResource();
            resource.setHttpServlet(this);
            resource.setRequest(request);
            resource.setResponse(response);
            ServletHolder.set(resource);
            /**
             * 处理请求
             */
            Query query = Router.parse(request);
            if (query != null) {
                PublishAble publish = query.getEntry().getPublish();
                if (publish != null) {
                    if (!isSyncPublish) {
                        publish.publish(request, response, query);
                    } else {
                        synchronized (publish) {
                            publish.publish(request, response, query);
                        }
                    }

                }
            }
        } catch (PublisherClassNoFoundException ex) {
            response.sendRedirect(getServletConfig().getInitParameter(ConfigConstants.INIT_PARAMS_NOPUBLISH));
        } finally {
            ServletHolder.clear();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet 方法。单击左侧的 + 号以编辑代码。">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
        processRequest(request, response);

    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
        processRequest(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return
     */
    @Override
    public String getServletInfo() {
        return "Zoeey Servlet";
    }
// </editor-fold>
}
