package icu.wwj.proxy.connection.mysql.handler;

import icu.wwj.proxy.connection.ByteBufAware;
import icu.wwj.proxy.connection.RequestContext;
import icu.wwj.proxy.connection.mysql.packet.command.ComQuitPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.util.List;

public class ClientCloseHandler extends ChannelOutboundHandlerAdapter {
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) {
        Promise<List<ByteBufAware>> quitPromise = new DefaultPromise<>(ctx.executor());
        ctx.writeAndFlush(new RequestContext(new ComQuitPacket(ctx.alloc()), quitPromise))
                .addListener(f -> ctx.close(promise));
    }
}
