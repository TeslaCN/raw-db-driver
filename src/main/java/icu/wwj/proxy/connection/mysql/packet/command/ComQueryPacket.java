package icu.wwj.proxy.connection.mysql.packet.command;

import icu.wwj.proxy.connection.mysql.packet.Command;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;

public class ComQueryPacket extends CommandPacket {
    
    private static final Command COMMAND = Command.COM_QUERY;
    
    public ComQueryPacket(ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    public Command getCommand() {
        return COMMAND;
    }

    public ComQueryPacket(ByteBufAllocator allocator, String sql, Charset charset) {
        super(encode(allocator, sql, charset));
    }
    
    private static ByteBuf encode(ByteBufAllocator allocator, String sql, Charset charset) {
        byte[] bytes = sql.getBytes(charset);
        int capacity = 1 + bytes.length;
        return allocator.ioBuffer(capacity, capacity).writeByte(COMMAND.getType()).writeBytes(bytes);
    }
    
    public String decodeSQL(Charset charset) {
        return byteBuf().getCharSequence(1, byteBuf().readableBytes() - 1, charset).toString();
    }
}
