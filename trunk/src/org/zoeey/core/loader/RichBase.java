/*
 * MoXie (SysTem128@GMail.Com) 2009-9-2 10:37:14
 * $Id: RichBase.java 71 2010-01-07 05:49:47Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.loader;

import org.zoeey.core.common.Supervisor;

/**
 * 富化器
 * @author MoXie
 */
public abstract class RichBase implements RichAble {

    /**
     * 读取器
     */
    private Loader loader = null;
    /**
     * 全局监督者
     */
    private Supervisor svisor = null;

    /**
     * 读取器
     * @return  读取器
     */
    public Loader getLoader() {
        return loader;
    }

    /**
     * 读取器
     * @param loader    读取器
     */
    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    /**
     * 全局监督者
     * @return  全局监督者
     */
    public Supervisor getSvisor() {
        return svisor;
    }

    /**
     * 全局监督者
     * @param svisor    全局监督者
     */
    public void setSvisor(Supervisor svisor) {
        this.svisor = svisor;
    }
}
