package icu.wwj.proxy.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Promise;

import java.net.SocketAddress;
import java.util.List;
import java.util.Queue;

public interface DatabaseConnection {
    
    AttributeKey<Queue<ByteBufAware>> PIPELINE_KEY = AttributeKey.valueOf("PIPELINE");
    
    Channel channel();
    
    ChannelFuture connect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, User user, ConnectionOption option);
    
    ChannelFuture request(ByteBufAware byteBufAware, Promise<List<ByteBufAware>> promise);
    
    ChannelFuture close();
}
