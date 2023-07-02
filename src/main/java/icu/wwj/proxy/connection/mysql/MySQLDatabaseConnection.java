package icu.wwj.proxy.connection.mysql;

import icu.wwj.proxy.connection.DatabaseConnection;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicInteger;

public interface MySQLDatabaseConnection extends DatabaseConnection {
    
    AttributeKey<AtomicInteger> SEQUENCE_ID = AttributeKey.valueOf("MYSQL_SEQUENCE_ID");
}
