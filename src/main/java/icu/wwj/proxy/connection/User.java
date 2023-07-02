package icu.wwj.proxy.connection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class User {
    
    private final String user;
    
    private final String password;
}
