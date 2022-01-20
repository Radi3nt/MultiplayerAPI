package fr.radi3nt.multi.sockets.shared.distant.connection;

import fr.radi3nt.multi.sockets.shared.distant.connection.address.Address;
import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;

public class ConnectionFactory {

    private static ConnectionProvider connectionProvider;

    public static Connection createConnection(Address address) {
        return connectionProvider.createConnection(address);
    }

    public static void setConnectionProvider(ConnectionProvider connectionProvider) {
        ConnectionFactory.connectionProvider = connectionProvider;
    }

    private interface ConnectionProvider {

        Connection createConnection(Address address);

    }
}
