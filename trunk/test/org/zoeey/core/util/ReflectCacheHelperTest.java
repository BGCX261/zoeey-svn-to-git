/*
 * MoXie (SysTem128@GMail.Com) 2009-5-9 15:12:35
 * $Id: ReflectCacheHelperTest.java 78 2010-04-30 02:10:36Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zoeey.core.loader.User;
import org.zoeey.core.loader.annotations.Request;
import org.zoeey.core.util.BeanHelperTest.HanMeimei;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class ReflectCacheHelperTest extends TestCase {

    /**
     *
     */
    public ReflectCacheHelperTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of cache method, of class ReflectCacheHelper.
     */
    @Test
    public void testCache() {
        System.out.println("cache");
        Class<?> clazz = Article.class;
        TimeMeasurer tm = new TimeMeasurer();
        tm.start();
        Map<Method, Request> apmm = null;
        for (int i = 0; i < 1; i++) {
            ReflectCacheHelper rc = ReflectCacheHelper.get(clazz);

            for (Method method : rc.getMethodMap().values()) {
                method.getName();
            }
//            list = rc.getAnnotationPresentedList(Request.class);
//            apmm = rc.getAnnotationPresentedMethodMap(Request.class);
        }

//        System.out.println(tm.spend());
        /**
         * cached
         * 1,000,000
         * getAnnotationPresentedList
         * 4345
         * 4168
         * 4298
         * getAnnotationedMethodList
         * 3976
         * 4158
         * 4080
         * 4309
         * 100,000
         * 605ms
         * 583ms
         * 519ms
         * 519ms
         * 533ms
         * cached method annotations
         * 481ms
         * 483ms
         * 506ms
         * 10,000
         * 149ms
         * 111ms
         * 109ms
         * 1,000
         * 95ms
         * 81ms
         * 73ms
         * uncached
         * 1,000
         * 1127ms
         * 1144ms
         * 1184ms
         * 10,000
         * 8913ms
         */
        clazz = User.class;
        ReflectCacheHelper reflect = ReflectCacheHelper.get(clazz);
//        list = rc.getAnnotationPresentedList(Request.class);
        apmm = reflect.<Request>getAnnotationPresentedMethodMap(Request.class);
        StringBuilder strBuilder = new StringBuilder();
        for (Request req : apmm.values()) {
            strBuilder.append(req.name());
            strBuilder.append(",");
        }

        assertEquals(strBuilder.toString(), "account,email,id,photos,historys,avatar,items,");
        strBuilder = new StringBuilder();
        Map<Method, Map<Class<? extends Annotation>, Annotation>> maMap = reflect.getMethodAnnotationMap();
        for (Map<Class<? extends Annotation>, Annotation> aMap : maMap.values()) {
            for (Annotation annot : aMap.values()) {
                strBuilder.append(annot.annotationType().getName());
            }
        }

        assertEquals(strBuilder.toString(), "org.zoeey.core.loader.annotations.Request"
                + "org.zoeey.core.validator.annotations.Email"
                + "org.zoeey.core.validator.annotations.Conclusion"
                + "org.zoeey.core.loader.annotations.Request"
                + "org.zoeey.core.validator.annotations.Accessory"
                + "org.zoeey.core.loader.annotations.Request"
                + "org.zoeey.core.loader.annotations.Request"
                + "org.zoeey.core.loader.annotations.Request"
                + "org.zoeey.core.loader.annotations.Request"
                + "org.zoeey.core.loader.annotations.Request");
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testGetGetMap() throws Exception {
        System.out.println("getGetMap");
        Object obj = new HanMeimei("Han Meimei", 20, true, "dress");
        ReflectCacheHelper refHelper = ReflectCacheHelper.get(obj.getClass());
        Map<String, Method> beanMap = refHelper.getGetMap();
        StringBuilder strBuilder = new StringBuilder();
        for (Entry<String, Method> entry : beanMap.entrySet()) {
            strBuilder.append(entry.getKey());
        }
        assertEquals(strBuilder.toString(), "age"
                + "class"
                + "isActive"
                + "dress"
                + "name");
        HashMap<String, String> map = new HashMap<String, String>();
    }

    /**
     * multi-thread test
     */
    @Test
    public void testPut() throws Exception {
        if (true) {
            return;
        }
        System.out.println("testPut");
        ObjectCacheHelper.removeAll();
        for (int i = 0; i < 10; i++) {
            Thread thread = new PutThread();
            thread.start();
        }
        while (Thread.activeCount() > 1) {
            Thread.sleep(100);
        }
    }

    private static class PutThread extends Thread {

        @Override
        public void run() {
            ReflectCacheHelper.get(HanMeimei.class);
        }
    }
}
