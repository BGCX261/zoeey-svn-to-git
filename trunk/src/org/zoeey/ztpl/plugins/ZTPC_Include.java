/*
 * MoXie (SysTem128@GMail.Com) 2009-4-30 22:44:24
 * $Id: ZTPC_Include.java 71 2010-01-07 05:49:47Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.plugins;
 
import org.objectweb.asm.Opcodes;
import org.zoeey.ztpl.compiler.CompileAble;
import org.zoeey.ztpl.compiler.CompileBase;
import org.zoeey.ztpl.compiler.CompileTracker;
import org.zoeey.ztpl.Ztpl;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class ZTPC_Include extends CompileBase implements CompileAble, Opcodes {

    public void registerCompiler(CompileTracker ct, Ztpl tpl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
