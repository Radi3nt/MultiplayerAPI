package fr.radi3nt.multi.main.packets.shared;

import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.logic.PacketInterceptor;
import fr.radi3nt.multi.packets.logic.PacketProtocol;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NetworkManager {

    private final Queue<PacketOut> queuedPackets = new ConcurrentLinkedQueue<>();
    private final List<QueueOptimizer> queueOptimizers = new ArrayList<>();
    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private final ConnectionManager connectionManager;
    private final PacketProtocol packetProtocol;

    public NetworkManager(ConnectionManager connectionManager, PacketProtocol packetProtocol) {
        this.connectionManager = connectionManager;
        this.packetProtocol = packetProtocol;
    }

    public void sendPacket(PacketOut packet) {

        /*
        if (isConnected()) {
            this.sendQueuedLeft();
            this.actuallySendPacket(packet);
        } else {
            reentrantReadWriteLock.writeLock().lock();
            this.queuedPackets.add(new QueuedPacket(packet));
            reentrantReadWriteLock.writeLock().unlock();
        }
        */

        reentrantReadWriteLock.writeLock().lock();
        for (QueueOptimizer queueOptimizer : queueOptimizers) {
            queueOptimizer.packetAdded(packet, queuedPackets);
        }
        this.queuedPackets.add(packet);
        reentrantReadWriteLock.writeLock().unlock();

    }

    public void sendPackets() {
        sendQueuedLeft();
    }

    private void actuallySendPacket(PacketOut packet) {
        packetProtocol.sendPacket(packet);
    }

    private void sendQueuedLeft() {
        reentrantReadWriteLock.readLock().lock();

        while(!this.queuedPackets.isEmpty()) {
            PacketOut queuedPacket = this.queuedPackets.poll();
            if (connectionManager.isConnected()) {
                actuallySendPacket(queuedPacket);
            }
        }

        reentrantReadWriteLock.readLock().unlock();
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
