package fr.radi3nt.multi.sockets.shared.distant.managing.listener;

import java.io.DataInputStream;

public interface ConnectionListener {

    void read(DataInputStream dataInputStream);

}
