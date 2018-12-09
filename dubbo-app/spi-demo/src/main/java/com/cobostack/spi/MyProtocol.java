package com.cobostack.spi;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.protocol.AbstractProtocol;

/**
 * 自定义协议实现, 参考 {@link com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol}
 *
 * @author Xu_Ming
 * @version 1.0, 2018/12/9
 */

public class MyProtocol extends AbstractProtocol {

    @Override
    public int getDefaultPort() {
        return 88;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        return null;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        return null;
    }
}