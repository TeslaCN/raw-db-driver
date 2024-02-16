package icu.wwj.proxy.connection;

import io.netty.util.concurrent.Promise;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Request {
    
    private final ByteBufAware request;
    
    private final Promise<List<ByteBufAware>> promise;
}
