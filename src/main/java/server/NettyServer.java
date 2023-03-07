package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.handler.LoggingHandler;
import netty.handler.TemplateHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyServer {
    // TODO 配置文件读取
    public static final int PORT = 9998;

    public void start(){
        //注册中心下线

        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new TemplateHandler());
                            //具体业务逻辑
                            //p.addLast
                        }
                    });
            ChannelFuture f = bootstrap.bind(host,PORT).sync();
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
