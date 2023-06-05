package icu.wwj.proxy.connection.mysql.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class MySQLPacketDecoder extends LengthFieldBasedFrameDecoder {

    private static final int MAX_PACKET_LENGTH = 0xFFFFFF;

    private final List<ByteBuf> pendingByteBuf = new ArrayList<>();

    public MySQLPacketDecoder() {
        super(ByteOrder.LITTLE_ENDIAN, MAX_PACKET_LENGTH, 0, 3, 0, 4, true);
    }

    @Override
    protected ByteBuf decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf byteBuf = (ByteBuf) super.decode(ctx, in);
        if (null == byteBuf) {
            return null;
        }
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
        return alloc.compositeBuffer(pendingByteBuf.size() + 1)
                .addComponents(true, pendingByteBuf)
                .addComponents(true, byteBuf);
    }
}
