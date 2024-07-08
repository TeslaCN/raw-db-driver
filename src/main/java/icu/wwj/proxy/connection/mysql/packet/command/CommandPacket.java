package icu.wwj.proxy.connection.mysql.packet.command;

import icu.wwj.proxy.connection.mysql.packet.Command;
import icu.wwj.proxy.connection.mysql.packet.MySQLPacket;
import io.netty.buffer.ByteBuf;

public abstract class CommandPacket extends MySQLPacket {

    public CommandPacket(ByteBuf byteBuf) {
        super(byteBuf);
    }

    public abstract Command getCommand();
}
