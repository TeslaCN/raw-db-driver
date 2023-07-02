package icu.wwj.proxy.connection.mysql.packet;

import com.mysql.cj.CharsetMapping;
import com.mysql.cj.protocol.a.NativeServerSession;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class HandshakeResponse extends MySQLPacket {
    
    public HandshakeResponse(final ByteBuf byteBuf, final int capabilityFlags, final int maxPacketSize, final String username, final byte[] authResponse, final String database, final int collation, final CharSequence clientPluginName) {
        super(byteBuf);
        // TODO Complete client flags
        int clientFlag = capabilityFlags & NativeServerSession.CLIENT_LONG_PASSWORD //
//                | (this.propertySet.getBooleanProperty(PropertyKey.useAffectedRows).getValue() ? //
//                0 : capabilityFlags & NativeServerSession.CLIENT_FOUND_ROWS) //
                | capabilityFlags & NativeServerSession.CLIENT_FOUND_ROWS
                | capabilityFlags & NativeServerSession.CLIENT_LONG_FLAG //
                | (!database.isEmpty() ? capabilityFlags & NativeServerSession.CLIENT_CONNECT_WITH_DB : 0) //
//                | (this.propertySet.getBooleanProperty(PropertyKey.useCompression).getValue() ? //
//                capabilityFlags & NativeServerSession.CLIENT_COMPRESS : 0) //
//                | (this.propertySet.getBooleanProperty(PropertyKey.allowLoadLocalInfile).getValue()
//                || this.propertySet.getStringProperty(PropertyKey.allowLoadLocalInfileInPath).isExplicitlySet() ? //
//                capabilityFlags & NativeServerSession.CLIENT_LOCAL_FILES : 0) //
                | capabilityFlags & NativeServerSession.CLIENT_PROTOCOL_41 //
//                | (this.propertySet.getBooleanProperty(PropertyKey.interactiveClient).getValue() ? //
//                capabilityFlags & NativeServerSession.CLIENT_INTERACTIVE : 0) //
//                | (this.propertySet.<PropertyDefinitions.SslMode>getEnumProperty(PropertyKey.sslMode).getValue() != PropertyDefinitions.SslMode.DISABLED ? //
//                capabilityFlags & NativeServerSession.CLIENT_SSL : 0) //
                | capabilityFlags & NativeServerSession.CLIENT_TRANSACTIONS // Required to get server status values.
                | NativeServerSession.CLIENT_SECURE_CONNECTION //
//                | (this.propertySet.getBooleanProperty(PropertyKey.allowMultiQueries).getValue() ? //
//                capabilityFlags & NativeServerSession.CLIENT_MULTI_STATEMENTS : 0) //
//                | capabilityFlags & NativeServerSession.CLIENT_MULTI_RESULTS // Always allow multiple result sets.
//                | capabilityFlags & NativeServerSession.CLIENT_PS_MULTI_RESULTS // Always allow multiple result sets for SSPS.
                | NativeServerSession.CLIENT_PLUGIN_AUTH //
//                | (NONE.equals(this.propertySet.getStringProperty(PropertyKey.connectionAttributes).getValue()) ? //
//                0 : capabilityFlags & NativeServerSession.CLIENT_CONNECT_ATTRS) //
                | capabilityFlags & NativeServerSession.CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA //
//                | (this.propertySet.getBooleanProperty(PropertyKey.disconnectOnExpiredPasswords).getValue() ? //
//                0 : capabilityFlags & NativeServerSession.CLIENT_CAN_HANDLE_EXPIRED_PASSWORD) //
//                | (this.propertySet.getBooleanProperty(PropertyKey.trackSessionState).getValue() ? // 
//                capabilityFlags & NativeServerSession.CLIENT_SESSION_TRACK : 0) //
                | capabilityFlags & NativeServerSession.CLIENT_DEPRECATE_EOF //
//                | capabilityFlags & NativeServerSession.CLIENT_QUERY_ATTRIBUTES // 
//                | capabilityFlags & NativeServerSession.CLIENT_MULTI_FACTOR_AUTHENTICATION
                ;
        byteBuf.writeIntLE(clientFlag);
        byteBuf.writeIntLE(maxPacketSize * 100);
        byteBuf.writeByte(collation);
        byteBuf.writeZero(23);
        String encoding = CharsetMapping.getStaticJavaEncodingForCollationIndex(collation);
        byteBuf.writeCharSequence(username, Charset.forName(encoding));
        byteBuf.writeZero(1);
        MySQLUtils.writeStringLenenc(byteBuf, authResponse);
        if (!database.isEmpty()) {
            byteBuf.writeCharSequence(database, Charset.forName(encoding));
            byteBuf.writeZero(1);
        }
        byteBuf.writeCharSequence(clientPluginName, Charset.forName(encoding));
        byteBuf.writeZero(1);
    }
}
