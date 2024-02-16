package icu.wwj.proxy.connection.mysql.packet;

import io.netty.buffer.ByteBuf;

public class OKPacket extends MySQLPacket {
    
    public static byte TYPE = 0x00;
    
    public OKPacket(ByteBuf byteBuf) {
        super(byteBuf);
    }
}
