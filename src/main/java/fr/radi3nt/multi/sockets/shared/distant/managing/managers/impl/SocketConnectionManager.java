package fr.radi3nt.multi.sockets.shared.distant.managing.managers.impl;

import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ConnectionManager;

import java.net.Socket;

public interface SocketConnectionManager extends ConnectionManager {

    Socket getCurrentSocket();

}
