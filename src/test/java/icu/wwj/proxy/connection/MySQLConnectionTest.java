package icu.wwj.proxy.connection;

import icu.wwj.proxy.connection.mysql.MySQLDatabaseConnection;
import icu.wwj.proxy.connection.mysql.MySQLDatabaseConnectionImpl;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

class MySQLConnectionTest {
    
    @Test
    void test() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        MySQLDatabaseConnection connection = new MySQLDatabaseConnectionImpl();
        ChannelFuture future = connection.connect(eventLoopGroup, new InetSocketAddress("127.0.0.1", 3306), new User("root", "root"), new ConnectionOption());
        future.syncUninterruptibly();
        eventLoopGroup.shutdownGracefully();
    }
}
