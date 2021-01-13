package cn.wang.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup wokerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,wokerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // 加入一个 netty 提供的 IdleStateHandler
                            /*
                            说明：
                            1. IdleStateHandler 是netty 提供的处理空闲状态的处理器
                            2. long readerIdleTime : 表示多少时间没有读， 就会发送一个心跳检测包检测是否连接
                            3. long writerIdleTime : 表示多少时间没有写， 就会发送一个心跳检测包检测是否连接
                            4. long allIdleTime : 表示多少时间没有读和写， 就会发送一个心跳检测包检测是否连接

                            5. 文档说明
                            Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                            read, write, or both operation for a while.

                            6. 当 IdleStateHandler 触发后， 就会传递给管道的下一个 handler 去处理
                            通过调用（触发）下一个 handler 的 userEventTiggered , 在该方法中去处理
                            IdleStateEvent
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的handler(自定义)
                            pipeline.addLast(new MyServerHandler());
                        }
                    });

            // 启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            wokerGroup.shutdownGracefully();
        }
    }
}
