package netty.handler;

import dto.RpcRequest;
import utils.ServiceProvider;

public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler(){
        ServiceProvider = new ServiceProvider(){}
    }

    public Object handle(RpcRequest rpcRequest){
        Object service = ServiceProvider.getService(rpcRequest.getRpcServiceName())
    }
}
