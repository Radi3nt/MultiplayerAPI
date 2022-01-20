package fr.radi3nt.multi.main.packets.shared.connection;

import fr.radi3nt.multi.sockets.server.socket.SocketServerConnection;
import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;
import fr.radi3nt.multi.sockets.shared.distant.connection.handle.ConnectionHandler;
import fr.radi3nt.multi.sockets.shared.distant.connection.handle.ConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnectionListener implements ConnectionListener {

    private final ConnectionHandler connectionHandler;
    private final int port;
    private ServerSocket serverSocket;
    private boolean acceptRequests = false;

    public SocketConnectionListener(int port, ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.port = port;
    }

    public void update() {
        if (acceptRequests) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                if (!e.getMessage().equalsIgnoreCase("socket closed"))
                    e.printStackTrace();
            }

            if (socket != null) {
                connectionHandler.onConnection(createServerConnection(socket));
            }
        }

    }

    private Connection createServerConnection(Socket socket) {
        return new SocketServerConnection(socket);
    }

    @Override
    public void open() {
        acceptRequests= true;
        if (serverSocket==null) {
            try {
                this.serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                throw new UnsupportedOperationException("Cannot create socket", e);
            }
        }
    }

    @Override
    public void close() {
        acceptRequests = false;
    }

    @Override
    public void delete() {
        close();
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverSocket = null;
    }

    public boolean isOpened() {
        return acceptRequests;
    }
}
