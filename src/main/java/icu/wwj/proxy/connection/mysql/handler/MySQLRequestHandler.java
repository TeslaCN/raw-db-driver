package icu.wwj.proxy.connection.mysql.handler;

import icu.wwj.proxy.connection.DatabaseConnection;
import icu.wwj.proxy.connection.RequestContext;
import icu.wwj.proxy.connection.mysql.task.MySQLCommandTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.Queue;

public class MySQLRequestHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
        RequestContext requestContext = (RequestContext) msg;
        requestContext.setChannelHandlerContext(ctx);
        Queue<RequestContext> queue = ctx.channel().attr(DatabaseConnection.PIPELINE_KEY).get();
        queue.offer(requestContext);
        if (1 == queue.size()) {
            ctx.channel().eventLoop().execute(new MySQLCommandTask(requestContext));
        }
    }
}
