package fr.radi3nt.multi.sockets.shared.distant.connection.handle;

import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;

import java.util.Collection;

public interface ConnectionListener {

    void open();
    void close();
    void delete();

}
