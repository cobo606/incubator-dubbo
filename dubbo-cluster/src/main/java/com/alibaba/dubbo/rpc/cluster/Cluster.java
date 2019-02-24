/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.cluster;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.support.BroadcastCluster;
import com.alibaba.dubbo.rpc.cluster.support.FailbackCluster;
import com.alibaba.dubbo.rpc.cluster.support.FailfastCluster;
import com.alibaba.dubbo.rpc.cluster.support.FailoverCluster;
import com.alibaba.dubbo.rpc.cluster.support.FailoverClusterInvoker;
import com.alibaba.dubbo.rpc.cluster.support.FailsafeCluster;
import com.alibaba.dubbo.rpc.cluster.support.ForkingCluster;

import java.util.List;

/**
 * Cluster. (SPI, Singleton, ThreadSafe)
 * <p>
 * <a href="http://en.wikipedia.org/wiki/Computer_cluster">Cluster</a>
 * <a href="http://en.wikipedia.org/wiki/Fault-tolerant_system">Fault-Tolerant</a>
 *
 * <p> <img src="http://dubbo.apache.org/docs/zh-cn/user/sources/images/cluster.jpg"> </p>
 * <p> 集群容错的整个工作过程: 集群工作过程可分为两个阶段, 第一个阶段是在服务消费者初始化期间, 集群 Cluster 实现类
 * {@link FailoverCluster} 为服务消费者创建 Cluster Invoker 实例 {@link FailoverClusterInvoker}, 即上图中的 merge 操作.
 * <p> 第二个阶段是在服务消费者进行远程调用时. 以 FailoverClusterInvoker 为例, 该类型 Cluster Invoker 首先会调用
 * Directory 的 list 方法 {@link Directory#list(Invocation)} 列举 Invoker 列表(可将 Invoker 简单理解为服务提供者).
 * Directory 的用途是保存 Invoker, 可简单类比为 List<Invoker>. 其实现类 RegistryDirectory 是一个动态服务目录,
 * 可感知注册中心配置的变化, 它所持有的 Invoker 列表会随着注册中心内容的变化而变化. 每次变化后, RegistryDirectory 会动态增删
 * Invoker, 并调用 Router 的 route 方法进行路由, 过滤掉不符合路由规则的 Invoker. 回到上图, Cluster Invoker 实际上并不会直接调用
 * Router 进行路由. 当 FailoverClusterInvoker 拿到 Directory 返回的 Invoker 列表后, 它会通过 LoadBalance 从 Invoker
 * 列表中选择一个 Invoker {@link LoadBalance#select(List, URL, Invocation)}. 最后 FailoverClusterInvoker 会将参数传给
 * LoadBalance 选择出的 Invoker 实例的 invoker 方法, 进行真正的 RPC 调用.
 *
 * <ul>
 * 集群容错模式:
 * <li> {@link FailfastCluster} 快速失败, 只发起一次调用, 失败立即报错. 通常用于非幂等性的写操作, 比如新增记录.
 * <li> {@link FailsafeCluster} 失败安全, 出现异常时, 直接忽略. 通常用于写入审计日志等操作.
 * <li> {@link FailbackCluster} 失败自动恢复, 后台记录失败请求, 定时重发. 通常用于消息通知操作.
 * <li> {@link ForkingCluster} 并行调用多个服务器, 只要一个成功即返回. 通常用于实时性要求较高的读操作, 但需要浪费更多服务资源.
 *                             可通过 forks="2" 来设置最大并行数.
 * <li> {@link BroadcastCluster} 广播调用所有提供者, 逐个调用, 任意一台报错则报错. 通常用于通知所有提供者更新缓存或日志等本地资源信息.
 * </ul>
 *
 * <p> 注意: Cluster 是接口, 而 Cluster Invoker 是一种 Invoker. 服务提供者的选择逻辑, 以及远程调用失败后的的处理逻辑均是封装在
 * Cluster Invoker 中的. Cluster 接口和相关实现类只是用于生成 Cluster Invoker, 仅此而已.
 */
@SPI(FailoverCluster.NAME)
public interface Cluster {

    /**
     * Merge the directory invokers to a virtual invoker.
     *
     * @param <T>
     * @param directory
     * @return cluster invoker
     * @throws RpcException
     */
    @Adaptive
    <T> Invoker<T> join(Directory<T> directory) throws RpcException;

}