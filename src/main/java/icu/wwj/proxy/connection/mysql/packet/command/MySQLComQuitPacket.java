package icu.wwj.proxy.connection.mysql.packet.command;

import icu.wwj.proxy.connection.mysql.packet.MySQLCommand;
import io.netty.buffer.ByteBufAllocator;

public class MySQLComQuitPacket extends MySQLCommandPacket {

    private static final MySQLCommand COMMAND = MySQLCommand.COM_QUIT;

    public MySQLComQuitPacket(ByteBufAllocator allocator) {
        super(allocator.ioBuffer(1, 1).writeByte(COMMAND.getType()));
    }

    @Override
    public MySQLCommand getCommand() {
        return COMMAND;
    }
}
