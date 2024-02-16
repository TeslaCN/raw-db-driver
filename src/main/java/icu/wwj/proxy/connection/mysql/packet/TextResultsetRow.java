package icu.wwj.proxy.connection.mysql.packet;

import io.netty.buffer.ByteBuf;

public class TextResultsetRow extends MySQLPacket {
    
    public TextResultsetRow(ByteBuf byteBuf) {
        super(byteBuf);
    }
}
