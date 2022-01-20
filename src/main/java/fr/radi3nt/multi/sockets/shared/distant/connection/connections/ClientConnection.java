package fr.radi3nt.multi.sockets.shared.distant.connection.connections;

import fr.radi3nt.multi.sockets.shared.distant.managing.managers.ClientConnectionManager;

public interface ClientConnection extends Connection {

    ClientConnectionManager getConnectionManager();

}
