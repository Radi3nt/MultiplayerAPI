package fr.radi3nt.multi.sockets.client.socket.managers;

import fr.radi3nt.multi.sockets.shared.distant.connection.address.Address;
import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.ClientConnectionManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.impl.SocketConnectionManager;
import fr.radi3nt.multi.sockets.shared.util.Timings;

import java.io.IOException;
import java.net.Socket;

public class ClientSocketConnectionManager implements ClientConnectionManager, SocketConnectionManager {

    private final Connection connection;
    private final Address serverAddress;
    private Socket socket;

    public ClientSocketConnectionManager(Connection connection, Address serverAddress) {
        this.connection = connection;
        this.serverAddress = serverAddress;
    }


    @Override
    public void connect() {
        shouldNotBeCreated();
        createSocket();
    }

    private void createSocket() {
        try {
            socket = new Socket(serverAddress.getHostName(), serverAddress.getPort());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create socket", e);
        }
    }

    @Override
    public void close() {
        shouldBeCreated();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return socket!=null && socket.isConnected() && !socket.isClosed();
    }

    private void shouldBeCreated() {
        if (socket==null) {
            throw new IllegalStateException("Socket is not created");
        }
    }

    private void shouldNotBeCreated() {
        if (socket!=null) {
            throw new IllegalStateException("Socket should not be created");
        }
    }

    @Override
    public Socket getCurrentSocket() {
        return socket;
    }
}
