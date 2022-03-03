package fr.radi3nt.multi.main.packets.shared;

import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.logic.PacketInterceptor;
import fr.radi3nt.multi.packets.logic.PacketProtocol;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NetworkManager {

    private final Queue<PacketOut> queuedPackets = new ConcurrentLinkedQueue<>();
    private final List<QueueOptimizer> queueOptimizers = new ArrayList<>();
    private final Lock lock = new ReentrantLock(true);
    private final ConnectionManager connectionManager;
    private final PacketProtocol packetProtocol;

    public NetworkManager(ConnectionManager connectionManager, PacketProtocol packetProtocol) {
        this.connectionManager = connectionManager;
        this.packetProtocol = packetProtocol;
    }

    public void sendPacket(PacketOut packet) {
        for (QueueOptimizer queueOptimizer : queueOptimizers) {
            synchronized (lock) {
                queueOptimizer.packetAdded(packet, queuedPackets);
            }
        }

        this.queuedPackets.add(packet);
    }

    public void sendPackets() {
        sendQueuedLeft();
    }

    private void actuallySendPacket(PacketOut packet) {
        packetProtocol.sendPacket(packet);
    }

    private void sendQueuedLeft() {

        while(!this.queuedPackets.isEmpty()) {
            PacketOut queuedPacket;
            synchronized (lock) {
                queuedPacket = this.queuedPackets.poll();
            }
            if (connectionManager.isConnected()) {
                actuallySendPacket(queuedPacket);
            }
        }

    }

    public void addInterceptor(PacketInterceptor packetInterceptor) {
        packetProtocol.addInterceptor(packetInterceptor);
    }

    public void addQueueOptimizer(QueueOptimizer queueOptimizer) {
        queueOptimizers.add(queueOptimizer);
    }

    public interface QueueOptimizer {

        void packetAdded(PacketOut packet, Queue<PacketOut> packets);

    }
}
