package icu.wwj.proxy.connection.mysql.packet.command;

import icu.wwj.proxy.connection.mysql.packet.Command;
import io.netty.buffer.ByteBufAllocator;

public class ComQuitPacket extends CommandPacket {

    private static final Command COMMAND = Command.COM_QUIT;

    public ComQuitPacket(ByteBufAllocator allocator) {
        super(allocator.ioBuffer(1, 1).writeByte(COMMAND.getType()));
    }

    @Override
    public Command getCommand() {
        return COMMAND;
    }
}
