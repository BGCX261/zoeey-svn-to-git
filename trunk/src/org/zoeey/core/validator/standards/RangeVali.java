/*
 * MoXie (SysTem128@GMail.Com) 2009-8-5 14:57:53
 * $Id: RangeVali.java 74 2010-01-27 13:58:09Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.validator.standards;

import org.zoeey.core.validator.ValiAble;
import java.lang.annotation.Annotation;
import java.util.Map;
import org.zoeey.core.common.Supervisor;
import org.zoeey.core.util.ArrayHelper;
import org.zoeey.core.validator.SwitchLabel;
import org.zoeey.core.validator.ValiHelper;
import org.zoeey.core.validator.WebValidator;
import org.zoeey.core.validator.annotations.Conclusion;
import org.zoeey.core.validator.annotations.Range;
import org.zoeey.core.validator.annotations.MsgSn;

/**
 * 整数范围验证
 * @author MoXie
 */
public class RangeVali implements ValiAble {

    /**
     * 验证失败时是否保留取值
     */
    private boolean isRetain = true;

    /**
     *  
     * @param clazzes 当前方法的注解集合
     * @return
     */
    public boolean accept(Class<? extends Annotation>[] clazzes) {
        return ArrayHelper.inArray(clazzes, new Class<?>[]{Range.class});
    }

    /**
     * 结合方式
     * @return
     */
    public SwitchLabel swit() {
        return SwitchLabel.JOIN;
    }

    /**
     * 验证失败时是否保留取值
     * @return
     */
    public boolean isRetain() {
        return isRetain;
    }

    /**
     * 单值验证
     * @param svisor 监督者
     * @param annts 注解集合
     * @param value 待验证值
     * @return
     */
    public boolean vali(Supervisor svisor, Map<Class<? extends Annotation>, Annotation> annts,//
            String value) {
        boolean isPass = true;
        Range range = (Range) annts.get(Range.class);
        MsgSn msgSn = (MsgSn) annts.get(MsgSn.class);
        Conclusion conclusion = (Conclusion) annts.get(Conclusion.class);
        if (value == null //
                || !WebValidator.isIntegerString_Neg(value) //
                || Integer.parseInt(value) < range.min() //
                || Integer.parseInt(value) > range.max()) {
            isPass = false;
        }
        if (!isPass) {
            svisor.addStatus(ValiHelper.pickMsgSn(msgSn, range.msgSn())//
                    , ValiHelper.pickMsgNative(msgSn, range.msgNative()),
                    range.sign());
            if (conclusion != null) {
                svisor.setConclusion(conclusion.value());
            }
            {
                isRetain = range.retain();
            }
        }
        return isPass;
    }

    /**
     * 多值验证
     * @param svisor 监督者
     * @param annts 注解集合
     * @param values 待验证值
     * @return
     */
    public boolean vali(Supervisor svisor, Map<Class<? extends Annotation>, Annotation> annts//
            , String[] values) {
        boolean isPass = true;
        Range range = (Range) annts.get(Range.class);
        MsgSn msgSn = (MsgSn) annts.get(MsgSn.class);
        Conclusion conclusion = (Conclusion) annts.get(Conclusion.class);
        if (values == null) {
            isPass = false;
        }
        if (isPass) {
            String val;
            for (int i = 0; i < values.length; i++) {
                val = values[i];
                if (val == null //
                        || !WebValidator.isIntegerString(val) //
                        || Integer.parseInt(val) < range.min() //
                        || Integer.parseInt(val) > range.max()) {
                    isPass = false;
                    break;
                }
            }
        }
        if (!isPass) {
            svisor.addStatus(ValiHelper.pickMsgSn(msgSn, range.msgSn()), //
                    ValiHelper.pickMsgNative(msgSn, range.msgNative()),
                    range.sign());
            if (conclusion != null) {
                svisor.setConclusion(conclusion.value());
            }
        }
        return isPass;

    }
}
