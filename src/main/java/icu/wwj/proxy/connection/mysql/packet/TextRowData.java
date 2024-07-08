package icu.wwj.proxy.connection.mysql.packet;

import io.netty.buffer.ByteBuf;

public class TextRowData extends MySQLPacket {
    
    public TextRowData(ByteBuf byteBuf) {
        super(byteBuf);
    }
}
