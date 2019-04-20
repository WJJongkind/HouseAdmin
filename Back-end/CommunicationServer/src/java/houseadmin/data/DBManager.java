/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package houseadmin.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Wessel
 */
public class DBManager {
    private static ConnectionProperties connectionProperties;
    private static final ConcurrentHashMap<Connection, Integer> readOnlyConnections = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Connection, Integer> mutableConnections = new ConcurrentHashMap<>();
    
    public static Connection getConnection(boolean readOnly) throws SQLException, ClassNotFoundException, IOException {
        if(connectionProperties == null) {
            connectionProperties = new ConnectionProperties();
        }
        
        return getConnectionFromPool(readOnly);
    }
    
    public static boolean releaseConnection(Connection connection) throws SQLException {
        if(connection == null || connection.isClosed()) {
            return false;
        } else {
            
            return (connection.isReadOnly() ? readOnlyConnections : mutableConnections).put(connection, 0) == null;
        }
    }
    
    private static Connection getConnectionFromPool(boolean readOnly) throws SQLException, ClassNotFoundException {
        Enumeration<Connection> pool = readOnly ? DBManager.readOnlyConnections.keys() : DBManager.mutableConnections.keys();
        
        while(pool.hasMoreElements()) {
            Connection connection = pool.nextElement();
            if(!connection.isClosed()) {
                return connection;
            } else {
                if(readOnly) {
                    DBManager.readOnlyConnections.remove(connection);
                } else {
                    DBManager.mutableConnections.remove(connection);
                }
            }
        }
        
        // No connection found, create a new one...
        return connectDB(readOnly);
    }
    
    private static Connection connectDB(boolean readOnly) throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection connection = DriverManager.getConnection(connectionProperties.host, 
                                                            connectionProperties.username, 
                                                            new String(connectionProperties.password));
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        connection.setReadOnly(readOnly);
        
        
        return connection;
    }
    
    
    private static class ConnectionProperties {
        private char[] password;
        private String host;
        private String username;
        
        public ConnectionProperties() throws FileNotFoundException, IOException {
            try (FileReader fr = new FileReader(new File("dbconfig.cfg")); BufferedReader reader = new BufferedReader(fr)) {
                host = reader.readLine();
                username = reader.readLine();
                password = reader.readLine().toCharArray();
            }
        }
    }
}
