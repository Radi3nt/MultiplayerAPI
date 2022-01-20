package fr.radi3nt.multi.sockets.server.socket;

import fr.radi3nt.multi.sockets.shared.distant.connection.connections.ServerConnection;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.CommunicationManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ListenerManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.ServerConnectionManager;
import fr.radi3nt.multi.sockets.server.socket.managers.ServerSocketConnectionManager;
import fr.radi3nt.multi.sockets.server.socket.managers.SocketCommunicationManager;
import fr.radi3nt.multi.sockets.server.socket.managers.SocketListenerManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerConnection implements ServerConnection {

    private final ServerSocketConnectionManager serverSocketConnectionManager;
    private final CommunicationManager communicationManager;
    private final ListenerManager listenerManager;

    public SocketServerConnection(Socket socket) {
        serverSocketConnectionManager = new ServerSocketConnectionManager(socket);
        communicationManager = new SocketCommunicationManager(this, serverSocketConnectionManager);
        listenerManager = new SocketListenerManager(serverSocketConnectionManager);
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
    public ServerConnectionManager getConnectionManager() {
        return serverSocketConnectionManager;
    }
}
