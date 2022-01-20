package fr.radi3nt.multi.sockets.server.socket.managers;

import fr.radi3nt.multi.sockets.shared.distant.managing.listener.ConnectionListener;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.impl.SocketConnectionManager;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ListenerManager;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SocketListenerManager implements ListenerManager {

    private final SocketConnectionManager serverSocketConnectionManager;
    private final Set<ActualListener> connectionListeners = new HashSet<>();

    public SocketListenerManager(SocketConnectionManager serverSocketConnectionManager) {
        this.serverSocketConnectionManager = serverSocketConnectionManager;
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
        try {
            _update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _update() throws IOException {
        InputStream inputStream = serverSocketConnectionManager.getCurrentSocket().getInputStream();
        DataInputStream dis = new DataInputStream(inputStream);
        int limit = dis.available();

        if (limit>=Integer.BYTES) {
            int bytesRead = 0;
            int bytesToRead = dis.readInt();

            ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayInputStream);

            byte[] partBytes = new byte[bytesToRead];

            do {
                int actualReadBytes = dis.read(partBytes, 0, bytesToRead - bytesRead);
                dataOutputStream.write(partBytes, 0, actualReadBytes);
                bytesRead += actualReadBytes;
            } while (bytesRead != bytesToRead);

            byte[] messageByte = byteArrayInputStream.toByteArray();
            dataOutputStream.flush();

            for (ActualListener connectionListener : connectionListeners) {
                if (connectionListener.needSupress())
                    connectionListeners.remove(connectionListener);
                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(messageByte, 0, bytesToRead));
                connectionListener.connectionListener.read(dataInputStream);
            }
        }
    }

    public static int intToByteArray(int[] value, byte[] bytes, int offset) {
        int size = 0;

        for (int i = 0; i < value.length; i++) {
            int intBits = value[i];
            for (int a = 0; a < Integer.BYTES; a++) {
                bytes[i * Integer.BYTES + a + offset] = new Integer(intBits).byteValue();
                intBits = intBits >> 8;
                size++;
            }
        }
        return size;
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
