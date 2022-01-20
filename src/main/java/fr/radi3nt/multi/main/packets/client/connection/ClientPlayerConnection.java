package fr.radi3nt.multi.main.packets.client.connection;

import fr.radi3nt.multi.main.packets.client.packets.PacketInServerStop;
import fr.radi3nt.multi.main.packets.client.packets.alive.PacketInServerAskKeepAlive;
import fr.radi3nt.multi.main.packets.client.packets.alive.PacketOutResponseKeepAlive;
import fr.radi3nt.multi.main.packets.shared.NetworkManager;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.logic.PacketInterceptor;
import fr.radi3nt.multi.sockets.shared.distant.connection.connections.ClientConnection;

public class ClientPlayerConnection {

    private final ClientConnection connection;
    private final NetworkManager networkManager;

    public ClientPlayerConnection(ClientConnection connection, NetworkManager networkManager) {
        this.connection = connection;
        this.networkManager = networkManager;

        networkManager.addInterceptor(new PacketInterceptor() {
            @Override
            public PacketIn onPacketIn(PacketIn packet) {

                if (packet instanceof PacketInServerAskKeepAlive) {
                    networkManager.sendPacket(new PacketOutResponseKeepAlive());
                }
                if (packet instanceof PacketInServerStop) {
                    connection.getConnectionManager().close();
                    System.out.println("Client: server stopped with reason: " + ((PacketInServerStop) packet).getReason());
                }

                return packet;
            }

            @Override
            public PacketOut onPacketOut(PacketOut packet) {
                return packet;
            }
        });
    }


    public void sendPacket(PacketOut packet) {
        networkManager.sendPacket(packet);
    }

    public void addInterceptor(PacketInterceptor packetInterceptor) {
        networkManager.addInterceptor(packetInterceptor);
    }
}
