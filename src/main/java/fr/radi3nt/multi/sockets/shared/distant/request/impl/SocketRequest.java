package fr.radi3nt.multi.sockets.shared.distant.request.impl;

import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;
import fr.radi3nt.multi.sockets.shared.distant.request.Request;

import java.io.IOException;
import java.io.OutputStream;

public class SocketRequest implements Request {

    private final byte[] data;

    public SocketRequest(byte[] data) {
        this.data = data;
    }

    public void send(Connection connection, OutputStream socket) {
        try {
            socket.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void terminate() {

    }
}
