package cn.wang.netty.groupchat;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class GroupChatServer {
    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
    }

    public static void main(String[] args) {

    }
}
