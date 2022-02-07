package fr.radi3nt.multi.main.packets.client;

import fr.radi3nt.multi.main.packets.client.connection.ClientPlayerConnection;
import fr.radi3nt.multi.main.packets.client.packets.PacketInServerStop;
import fr.radi3nt.multi.main.packets.client.packets.PacketOutClientDisconnect;
import fr.radi3nt.multi.main.packets.client.packets.alive.PacketInServerAskKeepAlive;
import fr.radi3nt.multi.main.packets.shared.ByteBufferPacketDataBuffer;
import fr.radi3nt.multi.main.packets.shared.ContextPacketIndexer;
import fr.radi3nt.multi.main.packets.shared.NetworkManager;
import fr.radi3nt.multi.main.packets.shared.SocketPacketProtocol;
import fr.radi3nt.multi.packets.data.serializer.PacketDataBuffer;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.logic.PacketInterceptor;
import fr.radi3nt.multi.packets.module.PacketDataBufferProvider;
import fr.radi3nt.multi.packets.module.list.SocketPacketPropertyList;
import fr.radi3nt.multi.packets.process.CipherEncryptionHandler;
import fr.radi3nt.multi.packets.process.recieving.PacketDecompressor;
import fr.radi3nt.multi.packets.process.recieving.PacketDecrypter;
import fr.radi3nt.multi.packets.process.sending.PacketCompressor;
import fr.radi3nt.multi.packets.process.sending.PacketEncryptor;
import fr.radi3nt.multi.sockets.client.socket.SocketClientConnection;
import fr.radi3nt.multi.sockets.shared.distant.connection.address.Address;
import fr.radi3nt.multi.sockets.shared.distant.connection.connections.ClientConnection;

import javax.crypto.NullCipher;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.zip.Inflater;

public class PacketMainClient {

    public static final int PORT = 22567;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ContextPacketIndexer contextPacketIndexer = new ContextPacketIndexer();
        contextPacketIndexer.addPacketType(PacketInServerAskKeepAlive.PACKET_TYPE, PacketInServerAskKeepAlive::new);
        contextPacketIndexer.addPacketType(PacketInServerStop.PACKET_TYPE, PacketInServerStop::new);
        //contextPacketIndexer.addPacketType(PacketOutResponseKeepAlive.PACKET_TYPE);

        ClientConnection socketClientConnection = new SocketClientConnection(new Address("localhost", PORT));
        CipherEncryptionHandler cipherEncryptionHandler = new CipherEncryptionHandler(new NullCipher(), new NullCipher());
        ClientPlayerConnection connection = new ClientPlayerConnection(socketClientConnection, new NetworkManager(socketClientConnection.getConnectionManager(), new SocketPacketProtocol(socketClientConnection.getCommunicationManager(), socketClientConnection.getListenerManager(), contextPacketIndexer, new PacketEncryptor(cipherEncryptionHandler), new PacketDecrypter(cipherEncryptionHandler), new PacketCompressor(), new PacketDecompressor(new Inflater()), new SocketPacketPropertyList(new PacketDataBufferProvider() {
            @Override
            public PacketDataBuffer createBuffer(int size) {
                return new ByteBufferPacketDataBuffer(ByteBuffer.allocate(size));
            }
        }, contextPacketIndexer))));
        socketClientConnection.getConnectionManager().connect();

        connection.addInterceptor(new PacketInterceptor() {
            @Override
            public PacketIn onPacketIn(PacketIn packet) {
                if (packet instanceof PacketInServerAskKeepAlive) {
                    System.out.println("Client: Received response from server");
                }
                return packet;
            }

            @Override
            public PacketOut onPacketOut(PacketOut packet) {
                return packet;
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (socketClientConnection.getConnectionManager().isConnected()) {
                    socketClientConnection.getListenerManager().update();
                }

                System.exit(0);
            }
        });

        thread.start();
        while (socketClientConnection.getConnectionManager().isConnected()) {
            if (scanner.hasNext()) {
                String command = scanner.nextLine();
                     switch (command) {
                        case "disconnect": {
                            connection.sendPacket(new PacketOutClientDisconnect());
                            socketClientConnection.getConnectionManager().close();
                            break;
                        }
                        case "send": {
                            break;
                        }
                    }
                }
        }


    }

}
