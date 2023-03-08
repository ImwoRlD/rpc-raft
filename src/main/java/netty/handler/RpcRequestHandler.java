package netty.handler;

import dto.RpcRequest;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import utils.ServiceProvider;
import utils.SingletonFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler(){
        serviceProvider = SingletonFactory.getInstance(ServiceProvider.class);
    }

    public Object handle(RpcRequest rpcRequest){
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest,service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service){
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
            result = method.invoke(service,rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method :[{}]",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
        }catch (NoSuchMethodException|IllegalArgumentException| InvocationTargetException|IllegalAccessException e){
            throw new RpcException(e.getMessage(),e);
        }
        return result;
    }
}
