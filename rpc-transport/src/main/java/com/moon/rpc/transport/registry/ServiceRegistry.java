package com.moon.rpc.transport.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 *
 * @author chenlei
 */
public interface ServiceRegistry {
    /**
     * 将服务的名称和地址注册进服务注册中心
     *
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

}
