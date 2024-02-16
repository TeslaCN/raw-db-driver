package icu.wwj.proxy.connection.mysql;

import icu.wwj.proxy.connection.ByteBufAware;
import icu.wwj.proxy.connection.ConnectionOption;
import icu.wwj.proxy.connection.User;
import icu.wwj.proxy.connection.mysql.handler.ClientAuthenticationHandler;
import icu.wwj.proxy.connection.mysql.handler.ClientCloseHandler;
import icu.wwj.proxy.connection.mysql.handler.MySQLPacketDecoder;
import icu.wwj.proxy.connection.mysql.handler.MySQLPacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Promise;

import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MySQLDatabaseConnectionImpl implements MySQLDatabaseConnection {

    private Channel channel;

    @Override
    public Channel channel() {
        if (null == channel) {
            throw new IllegalStateException("Connection is not ready yet");
        }
        return channel;
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
        ChannelPromise channelReadyPromise = channelFuture.channel().newPromise();
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                channel = channelFuture.channel();
                channel.pipeline().addLast(new ClientCloseHandler());
                channelReadyPromise.setSuccess();
            }
        });
        return channelReadyPromise;
    }

    @Override
    public ChannelFuture request(ByteBufAware byteBufAware, Promise<List<ByteBufAware>> promise) {
        channel.attr(SEQUENCE_ID).get().set(0);
        ChannelFuture channelFuture = channel.writeAndFlush(byteBufAware);
        return channelFuture;
    }

    @Override
    public ChannelFuture close() {
        return channel.close();
    }
}
