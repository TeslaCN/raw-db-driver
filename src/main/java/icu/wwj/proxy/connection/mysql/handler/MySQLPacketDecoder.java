package icu.wwj.proxy.connection.mysql.handler;

import icu.wwj.proxy.connection.mysql.MySQLDatabaseConnection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MySQLPacketDecoder extends LengthFieldBasedFrameDecoder {

    private static final int MAX_PACKET_LENGTH = 0xFFFFFF;

    private final List<ByteBuf> pendingByteBuf = new ArrayList<>();

    public MySQLPacketDecoder() {
        super(ByteOrder.LITTLE_ENDIAN, MAX_PACKET_LENGTH, 0, 3, 1, 3, true);
    }

    @Override
    protected ByteBuf decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf byteBuf = (ByteBuf) super.decode(ctx, in);
        if (null == byteBuf) {
            return null;
        }
        int sequenceId = byteBuf.readUnsignedByte();
        ctx.channel().attr(MySQLDatabaseConnection.SEQUENCE_ID).get().set(sequenceId + 1);
        int packetLength = byteBuf.readableBytes();
        if (MAX_PACKET_LENGTH == packetLength) {
            pendingByteBuf.add(byteBuf);
            return null;
        }
        if (!pendingByteBuf.isEmpty() && packetLength < MAX_PACKET_LENGTH) {
            return aggregatePendingByteBuf(ctx.alloc(), byteBuf);
        }
        return byteBuf;
    }

    private CompositeByteBuf aggregatePendingByteBuf(ByteBufAllocator alloc, ByteBuf byteBuf) {
        CompositeByteBuf result = alloc.compositeBuffer(pendingByteBuf.size() + 1)
                .addComponents(true, pendingByteBuf)
                .addComponents(true, byteBuf);
        pendingByteBuf.clear();
        return result;
    }
}
