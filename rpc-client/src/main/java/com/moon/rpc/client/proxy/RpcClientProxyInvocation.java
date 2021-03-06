package com.moon.rpc.client.proxy;

import com.moon.rpc.client.transport.DefaultResponseFuture;
import com.moon.rpc.client.LocalRpcResponseFactory;
import com.moon.rpc.client.transport.ResponseFuture;
import com.moon.rpc.client.transport.RpcClient;
import com.moon.rpc.client.exception.RemotingException;
import com.moon.rpc.client.exception.TimeoutException;
import com.moon.rpc.transport.dto.RpcRequest;
import com.moon.rpc.transport.dto.RpcResponse;
import com.moon.rpc.transport.protocol.SequenceIdGenerator;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * @author mzx
 * @date 2022/7/13 19:22
 */
@Slf4j
public class RpcClientProxyInvocation implements InvocationHandler {

    private Class<?> clazz;

    private String version;
    private RpcClient rpcClient;

    public RpcClientProxyInvocation(Class<?> clazz, RpcClient rpcClient) {
        this.clazz = clazz;
        this.rpcClient = rpcClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long timeout = 5000;
        // 1. 将方法调用转换为 消息对象
        int sequenceId = SequenceIdGenerator.nextId();
        RpcRequest rpcRequest = new RpcRequest(
                sequenceId,
                clazz.getName(),
                method.getName(),
                method.getParameterTypes(),
                args,
                timeout,
                TimeUnit.MILLISECONDS
        );
        // 2. 准备一个ResponseFuture 用于接受异步结果
        ResponseFuture<RpcResponse> future = new DefaultResponseFuture<>();
        LocalRpcResponseFactory.add(sequenceId, future);
        // 3. 进行网络通信，发起RPC调用，将消息对象发送出去
        rpcClient.sendRpcRequest(rpcRequest);
        // 4. 异步等待 超时 阻塞等待RPC结果
        RpcResponse rpcResponse = null;
        rpcResponse = future.get(timeout, TimeUnit.MILLISECONDS);
        // 5. 处理响应结果
        if(rpcResponse == null) {
            log.error("请求超时");
            throw new TimeoutException("RPC调用超时，超时时间:" + timeout);
        }
        if (rpcResponse.getException() != null) {
            log.error("RPC调用异常");
            throw new RemotingException(rpcResponse.getException().getMessage());
        } else {
            return rpcResponse.getReturnValue();
        }
    }
}
