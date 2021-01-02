package cn.wang.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {

        // 创建 BossGroup 和 WorkerGroup
        // 说明
        // 1. 创建两个线程组 bossGroup 和 workerGroup
        // 2. bossGroup 只是处理连接请求， 真正和客服端业务处理会交给 workerGroup完成
        // 3. 两个都是无限循环
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建服务器的启动对象， 配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            // 使用链式编程来进行配置
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用 NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128) // 设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道测试对象（匿名对象）
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 给workerGroup 的 EventLoop 对应的通道设置处理器
            System.out.println("......服务器 is ready...");

            // 绑定一个端口并且同步，生成了一个 ChannelFutrue 对象
            // 启动服务器（并绑定端口）
            ChannelFuture cf = bootstrap.bind(6668).sync();

            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
