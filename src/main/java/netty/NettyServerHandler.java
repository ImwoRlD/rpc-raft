package netty;

import constants.RpcConstants;
import dto.RpcMessage;
import dto.RpcRequest;
import dto.RpcResponse;
import enums.RpcResponseCodeEnum;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("idle check happen, so close the connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof RpcMessage){
                log.info("server receiver msg:[{}]",msg);
                byte messageType =((RpcMessage)msg).getMessageType();
                RpcMessage rpcMessage = new RpcMessage();
                //序列化方式
//                rpcMessage.setCodec();
                //压缩方式
//                rpcMessage.setCodec();
//                rpc
                if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE){
                    rpcMessage.setMessageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE);
                    rpcMessage.setData(RpcConstants.PONG);
                }else{
                    RpcRequest rpcRequest = (RpcRequest)((RpcMessage)msg).getData();
                    //TODO 具体处理业务
//                    Object result = rpcRequestHandler.handle(rpcRequest)
                    Object result = null;
                    log.info(String.format("server"));
                    rpcMessage.setMessageType(RpcConstants.RESPONSE_TYPE);
                    if (ctx.channel().isActive()&&ctx.channel().isWritable()){
                        RpcResponse<Object> response = RpcResponse.success(result,rpcRequest.getRequestId());
                        rpcMessage.setData(response);
                    }else{
                        RpcResponse<Object> rpcResponse = RpcResponse.fail();
                        rpcMessage.setData(rpcResponse);
                        log.error("not wirtable now,message dropped");
                    }
                }
                ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
