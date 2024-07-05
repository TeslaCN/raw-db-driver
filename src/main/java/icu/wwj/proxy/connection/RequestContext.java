package icu.wwj.proxy.connection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Promise;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class RequestContext {

    private final ByteBufAware byteBufAware;

    private final Promise<List<ByteBufAware>> promise;
    
    private ChannelHandlerContext channelHandlerContext;
    
    private boolean sent;
}
