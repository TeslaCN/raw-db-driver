package icu.wwj.proxy.connection;

import icu.wwj.proxy.connection.mysql.MySQLDatabaseConnection;
import icu.wwj.proxy.connection.mysql.MySQLDatabaseConnectionImpl;
import icu.wwj.proxy.connection.mysql.packet.MySQLComQueryPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

class MySQLConnectionTest {
    
    @Test
    void test() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        MySQLDatabaseConnection connection = new MySQLDatabaseConnectionImpl();
        ChannelFuture future = connection.connect(eventLoopGroup, new InetSocketAddress("127.0.0.1", 3306), new User("root", "root"), new ConnectionOption());
        future.syncUninterruptibly();
        Channel channel = connection.channel();
        DefaultPromise<List<ByteBufAware>> promise = new DefaultPromise<>(channel.eventLoop());
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Hello, World', 1", StandardCharsets.UTF_8), promise);
        promise.addListener(f -> {
            List<ByteBufAware> list = (List<ByteBufAware>) f.getNow();
        });
        promise.await(1000);
        connection.close().syncUninterruptibly();
        eventLoopGroup.shutdownGracefully();
    }
}
