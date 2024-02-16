package icu.wwj.proxy.connection.mysql.handler;

import com.mysql.cj.protocol.Security;
import icu.wwj.proxy.connection.ConnectionOption;
import icu.wwj.proxy.connection.User;
import icu.wwj.proxy.connection.mysql.packet.HandshakeResponse;
import icu.wwj.proxy.connection.mysql.packet.HandshakeV10;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.RequiredArgsConstructor;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class ClientAuthenticationHandler extends ChannelDuplexHandler {
    
    private final User user;
    
    private final ConnectionOption connectionOption;
    
    private ChannelPromise handshakePromise;
    
    private boolean handshakeReceived;
    
    @Override
    public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        handshakePromise = promise;
        ChannelPromise tcpPromise = ctx.newPromise();
        super.connect(ctx, remoteAddress, localAddress, tcpPromise);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        if (!handshakeReceived) {
            handshakeResponse(byteBuf, ctx);
            return;
        }
        if (0 == byteBuf.getByte(byteBuf.readerIndex())) {
            ctx.channel().pipeline().remove(this);
            handshakePromise.trySuccess();
        }
    }
    
    private void handshakeResponse(final ByteBuf msg, final ChannelHandlerContext ctx) {
        handshakeReceived = true;
        HandshakeV10 handshakeV10 = new HandshakeV10(msg);
        byte[] seed = handshakeV10.decodeAuthPluginData();
        int capabilities = handshakeV10.decodeCapabilities();
        String authPluginName = handshakeV10.decodeAuthPluginName();
        byte[] authResponse = Security.scramble411(user.getPassword().getBytes(StandardCharsets.US_ASCII), seed);
        msg.release();
        // TODO Values need to be configurable
        HandshakeResponse response = new HandshakeResponse(ctx.alloc().ioBuffer(999), capabilities, 0xFFFFFF, user.getUser(), authResponse, "", 224, authPluginName);
        ctx.writeAndFlush(response);
    }
}
