import client.NettyClient;
import dto.RpcRequest;
import server.NettyServer;

public class TestMain {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
//        NettyClient nettyClient = new NettyClient();
        nettyServer.start();
//        RpcRequest rpcRequest = new RpcRequest();
//        nettyClient.sendRpcRequest(rpcRequest);
    }
}
