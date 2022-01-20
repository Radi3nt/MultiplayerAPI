package fr.radi3nt.multi.main.packets.server.connection;

import fr.radi3nt.multi.main.packets.shared.NetworkManager;
import fr.radi3nt.multi.packets.data.Packet;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.logic.PacketInterceptor;

public class ServerPlayerConnection {

    private final NetworkManager networkManager;

    public ServerPlayerConnection(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }


    public void sendPacket(PacketOut packet) {
        networkManager.sendPacket(packet);
    }

    public void addInterceptor(PacketInterceptor packetInterceptor) {
        networkManager.addInterceptor(packetInterceptor);
    }
}
