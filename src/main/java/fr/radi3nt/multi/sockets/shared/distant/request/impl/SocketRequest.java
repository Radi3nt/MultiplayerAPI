package fr.radi3nt.multi.sockets.shared.distant.request.impl;

import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;
import fr.radi3nt.multi.sockets.shared.distant.request.Request;

import java.io.*;

public class SocketRequest implements Request {

    private final long id;
    private byte[] data;

    public SocketRequest() {
        this.id = SocketRequestIdCreator.generateId();
    }

    public SocketRequest(byte[] data) {
        this.id = SocketRequestIdCreator.generateId();
        this.data = data;
    }

    protected void setData(byte[] data) {
        this.data = data;
    }

    public void send(Connection connection, OutputStream socket) {
        try {
            socket.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected long getId() {
        return id;
    }

    @Override
    public void terminate() {
        SocketRequestIdCreator.releaseId(id);
    }
}
