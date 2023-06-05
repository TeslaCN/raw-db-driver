package icu.wwj.proxy.connection.mysql.packet;

import io.netty.buffer.ByteBuf;

public class HandshakeResponse41Packet extends MySQLPacket {
    
    public HandshakeResponse41Packet(final ByteBuf byteBuf) {
        super(byteBuf);
    }
}
