package icu.wwj.proxy.connection;

import icu.wwj.proxy.connection.mysql.MySQLDatabaseConnection;
import icu.wwj.proxy.connection.mysql.MySQLDatabaseConnectionImpl;
import icu.wwj.proxy.connection.mysql.packet.command.MySQLComQueryPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

class MySQLConnectionTest {
    
    @Test
    void test() throws InterruptedException {
        EventLoopGroup eventLoopGroup = getEventLoopGroup();
        MySQLDatabaseConnection connection = new MySQLDatabaseConnectionImpl();
        ChannelFuture future = connection.connect(eventLoopGroup, new InetSocketAddress("127.0.0.1", 3306), new User("root", "root"), new ConnectionOption());
        future.syncUninterruptibly();
        Channel channel = connection.channel();
        DefaultPromise<List<ByteBufAware>> promise = new DefaultPromise<>(channel.eventLoop());
        connection.request(new MySQLComQueryPacket(channel.alloc(), "set autocommit=0", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'One', 1", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Two', 2", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Three', 3", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Four', 4", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Five', 5", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Six', 6", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Seven', 7", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Eight', 8", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Nine', 9", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Ten', 10", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Eleven', 11", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Twelve', 12", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Thirteen', 13", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Fourteen', 14", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Fifteen', 15", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Sixteen', 16", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Seventeen', 17", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Eighteen', 18", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Nineteen', 19", StandardCharsets.UTF_8), promise);
        connection.request(new MySQLComQueryPacket(channel.alloc(), "select 'Twenty', 20", StandardCharsets.UTF_8), promise);
        promise.addListener(f -> {
            List<ByteBufAware> list = (List<ByteBufAware>) f.getNow();
        });
        promise.await(1000);
        connection.close();
        channel.closeFuture().syncUninterruptibly();
        eventLoopGroup.shutdownGracefully();
    }

    private EventLoopGroup getEventLoopGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup(1);
        }
        if (KQueue.isAvailable()) {
            return new KQueueEventLoopGroup(1);
        }
        return new NioEventLoopGroup(1);
    }
}
