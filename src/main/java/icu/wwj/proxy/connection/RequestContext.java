package icu.wwj.proxy.connection;

import icu.wwj.proxy.connection.mysql.protocol.TextResultSet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Promise;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestContext {

    private final ByteBufAware command;

    private final Promise<List<ByteBufAware>> promise;

    private final TextResultSet responseHandler;
    
    private ChannelHandlerContext channelHandlerContext;
    
    private boolean sent;

    public RequestContext(ByteBufAware command, Promise<List<ByteBufAware>> promise) {
        this.command = command;
        this.promise = promise;
        responseHandler = new TextResultSet(promise);
    }
}
