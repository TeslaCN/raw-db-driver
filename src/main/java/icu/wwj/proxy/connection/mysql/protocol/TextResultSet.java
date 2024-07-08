package icu.wwj.proxy.connection.mysql.protocol;

import icu.wwj.proxy.connection.ByteBufAware;
import icu.wwj.proxy.connection.mysql.packet.ColumnCountPacket;
import icu.wwj.proxy.connection.mysql.packet.ColumnDefinition41Packet;
import icu.wwj.proxy.connection.mysql.packet.TextRowData;
import icu.wwj.proxy.connection.mysql.packet.generic.OKPacket;
import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.Promise;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@ToString
public class TextResultSet {
    
    private final Promise<List<ByteBufAware>> promise;

    private int receivedPackets;
    
    private ByteBufAware firstPacket;

    private int columnCount = -1;
    
    private List<ColumnDefinition41Packet> columnDefinitions;

    private final List<ByteBufAware> pendingRows = new ArrayList<>();
    
    private int receivedRows;
    
    private ByteBufAware lastPacket;
    
    @Getter
    private boolean okOrEof;
    
    public void onReceived(ByteBuf byteBuf) {
        if (1 == ++receivedPackets) {
            handleFirstPacket(byteBuf);
            return;
        } 
        if (receivedPackets - 1 <= columnCount) {
            columnDefinitions.add(new ColumnDefinition41Packet(byteBuf));
            return;
        }
        if (0xFE == byteBuf.getUnsignedByte(byteBuf.readerIndex()) && byteBuf.readableBytes() < 9) {
            lastPacket = new OKPacket(byteBuf);
            okOrEof = true;
            promise.setSuccess(getAllPackets());
            return;
        }
        pendingRows.add(new TextRowData(byteBuf));
        receivedRows++;
    }

    private void handleFirstPacket(ByteBuf byteBuf) {
        columnCount = byteBuf.getUnsignedByte(byteBuf.readerIndex());
        columnDefinitions = new ArrayList<>(columnCount);
        if (0 == columnCount) {
            firstPacket = new OKPacket(byteBuf);
            promise.setSuccess(Collections.singletonList(firstPacket));
            okOrEof = true;
        } else {
            firstPacket = new ColumnCountPacket(byteBuf);
        }
    }
    
    private List<ByteBufAware> getAllPackets() {
        List<ByteBufAware> result = new ArrayList<>(receivedPackets + 1);
        result.add(firstPacket);
        result.addAll(columnDefinitions);
        result.addAll(pendingRows);
        result.add(lastPacket);
        return result;
    }
}
