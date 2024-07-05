package icu.wwj.proxy.connection.mysql.handler;

import icu.wwj.proxy.connection.DatabaseConnection;
import icu.wwj.proxy.connection.RequestContext;
import icu.wwj.proxy.connection.mysql.task.MySQLCommandTask;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;

@Slf4j
public class MySQLResponseHandler extends ChannelInboundHandlerAdapter {

    public static final int OK = 0x00;

    public static final byte EOF = (byte) 0xFE;

    public static final byte ERR = (byte) 0xFF;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        Queue<RequestContext> queue = ctx.channel().attr(DatabaseConnection.PIPELINE_KEY).get();
        byte type = byteBuf.getByte(byteBuf.readerIndex());
        switch (type) {
            case OK:
            case EOF:
            case ERR:
                queue.poll();
                if (!queue.isEmpty()) {
                    ctx.channel().eventLoop().execute(new MySQLCommandTask(queue.peek()));
                }
        }
    }
}
