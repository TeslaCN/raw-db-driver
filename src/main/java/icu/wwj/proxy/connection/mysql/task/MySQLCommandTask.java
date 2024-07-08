package icu.wwj.proxy.connection.mysql.task;

import icu.wwj.proxy.connection.RequestContext;
import icu.wwj.proxy.connection.mysql.MySQLDatabaseConnection;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MySQLCommandTask implements Runnable {
    
    private final RequestContext requestContext; 
    
    @Override
    public void run() {
        if (requestContext.isSent()) {
            throw new IllegalStateException("Request has been sent");
        }
        Channel channel = requestContext.getChannelHandlerContext().channel();
        channel.attr(MySQLDatabaseConnection.SEQUENCE_ID).get().set(0);
        requestContext.setSent(true);
        requestContext.getChannelHandlerContext().writeAndFlush(requestContext.getCommand(), channel.voidPromise());
    }
}
