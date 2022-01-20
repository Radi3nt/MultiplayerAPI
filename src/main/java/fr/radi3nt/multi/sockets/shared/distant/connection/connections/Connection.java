package fr.radi3nt.multi.sockets.shared.distant.connection.connections;

import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.CommunicationManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ConnectionManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ListenerManager;

public interface Connection {

    ConnectionManager getConnectionManager();
    CommunicationManager getCommunicationManager();
    ListenerManager getListenerManager();

}
