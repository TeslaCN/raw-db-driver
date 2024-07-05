package icu.wwj.proxy.connection.mysql;

import icu.wwj.proxy.connection.ByteBufAware;
import icu.wwj.proxy.connection.ConnectionOption;
import icu.wwj.proxy.connection.RequestContext;
import icu.wwj.proxy.connection.User;
import icu.wwj.proxy.connection.mysql.handler.ClientAuthenticationHandler;
import icu.wwj.proxy.connection.mysql.handler.ClientCloseHandler;
import icu.wwj.proxy.connection.mysql.handler.MySQLPacketDecoder;
import icu.wwj.proxy.connection.mysql.handler.MySQLPacketEncoder;
import icu.wwj.proxy.connection.mysql.handler.MySQLRequestHandler;
import icu.wwj.proxy.connection.mysql.handler.MySQLResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Promise;

import java.net.SocketAddress;
import java.util.ArrayDeque;
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
                .channel(determineChannelClass(eventLoopGroup))
                .group(eventLoopGroup)
                .handler(new ChannelInitializer<>() {

                    @Override
                    protected void initChannel(Channel ch) {
                        ch.attr(SEQUENCE_ID).set(new AtomicInteger());
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        ch.pipeline().addLast(new MySQLPacketDecoder());
                        ch.pipeline().addLast(new MySQLPacketEncoder());
                        ch.pipeline().addLast(new ClientAuthenticationHandler(user, option));
                        ch.pipeline().addLast(new MySQLRequestHandler());
                        ch.pipeline().addLast(new MySQLResponseHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(socketAddress);
        ChannelPromise channelReadyPromise = channelFuture.channel().newPromise();
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                channel = channelFuture.channel();
                channel.pipeline().addLast(new ClientCloseHandler());
                channel.attr(PIPELINE_KEY).set(new ArrayDeque<>());
                channelReadyPromise.setSuccess();
            }
        });
        return channelReadyPromise;
    }

    private Class<? extends SocketChannel> determineChannelClass(EventLoopGroup eventLoopGroup) {
        if (eventLoopGroup instanceof NioEventLoopGroup) {
            return NioSocketChannel.class;
        }
        if (eventLoopGroup instanceof EpollEventLoopGroup) {
            return EpollSocketChannel.class;
        }
        if (eventLoopGroup instanceof KQueueEventLoopGroup) {
            return KQueueSocketChannel.class;
        }
        throw new IllegalArgumentException("Unsupported EventLoopGroup " + eventLoopGroup);
    }

    @Override
    public void request(ByteBufAware byteBufAware, Promise<List<ByteBufAware>> promise) {
        if (null == byteBufAware || null == promise) {
            throw new IllegalArgumentException("ByteBufAware and Promise cannot be null");
        }
        ChannelFuture channelFuture = channel.writeAndFlush(new RequestContext(byteBufAware, promise));
//        channel.pipeline().fireUserEventTriggered()
    }

    @Override
    public ChannelFuture close() {
        return channel.close();
    }
}
