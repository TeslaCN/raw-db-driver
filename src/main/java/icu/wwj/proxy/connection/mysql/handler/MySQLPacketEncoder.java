package icu.wwj.proxy.connection.mysql.handler;

import icu.wwj.proxy.connection.mysql.MySQLDatabaseConnection;
import icu.wwj.proxy.connection.mysql.packet.MySQLPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class MySQLPacketEncoder extends ChannelOutboundHandlerAdapter {
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
        ByteBuf byteBuf = ((MySQLPacket) msg).byteBuf();
        int length = byteBuf.readableBytes();
        if (length <= 0xFFFFFF) {
            ByteBuf header = ctx.alloc().ioBuffer(4, 4);
            header.writeMediumLE(length);
            header.writeByte(ctx.channel().attr(MySQLDatabaseConnection.SEQUENCE_ID).get().incrementAndGet());
            ctx.write(header);
            ctx.write(byteBuf, promise);
        } else {
            writeSplit(ctx, byteBuf, promise);
        }
    }
    
    private void writeSplit(final ChannelHandlerContext ctx, final ByteBuf byteBuf, final ChannelPromise promise) {
        throw new UnsupportedOperationException("Write split packet");
    }
}
