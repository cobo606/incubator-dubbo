package com.cobostack.spi.demo;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import org.junit.Test;

/**
 * Dubbo SPI 机制
 *
 * <p> Dubbo 并未使用 Java SPI, 而是重新实现了一套功能更强的 SPI 机制. Dubbo SPI 的相关逻辑被封装在了 ExtensionLoader 类中,
 *  通过 ExtensionLoader, 我们可以加载指定的实现类. Dubbo SPI 的实现类配置放置在 META-INF/dubbo 路径下.
 * <p> 与 Java SPI 实现类配置不同,  Dubbo SPI 是通过键值对的方式(name=实现类全名)进行配置,  这样我们就可以按需加载指定的实现类了.
 * <p> 通过 ExtensionLoader 来加载的接口, 接口上标注 @SPI 注解.
 *
 * <ul>
 *     Dubbo SPI 对比 Java SPI 的增强点.
 * <li> JDK 标准的 SPI 会一次性实例化扩展点所有实现, 如果有扩展实现初始化很耗时, 但如果没用上也加载, 会很浪费资源.
 * <li> 增加了对扩展点 IOC 和 AOP 的支持,  一个扩展点可以直接 setter 注入其它扩展点. Dubbo SPI 加载完拓展实例后,  会通过该实例的
 *  setter 方法解析出实例依赖项的名称. 比如通过 setProtocol 方法名,  可知道目标实例依赖 Protocol. 知道了具体的依赖,
 *  接下来即可到 IOC 容器中寻找或生成一个依赖对象,  并通过 setter 方法将依赖注入到目标实例中.
 *  Dubbo AOP 是指使用 Wrapper 类（可自定义实现）对拓展对象进行包装,  Wrapper 类中包含了一些自定义逻辑,
 *  这些逻辑可在目标方法前行前后被执行,  类似 AOP.
 * </ul>
 *
 * @author Xu_Ming
 * @version 1.0, 2018/12/9
 */

public class DubboSpiTest {

    @Test
    public void testDubboSpi() {
        System.out.println("Dubbo SPI");

        // 1. 通过 ExtensionLoader 的 getExtensionLoader 方法获取一个 ExtensionLoader 实例
        ExtensionLoader<DemoService> extensionLoader = ExtensionLoader.getExtensionLoader(DemoService.class);

        // 2.  ExtensionLoader 的 getExtension 方法获取拓展类对象.
        // hello, I am DemoServiceOneImpl!
        DemoService oneService = extensionLoader.getExtension("one");
        oneService.hello();

        // hello, I am DemoServiceTwoImpl!
        DemoService twoService = extensionLoader.getExtension("two");
        twoService.hello();

    }

}