/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: SampleLeaderBeanAble.java 75 2010-03-16 02:21:41Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.dataBean;

/**
 * 用于缩放Person基类为Leader
 * @author MoXie(SysTem128@GMail.Com)
 */
public interface SampleLeaderBeanAble extends SamplePersonBeanAble {

    /**
     *
     * @return
     */
    public boolean isLeader();
}
