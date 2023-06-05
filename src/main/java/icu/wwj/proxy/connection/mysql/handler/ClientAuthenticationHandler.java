package icu.wwj.proxy.connection.mysql.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientAuthenticationHandler extends ChannelInboundHandlerAdapter {
    
    private boolean handshakeReceived;
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (!handshakeReceived) {
            
        }
    }
}
