package icu.wwj.proxy.connection.mysql.packet;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @see <a href="https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_connection_phase_packets_protocol_handshake_v10.html">Protocol::HandshakeV10</a>
 */
public class HandshakeV10 extends MySQLPacket {
    
    private final int serverVersionOffset = 1, serverVersionEndOffset;
    
    private static final int THREAD_ID_OFFSET = 1;
    
    private static final int AUTH_PLUGIN_DATA_PART_1_OFFSET = THREAD_ID_OFFSET + 4;
    
    private static final int CAPABILITY_FLAGS_1_OFFSET = AUTH_PLUGIN_DATA_PART_1_OFFSET + 8 + 1;
    
    private static final int CHARACTER_SET_OFFSET = CAPABILITY_FLAGS_1_OFFSET + 2;
    
    private static final int STATUS_FLAGS_OFFSET = CHARACTER_SET_OFFSET + 1;
    
    private static final int CAPABILITY_FLAGS_2_OFFSET = STATUS_FLAGS_OFFSET + 2;
    
    private static final int AUTH_PLUGIN_DATA_LEN_OFFSET = CAPABILITY_FLAGS_2_OFFSET + 2;
    
    private static final int AUTH_PLUGIN_DATA_PART_2_OFFSET = AUTH_PLUGIN_DATA_LEN_OFFSET + 1 + 10;
    
    private final int authPluginDataPart2Length;
    
    public HandshakeV10(final ByteBuf byteBuf) {
        super(byteBuf);
        int readableBytes = byteBuf.readableBytes();
        serverVersionEndOffset = byteBuf.indexOf(serverVersionOffset, readableBytes, (byte) 0x00);
        authPluginDataPart2Length = Math.max(13, byteBuf.getUnsignedByte(serverVersionEndOffset + AUTH_PLUGIN_DATA_LEN_OFFSET) - 8);
    }
    
    public String decodeServerVersion() {
        return byteBuf().getCharSequence(serverVersionOffset, serverVersionEndOffset - serverVersionOffset, StandardCharsets.US_ASCII).toString();
    }
    
    public int decodeThreadId() {
        return byteBuf().getIntLE(serverVersionEndOffset + THREAD_ID_OFFSET);
    }
    
    public int decodeCapabilities() {
        return byteBuf().getUnsignedShortLE(serverVersionEndOffset + CAPABILITY_FLAGS_1_OFFSET)
                | byteBuf().getUnsignedShortLE(serverVersionEndOffset + CAPABILITY_FLAGS_2_OFFSET) << 16;
    }
    
    public byte[] decodeAuthPluginData() {
        byte[] result = new byte[8 + authPluginDataPart2Length - 1];
        byteBuf().getBytes(serverVersionEndOffset + AUTH_PLUGIN_DATA_PART_1_OFFSET, result, 0, 8);
        byteBuf().getBytes(serverVersionEndOffset + AUTH_PLUGIN_DATA_PART_2_OFFSET, result, 8, authPluginDataPart2Length - 1);
        return result;
    }
    
    public String decodeAuthPluginName() {
        int begin = serverVersionEndOffset + AUTH_PLUGIN_DATA_PART_2_OFFSET + authPluginDataPart2Length;
        int length = byteBuf().bytesBefore(begin, byteBuf().writerIndex() - begin, (byte) 0);
        return byteBuf().getCharSequence(begin, length, StandardCharsets.US_ASCII).toString();
    }
}
