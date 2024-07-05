package icu.wwj.proxy.connection.mysql.packet.command;

import icu.wwj.proxy.connection.mysql.packet.MySQLCommand;
import icu.wwj.proxy.connection.mysql.packet.MySQLPacket;
import io.netty.buffer.ByteBuf;

public abstract class MySQLCommandPacket extends MySQLPacket {

    public MySQLCommandPacket(ByteBuf byteBuf) {
        super(byteBuf);
    }

    public abstract MySQLCommand getCommand();
}
