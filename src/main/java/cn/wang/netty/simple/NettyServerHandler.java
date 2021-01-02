package cn.wang.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 1、自定义的一个handler 需要继承netty 规定好的某个 HandlerAdapter（规范）
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 读取数据实际（这里我们可以读取客户端发送的信息）
    /*
    1、ChannelHandlerContext：上下文对象，含有 管道pipeline, 通道channel， 地址
    2、Object msg： 就是客户端发送的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);

        // 将 msg 转为一个ByteBuf
        // ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送的消息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush 是 write + flush
        // 将数据写到缓存，并刷新
        // 将发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端", CharsetUtil.UTF_8));
    }

    // 异常处理，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
