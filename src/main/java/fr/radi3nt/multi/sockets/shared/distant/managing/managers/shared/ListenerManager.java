package fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared;

import fr.radi3nt.multi.sockets.shared.distant.managing.listener.ConnectionListener;

public interface ListenerManager {

    void addListener(ConnectionListener connectionListener);
    void addTimeoutListener(ConnectionListener connectionListener, long secondTimeOut);
    void removeListener(ConnectionListener connectionListener);
    boolean isListener(ConnectionListener connectionListener);

    void update();

    void finish();
}
