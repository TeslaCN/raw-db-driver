package icu.wwj.proxy.connection.mysql.packet;

import icu.wwj.proxy.connection.ByteBufAware;
import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MySQLPacket implements ByteBufAware {
    
    private final ByteBuf byteBuf;
    
    @Override
    public final ByteBuf byteBuf() {
        return byteBuf;
    }
}
