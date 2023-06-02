package icu.wwj.proxy.connection;

import io.netty.buffer.ByteBuf;

public interface ByteBufAware {
    
    ByteBuf byteBuf();
}
