package fr.radi3nt.multi.sockets.client.socket;

import fr.radi3nt.multi.sockets.client.socket.managers.ClientSocketConnectionManager;
import fr.radi3nt.multi.sockets.server.socket.managers.SocketCommunicationManager;
import fr.radi3nt.multi.sockets.server.socket.managers.SocketListenerManager;
import fr.radi3nt.multi.sockets.shared.distant.connection.address.Address;
import fr.radi3nt.multi.sockets.shared.distant.connection.connections.ClientConnection;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.ClientConnectionManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.CommunicationManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ListenerManager;

public class SocketClientConnection implements ClientConnection {

    private final ClientSocketConnectionManager connectionManager;
    private final CommunicationManager communicationManager;
    private final ListenerManager listenerManager;

    public SocketClientConnection(Address server) {
        connectionManager = new ClientSocketConnectionManager(this, server);
        communicationManager = new SocketCommunicationManager(this, connectionManager);
        listenerManager = new SocketListenerManager(connectionManager);
    }

    @Override
    public CommunicationManager getCommunicationManager() {
        return communicationManager;
    }

    @Override
    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
