package fr.radi3nt.multi.main.packets.client.connection;

import fr.radi3nt.multi.main.packets.shared.NetworkManager;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.logic.PacketInterceptor;
import fr.radi3nt.multi.sockets.shared.distant.connection.connections.ClientConnection;

public class ClientPlayerConnection {

    private final ClientConnection connection;
    private final NetworkManager networkManager;

    public ClientPlayerConnection(ClientConnection connection, NetworkManager networkManager) {
        this.connection = connection;
        this.networkManager = networkManager;
    }


    public void sendPacket(PacketOut packet) {
        networkManager.sendPacket(packet);
    }

    public void addInterceptor(PacketInterceptor packetInterceptor) {
        networkManager.addInterceptor(packetInterceptor);
    }
}
