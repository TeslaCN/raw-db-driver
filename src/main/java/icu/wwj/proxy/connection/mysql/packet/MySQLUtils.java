package icu.wwj.proxy.connection.mysql.packet;

import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

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
    
    public static Number getLenencInt(ByteBuf byteBuf, int readerIndex) {
        int first = byteBuf.getUnsignedByte(readerIndex);
        if (first < 0xFB) {
            return first;
        }
        if (first == 0xFC) {
            return byteBuf.getUnsignedShortLE(readerIndex + 1);
        }
        if (first == 0xFD) {
            return byteBuf.getUnsignedMediumLE(readerIndex + 1);
        }
        long value = byteBuf.getLongLE(readerIndex + 1);
        if (value >= 0) {
            return value;
        }
        return BigInteger.valueOf(value & Long.MAX_VALUE).add(BigInteger.ONE.shiftLeft(63));
    }
}
