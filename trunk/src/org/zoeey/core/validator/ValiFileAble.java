/*
 * MoXie (SysTem128@GMail.Com) 2009-7-31 17:13:42
 * $Id: ValiFileAble.java 71 2010-01-07 05:49:47Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.validator;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.zoeey.core.common.Supervisor;
import org.zoeey.core.loader.fileupload.FileItem;

/**
 * 上传文件项验证
 * @author MoXie
 */
public interface ValiFileAble {

    /**
     *
     * @param clazzes
     * @return
     */
    public boolean accept(Class<? extends Annotation>[] clazzes);

    /**
     *
     * @return
     */
    public SwitchLabel swit();

    /**
     *
     * @param svisor
     * @param annts
     * @param value
     * @return
     */
    public boolean vali(Supervisor svisor, Map<Class<? extends Annotation>, Annotation> annts, FileItem value);

    /**
     *
     * @param svisor
     * @param annts
     * @param values
     * @return
     */
    public boolean vali(Supervisor svisor, Map<Class<? extends Annotation>, Annotation> annts, FileItem[] values);
}
