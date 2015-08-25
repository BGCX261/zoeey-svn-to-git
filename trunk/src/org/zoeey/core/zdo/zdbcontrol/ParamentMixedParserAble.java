/*
 * MoXie (SysTem128@GMail.Com) 2009-5-25 2:16:16
 * $Id: ParamentMixedParserAble.java 66 2009-08-20 07:30:24Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.zdo.zdbcontrol;

import java.util.List;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public interface ParamentMixedParserAble extends ParamentNamedParserAble {

    /**
     * 键列表。
     * @param index
     * @return
     */
    public List<Integer> listIndexOf(int index);
}
