package fr.radi3nt.multi.main.packets.server.connection;

import fr.radi3nt.multi.main.packets.shared.connection.SocketConnectionListener;

public class QueryConnectionListener {

    private final SocketConnectionListener socketConnectionListener;
    private final Thread listeningThread;
    private boolean closed = true;

    public QueryConnectionListener(SocketConnectionListener socketConnectionListener) {
        this.socketConnectionListener = socketConnectionListener;
        listeningThread = new Thread(() -> {
            while (isNotClosed()) {
                update();
            }
        }, "Listening thread");
    }

    public void open() {
        if (closed && !socketConnectionListener.isOpened())  {
            socketConnectionListener.open();
            listeningThread.start();
            closed = false;

        }
    }

    public void close() {
        if (!closed) {
            closed = true;

            socketConnectionListener.delete();
        }
    }

    public void update() {

        if (!closed && socketConnectionListener.isOpened()) {
            socketConnectionListener.update();
        }

    }

    public boolean isNotClosed() {
        return !closed;
    }

}
