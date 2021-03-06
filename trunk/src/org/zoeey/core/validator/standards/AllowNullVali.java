/*
 * MoXie (SysTem128@GMail.Com) 2009-8-5 12:25:55
 * $Id: AllowNullVali.java 74 2010-01-27 13:58:09Z MoXie $
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
import org.zoeey.core.validator.annotations.AllowNull;

/**
 *
 * @author MoXie
 */
public class AllowNullVali implements ValiAble {

    private SwitchLabel swit = SwitchLabel.ALLOWNULL_ASSERT;
    private boolean isRetain = false;

    /**
     *
     * @param clazzes
     * @return
     */
    public boolean accept(Class<? extends Annotation>[] clazzes) {
        return ArrayHelper.inArray(clazzes, new Class<?>[]{AllowNull.class});
    }

    /**
     *
     * @return
     */
    public SwitchLabel swit() {
        return swit;
    }

    /**
     *
     * @return
     */
    public boolean isRetain() {
        return isRetain;
    }

    /**
     *
     * @param svisor
     * @param annts
     * @param value
     * @return
     */
    public boolean vali(Supervisor svisor, Map<Class<? extends Annotation>, Annotation> annts, String value) {
        boolean isPass = true;
        if (value == null || value.length() == 0) {
            swit = SwitchLabel.ALLOWNULL_ASSERT;
        } else {
            swit = SwitchLabel.JOIN;
        }

        {
            isRetain = false;
        }

        return isPass;
    }

    /**
     *
     * @param svisor
     * @param annts
     * @param values
     * @return
     */
    public boolean vali(Supervisor svisor, Map<Class<? extends Annotation>, Annotation> annts, String[] values) {
        boolean isPass = true;
        if (values == null || values.length == 0) {
            swit = SwitchLabel.ALLOWNULL_ASSERT;
        } else {
            swit = SwitchLabel.JOIN;
        }
        {
            isRetain = false;
        }
        return isPass;
    }
}
