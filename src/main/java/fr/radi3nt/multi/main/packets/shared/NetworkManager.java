package fr.radi3nt.multi.main.packets.shared;

import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.logic.PacketInterceptor;
import fr.radi3nt.multi.packets.logic.PacketProtocol;
import fr.radi3nt.multi.sockets.shared.distant.managing.managers.shared.ConnectionManager;
import fr.radi3nt.timing.TimingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkManager {

    private final Queue<PacketOut> queuedPackets = new ConcurrentLinkedQueue<>();
    private final List<QueueOptimizer> queueOptimizers = new ArrayList<>();
    private final ConnectionManager connectionManager;
    private final PacketProtocol packetProtocol;

    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final AtomicBoolean finishedSending = new AtomicBoolean(false);

    public NetworkManager(ConnectionManager connectionManager, PacketProtocol packetProtocol) {
        this.connectionManager = connectionManager;
        this.packetProtocol = packetProtocol;
    }

    public void sendPacket(PacketOut packet) {
        for (QueueOptimizer queueOptimizer : queueOptimizers) {
                queueOptimizer.packetAdded(packet, queuedPackets);
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

        boolean currentClosed = closed.get();

        if (finishedSending.get())
            return;

        while(!this.queuedPackets.isEmpty()) {
            PacketOut queuedPacket = this.queuedPackets.poll();
            if (connectionManager.isConnected()) {
                actuallySendPacket(queuedPacket);
            }
        }

        if (currentClosed) {
            finishedSending.set(true);
        }

    }

    public void addInterceptor(PacketInterceptor packetInterceptor) {
        packetProtocol.addInterceptor(packetInterceptor);
    }

    public void addQueueOptimizer(QueueOptimizer queueOptimizer) {
        queueOptimizers.add(queueOptimizer);
    }

    public void closeAndWaitLeftPackets() {
        closed.set(true);
        while (!finishedSending.get() && connectionManager.isConnected()) {
            TimingUtil.waitMillis(1);
        }
    }

    public interface QueueOptimizer {

        void packetAdded(PacketOut packet, Queue<PacketOut> packets);

    }
}
