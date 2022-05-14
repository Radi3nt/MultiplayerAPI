package fr.radi3nt.multi.sockets.shared.distant.request.impl;

import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;
import fr.radi3nt.multi.sockets.shared.distant.request.Request;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SocketRequest implements Request {

    private final ByteBuffer data;

    public SocketRequest(ByteBuffer data) {
        this.data = data;
    }

    public boolean send(Connection connection, OutputStream socket) {
        try {
            socket.write(data.array());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
