/*
 * MoXie (SysTem128@GMail.Com) 2009-5-2 16:40:43
 * $Id: ZTPC_If.java 71 2010-01-07 05:49:47Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.plugins;
 
import org.objectweb.asm.Opcodes;
import org.zoeey.ztpl.compiler.CompileAble;
import org.zoeey.ztpl.compiler.CompileTracker;
import org.zoeey.ztpl.Ztpl;
import org.zoeey.ztpl.compiler.CompileBase;

/**
 * ZTP = Zoeey Template Plugin - compile
 * @author MoXie(SysTem128@GMail.Com)
 */
public class ZTPC_If extends CompileBase implements CompileAble, Opcodes {

    public void registerCompiler(CompileTracker ct, Ztpl tpl) {
        ct.write("/* if statement start */");
        ct.write("/* /if statement end */");
    }
}
