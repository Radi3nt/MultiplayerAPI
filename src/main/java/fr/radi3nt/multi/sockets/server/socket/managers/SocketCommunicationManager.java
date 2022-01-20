package fr.radi3nt.multi.sockets.server.socket.managers;

import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.CommunicationManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.impl.SocketConnectionManager;
import fr.radi3nt.multi.sockets.shared.distant.request.Request;

import java.io.IOException;

public class SocketCommunicationManager implements CommunicationManager {

    private final Connection connection;
    private final SocketConnectionManager serverSocketConnectionManager;

    public SocketCommunicationManager(Connection connection, SocketConnectionManager serverSocketConnectionManager) {
        this.connection = connection;
        this.serverSocketConnectionManager = serverSocketConnectionManager;
    }

    @Override
    public void send(Request request) {
        if (!serverSocketConnectionManager.isConnected())
            throw new IllegalStateException("Socket is not connected");
        try {
            request.send(connection, serverSocketConnectionManager.getCurrentSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
