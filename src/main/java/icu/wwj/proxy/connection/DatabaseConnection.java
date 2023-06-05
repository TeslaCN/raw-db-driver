package icu.wwj.proxy.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Promise;

import java.net.SocketAddress;
import java.util.List;

public interface DatabaseConnection {
    
    Channel channel();
    
    ChannelFuture connect(SocketAddress socketAddress, User user, ConnectionOption option);
    
    ChannelFuture request(ByteBufAware byteBufAware, Promise<List<ByteBufAware>> promise);
    
    ChannelFuture close();
}
