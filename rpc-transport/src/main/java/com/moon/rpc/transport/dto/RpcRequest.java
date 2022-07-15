package com.moon.rpc.transport.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * @author mzx
 */
@Getter
@Setter
public class RpcRequest extends Message {
    /**
     * 接口的名字(全限定名)
     */
    private String apiName;

    /**
     * 接口的版本
     */
    private String version;

    /**
     * 调用接口中的方法名
     */
    private String methodName;

    /**
     * 方法参数类型数组
     */
    private Class<?>[] parameterTypes;

    /**
     * 方法参数值数组
     */
    private Object[] parameterValue;

    /**
     * 超时时间，这个时间提供给服务端使用
     * TODO 超时时间好像没有必要传给服务端，因为服务端也可以自己定义超时时间
     */
    private long timeout;


    public RpcRequest(int sequenceId, String apiName, String version, String methodName, Class<?>[] parameterTypes, Object[] parameterValue, long timeout) {
        this.sequenceId = sequenceId;
        this.apiName = apiName;
        this.version = version;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameterValue = parameterValue;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "apiName='" + apiName + '\'' +
                ", version='" + version + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameterValue=" + Arrays.toString(parameterValue) +
                ", timeout=" + timeout +
                ", sequenceId=" + sequenceId +
                ", messageType=" + messageType +
                '}';
    }
}
