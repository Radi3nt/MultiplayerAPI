package fr.radi3nt.multi.sockets.server.socket.managers;

import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.ServerConnectionManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.impl.SocketConnectionManager;

import java.io.IOException;
import java.net.Socket;

public class ServerSocketConnectionManager implements ServerConnectionManager, SocketConnectionManager {

    private Socket currentConnectionSocket;

    public ServerSocketConnectionManager(Socket socket) {
        this.currentConnectionSocket = socket;
    }

    @Override
    public void close() {
        socketIsAccepted();
        try {
            currentConnectionSocket.close();
            currentConnectionSocket = null;
        } catch (IOException e) {
            throw new UnsupportedOperationException("Socket could not be closed", e);
        }
    }

    @Override
    public boolean isConnected() {
        return currentConnectionSocket!=null && currentConnectionSocket.isConnected() && !currentConnectionSocket.isClosed();
    }

    private void socketIsAccepted() {
        if (currentConnectionSocket==null || !currentConnectionSocket.isConnected())
            throw new IllegalStateException("Connection is not opened");
    }

    private void socketNotBeAccepted() {
        if (currentConnectionSocket!=null && currentConnectionSocket.isConnected())
            throw new IllegalStateException("Connection is already opened");
    }

    @Override
    public Socket getCurrentSocket() {
        return currentConnectionSocket;
    }
}
