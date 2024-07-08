package icu.wwj.proxy.connection.mysql.packet;

import io.netty.buffer.ByteBuf;

public class ColumnCountPacket extends MySQLPacket {
    
    public ColumnCountPacket(ByteBuf byteBuf) {
        super(byteBuf);
    }
    
    public int decodeColumnCount() {
        return MySQLUtils.getLenencInt(byteBuf(), byteBuf().readerIndex()).intValue();
    }
}
