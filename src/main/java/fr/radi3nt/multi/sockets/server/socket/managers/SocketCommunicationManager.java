package fr.radi3nt.multi.sockets.server.socket.managers;

import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.impl.SocketConnectionManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.CommunicationManager;
import fr.radi3nt.multi.sockets.shared.distant.request.Request;

import java.io.DataOutputStream;
import java.io.IOException;

public class SocketCommunicationManager implements CommunicationManager {

    private final Connection connection;
    private final SocketConnectionManager serverSocketConnectionManager;
    private DataOutputStream dataOutputStream;

    public SocketCommunicationManager(Connection connection, SocketConnectionManager serverSocketConnectionManager) {
        this.connection = connection;
        this.serverSocketConnectionManager = serverSocketConnectionManager;
    }

    @Override
    public void send(Request request) {
        if (!serverSocketConnectionManager.isConnected())
            throw new IllegalStateException("Socket is not connected");

        if (dataOutputStream == null) {
            try {
                dataOutputStream = new DataOutputStream(serverSocketConnectionManager.getCurrentSocket().getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!request.send(connection, dataOutputStream)) {
            serverSocketConnectionManager.close();
        }
    }
}
