package icu.wwj.proxy.connection.mysql.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Command {

    COM_QUIT((byte) 0x01),
    
    COM_QUERY((byte) 0x03),
    ;
    
    private final byte type;
}
