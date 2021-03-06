package cn.wang.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 入站的 handler 进行解码 MyByteToLongDecoder
        pipeline.addLast("decoder", new MyByteToLongDecoder());
//        pipeline.addLast(new MyByteToLongDecoder2());

        // 出站的 handler 进行编码
        pipeline.addLast(new MyLongToByteEncoder());
        // 加入一个自定义的 handler , 处理业务
        pipeline.addLast(new MyServerHandler());
    }
}
