package com.cobostack.spi.demo;

/**
 * @author Xu_Ming
 * @version 1.0, 2018/12/9
 */

public class DemoServiceTwoImpl implements DemoService {

    @Override
    public void hello() {
        System.out.println("hello, I am DemoServiceTwoImpl!");
    }
}