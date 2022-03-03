package fr.radi3nt.multi.sockets.server.socket.managers;

import fr.radi3nt.multi.sockets.shared.distant.managing.listener.ConnectionListener;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.impl.SocketConnectionManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ListenerManager;
import fr.radi3nt.multi.sockets.shared.util.Timings;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SocketListenerManager implements ListenerManager {

    private final SocketConnectionManager serverSocketConnectionManager;
    private final Set<ActualListener> connectionListeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Queue<ByteArrayOutputStream> inPackets = new ConcurrentLinkedQueue<>();
    private final Thread thread;

    private boolean firstExecution = true;
    private DataInputStream enteringPackets;

    public SocketListenerManager(SocketConnectionManager serverSocketConnectionManager) {
        this.serverSocketConnectionManager = serverSocketConnectionManager;
        thread = new Thread(() -> {
            while (serverSocketConnectionManager.isConnected()) {
                ByteArrayOutputStream byteArrayOutputStream = inPackets.poll();
                if (byteArrayOutputStream!=null) {
                    byte[] messageByte = byteArrayOutputStream.toByteArray();
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (ActualListener connectionListener : connectionListeners) {
                        if (connectionListener.needSupress())
                            connectionListeners.remove(connectionListener);
                        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(messageByte, 0, messageByte.length));
                        connectionListener.connectionListener.read(dataInputStream);
                        try {
                            dataInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Packet Listener Runner");
    }

    @Override
    public void addListener(ConnectionListener connectionListener) {
        connectionListeners.add(new ActualListener(connectionListener));
    }

    @Override
    public void addTimeoutListener(ConnectionListener connectionListener, long secondTimeOut) {
        connectionListeners.add(new ActualListener(connectionListener, secondTimeOut));
    }

    @Override
    public void removeListener(ConnectionListener connectionListener) {
        connectionListeners.remove(new ActualListener(connectionListener));
    }

    @Override
    public boolean isListener(ConnectionListener connectionListener) {
        return connectionListeners.contains(new ActualListener(connectionListener));
    }

    @Override
    public void update() {
        if (!serverSocketConnectionManager.isConnected())
            throw new IllegalStateException("Socket is not connected");

        if (firstExecution) {
            firstExecution = false;

            thread.start();
            try {
                InputStream inputStream = serverSocketConnectionManager.getCurrentSocket().getInputStream();
                enteringPackets = new DataInputStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            _update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        try {
            enteringPackets.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _update() throws IOException {
        int limit = enteringPackets.available();

        if (limit>=Integer.BYTES) {
            int bytesRead = 0;
            int bytesToRead = enteringPackets.readInt();

            ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayInputStream);

            byte[] partBytes = new byte[bytesToRead];

            do {
                int actualReadBytes = enteringPackets.read(partBytes, 0, bytesToRead - bytesRead);
                dataOutputStream.write(partBytes, 0, actualReadBytes);
                bytesRead += actualReadBytes;
            } while (bytesRead != bytesToRead);

            inPackets.add(byteArrayInputStream);
        }
    }

    public static class ActualListener {

        private final ConnectionListener connectionListener;
        private final long added = System.currentTimeMillis();
        private final long timeout;

        public ActualListener(ConnectionListener connectionListener, long timeout) {
            this.connectionListener = connectionListener;
            this.timeout = timeout;
        }

        public ActualListener(ConnectionListener connectionListener) {
            this.connectionListener = connectionListener;
            this.timeout = -1;
        }

        public boolean needSupress() {
            return timeout != -1 && added + timeout > System.currentTimeMillis();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ActualListener that = (ActualListener) o;
            return Objects.equals(connectionListener, that.connectionListener);
        }

        @Override
        public int hashCode() {
            return Objects.hash(connectionListener);
        }
    }
}
