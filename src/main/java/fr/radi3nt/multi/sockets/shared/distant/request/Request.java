package fr.radi3nt.multi.sockets.shared.distant.request;

import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;

import java.io.OutputStream;

public interface Request {

    void send(Connection connection, OutputStream outputStream);
    void terminate();

}
