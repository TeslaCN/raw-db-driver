package icu.wwj.proxy.connection.mysql.packet;

import io.netty.buffer.ByteBuf;

public class MySQLUtils {
    
    public static void writeStringLenenc(ByteBuf byteBuf, byte[] bytes) {
        int length = bytes.length;
        if (length <= 0xFB) {
            byteBuf.writeByte(length);
        } else if (length <= 0xFFFF) {
            byteBuf.writeByte(0xFC);
            byteBuf.writeShortLE(length);
        } else if (length <= 0xFFFFFF) {
            byteBuf.writeByte(0xFD);
            byteBuf.writeMediumLE(length);
        } else {
            byteBuf.writeByte(0xFE);
            byteBuf.writeIntLE(length);
        }
        byteBuf.writeBytes(bytes);
    }
}
