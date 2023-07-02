package icu.wwj.proxy.connection.mysql;

import icu.wwj.proxy.connection.ByteBufAware;
import icu.wwj.proxy.connection.ConnectionOption;
import icu.wwj.proxy.connection.User;
import icu.wwj.proxy.connection.mysql.handler.ClientAuthenticationHandler;
import icu.wwj.proxy.connection.mysql.handler.MySQLPacketDecoder;
import icu.wwj.proxy.connection.mysql.handler.MySQLPacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;

import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MySQLDatabaseConnectionImpl implements MySQLDatabaseConnection {

    private Channel channel;

    @Override
    public Channel channel() {
        return null;
    }

    @Override
    public ChannelFuture connect(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, User user, ConnectionOption option) {
        Bootstrap bootstrap = new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(eventLoopGroup)
                .handler(new ChannelInitializer<>() {

                    @Override
                    protected void initChannel(Channel ch) {
                        ch.attr(SEQUENCE_ID).set(new AtomicInteger());
                        ch.pipeline().addLast(new MySQLPacketDecoder());
                        ch.pipeline().addLast(new MySQLPacketEncoder());
                        ch.pipeline().addLast(new ClientAuthenticationHandler(user, option));
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(socketAddress);
        return channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                channel = channelFuture.channel();
            }
        });
    }

    @Override
    public ChannelFuture request(ByteBufAware byteBufAware, Promise<List<ByteBufAware>> promise) {
        return null;
    }

    @Override
    public ChannelFuture close() {
        return null;
    }
}
