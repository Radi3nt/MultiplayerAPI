package fr.radi3nt.multi.sockets.shared.distant.connection.connections;

import fr.radi3nt.multi.sockets.shared.distant.managing.managers.ServerConnectionManager;

public interface ServerConnection extends Connection {

    ServerConnectionManager getConnectionManager();

}
