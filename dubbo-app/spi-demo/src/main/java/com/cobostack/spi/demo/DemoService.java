package com.cobostack.spi.demo;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * 模拟接口
 *
 * @author Xu_Ming
 * @version 1.0, 2018/12/9
 */

@SPI
public interface DemoService {

    void hello();

}