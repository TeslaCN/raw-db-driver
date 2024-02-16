package icu.wwj.proxy.connection.mysql.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class ClientCloseHandler extends ChannelOutboundHandlerAdapter {
    
    private static final int COM_QUIT = 0x01;
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ByteBuf byteBuf = ctx.alloc().ioBuffer(1, 1);
        byteBuf.writeByte(COM_QUIT);
        ctx.writeAndFlush(byteBuf);
        super.close(ctx, promise);
    }
}
