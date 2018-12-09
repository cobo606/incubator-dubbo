package com.cobostack.spi.demo;

import org.junit.Test;

import java.util.ServiceLoader;

/**
 * Java SPI 机制
 *
 * <ol>
 * Java Spi 规范
 * <li> 需要在classpath下创建一个目录, 该目录命名必须是: META-INF/services
 * <li> 在该目录下创建一个文件, 该文件需要满足以下几个条件: <ol>
 * <li> 文件名必须是扩展的接口的全路径名称.
 * <li> 文件内部描述的是该扩展接口的所有实现类, 可以写一个或多个.
 * <li> 文件的编码格式是UTF-8.</ol>
 * <li> 通过java.util.ServiceLoader的加载机制来, 加载实现类.
 * </ol>
 *
 * <ol>
 * Java Spi 的缺点
 * <li> JDK标准的SPI会一次性加载实例化扩展点的所有实现, 如果你在META-INF/service下的文件里面加了N个实现类,  那么JDK启动的时候都会
 * 一次性全部加载. 那么如果有的扩展点实现初始化很耗时或者如果有些实现类并没有用到,  那么会很浪费资源.
 * <li> 如果扩展点加载失败,  会导致调用方报错,  而且这个错误很难定位到是这个原因.
 * </ol>
 *
 * @author Xu_Ming
 * @version 1.0, 2018/12/9
 */

public class JavaSpiTest {

    @Test
    public void testJavaSpi() {
        ServiceLoader<DemoService> serviceLoader = ServiceLoader.load(DemoService.class);

        System.out.println("Java SPI");

        /* 定义在 META-INF/services/com.cobostack.spi.demo.DemoService 中的两个实现类被成功的加载.
         * hello, I am DemoServiceOneImpl!
         * hello, I am DemoServiceTwoImpl!
         */
        for (DemoService demoService : serviceLoader) {
            demoService.hello();
        }
    }
}