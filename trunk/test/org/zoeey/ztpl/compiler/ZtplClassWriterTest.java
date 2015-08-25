/*
 * MoXie (SysTem128@GMail.Com) 2009-5-3 1:32:56
 * $Id: ZtplClassWriterTest.java 68 2009-11-01 07:56:13Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.ztpl.compiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class ZtplClassWriterTest extends ClassLoader {

    /**
     *
     */
    public ZtplClassWriterTest() {
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
     * Test of evalCompileFunctions method, of class TemplateClassWriter.
     * @throws InstantiationException 
     * @throws FileNotFoundException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void testEvalCompileFunctions()
            throws InstantiationException, IllegalAccessException,
            FileNotFoundException, IOException, ClassNotFoundException {
        System.out.println("evalCompileFunctions");
        /**
         * 注册编译函数
         */
//        CompileAble[] cmps = new CompileAble[]{new ZTPC_If()};
        /**
         * 定义类书写器
        //         */
//        ZtplClassWriter zcw = new ZtplClassWriter("Comment"，null);
//        zcw.evalCompileFunctions(cmps);
//        zcw.endWrite();
//        byte[] code = zcw.getCode();
//        /**
//         * 输出字节码
//         */
//        {
////            File file = new File(DirInfo.getClassesDir() + "org/zoeey/ztpl/compiled/Comment.class");
//            File file = new File("D:/UploadTest/class/Comment.class");
//            System.out.println(file.getAbsoluteFile());
//            FileHelper.tryCreate(file);
//            BinaryFileHelper.write(file, code);
//        }
//        /**
//         * 加载到ClassLoader
//         */
//        Class clazz = defineClass(ZtplConstant.CLASS_PACKAGE + "Comment", code);
//        TemplateAble tpl = (TemplateAble) clazz.newInstance();
//        /**
//         * 构建一个输出环境
//         */
//        File htmlFile = new File("D:/UploadTest/class/comment.html");
//        PrintWriter writer = new PrintWriter(htmlFile);
//        Map map = new HashMap();
//        map.put("name", "MoXie");
//        map.put("count", 100);
//        tpl.publish(writer, map);
//        writer.flush();
//        writer.close();
//        writer = null;
//        System.out.println(TextFileHelper.read(htmlFile));
    }

    /**
     *
     * @param name
     * @param code
     * @return
     */
    public Class defineClass(String name, byte[] code) {
        return defineClass(name, code, 0, code.length);
    }
}
