package transport;

import dto.RpcRequest;

public interface RpcRequestTransport {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
