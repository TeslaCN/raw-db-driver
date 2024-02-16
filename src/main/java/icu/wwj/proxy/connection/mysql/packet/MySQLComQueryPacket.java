package icu.wwj.proxy.connection.mysql.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;

public class MySQLComQueryPacket extends MySQLPacket {
    
    public static final byte TYPE = 0x03;
    
    public MySQLComQueryPacket(ByteBuf byteBuf) {
        super(byteBuf);
    }
    
    public MySQLComQueryPacket(ByteBufAllocator allocator, String sql, Charset charset) {
        super(encode(allocator, sql, charset));
    }
    
    private static ByteBuf encode(ByteBufAllocator allocator, String sql, Charset charset) {
        byte[] bytes = sql.getBytes(charset);
        int capacity = 1 + bytes.length;
        return allocator.ioBuffer(capacity, capacity).writeByte(TYPE).writeBytes(bytes);
    }
    
    public String decodeSQL(Charset charset) {
        return byteBuf().getCharSequence(1, byteBuf().readableBytes() - 1, charset).toString();
    }
}
