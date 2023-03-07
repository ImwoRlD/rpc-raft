package client;

import dto.RpcMessage;
import dto.RpcRequest;
import dto.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import netty.NettyClientHandler;
import transport.RpcRequestTransport;
import utils.ChannelProvider;
import utils.SingletonFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyClient implements RpcRequestTransport {

    private final EventLoopGroup eventLoopGroup;
    //bootstrap
    private final Bootstrap bootstrap;
    private final ChannelProvider channelProvider;
    public NettyClient(){
        eventLoopGroup = new NioEventLoopGroup();
        channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .handler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new IdleStateHandler(0,5,0, TimeUnit.SECONDS));
                        p.addLast(new NettyClientHandler());
                    }
                });
    }
    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress){
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    //log
                    completableFuture.complete(channelFuture.channel());
                }else{
                    throw new IllegalStateException();
                }
            }
        });
        return completableFuture.get();
    }
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        //
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        //TODO get from register

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",8888);

        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()){
            //put unprocessed request

            RpcMessage rpcMessage = RpcMessage.builder()
                    .data(rpcRequest)
                    //TODO 协议制定
                    .build();
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()){
                    //log
                }else{
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    //log
                }
            });
        }else{
            throw new IllegalStateException();
        }
        return resultFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress){
        Channel channel =channelProvider.get(inetSocketAddress);
        if (channel == null){
            channel = doConnect(inetSocketAddress);
            //get from channelProvider
            channelProvider.set(inetSocketAddress,channel);
        }
        return channel;
    }

    public void close(){
        eventLoopGroup.shutdownGracefully();
    }
}
