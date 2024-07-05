package icu.wwj.proxy.connection.mysql.protocol;

import icu.wwj.proxy.connection.mysql.packet.ColumnDefinition41Packet;

import java.util.List;

public class TextProtocolHandler {
    
    private List<ColumnDefinition41Packet> columnDefinitions;
    
    private int receivedRows;
}
