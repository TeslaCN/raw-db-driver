package icu.wwj.proxy.connection.mysql.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MySQLResponseHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte type = byteBuf.getByte(byteBuf.readerIndex());
        switch (type) {
            case 0x01:
        }
    }
}
