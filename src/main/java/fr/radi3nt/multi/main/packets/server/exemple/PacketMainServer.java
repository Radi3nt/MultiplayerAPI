package fr.radi3nt.multi.main.packets.server.exemple;

import fr.radi3nt.multi.main.packets.client.exemple.PacketMainClient;
import fr.radi3nt.multi.main.packets.server.connection.ServerPlayerConnection;
import fr.radi3nt.multi.main.packets.server.exemple.packets.PacketInClientDisconnect;
import fr.radi3nt.multi.main.packets.server.exemple.packets.PacketOutServerDisconnect;
import fr.radi3nt.multi.main.packets.server.exemple.packets.alive.PacketInResponseKeepAlive;
import fr.radi3nt.multi.main.packets.server.exemple.packets.alive.PacketOutServerAskKeepAlive;
import fr.radi3nt.multi.main.packets.shared.ByteBufferPacketDataBuffer;
import fr.radi3nt.multi.main.packets.shared.ContextPacketIndexer;
import fr.radi3nt.multi.main.packets.shared.NetworkManager;
import fr.radi3nt.multi.main.packets.shared.SocketPacketProtocol;
import fr.radi3nt.multi.main.packets.shared.connection.SocketConnectionListener;
import fr.radi3nt.multi.packets.data.types.PacketIn;
import fr.radi3nt.multi.packets.data.types.PacketOut;
import fr.radi3nt.multi.packets.logic.PacketInterceptor;
import fr.radi3nt.multi.packets.module.list.SocketPacketPropertyList;
import fr.radi3nt.multi.packets.process.CipherEncryptionHandler;
import fr.radi3nt.multi.packets.process.recieving.PacketDecompressor;
import fr.radi3nt.multi.packets.process.recieving.PacketDecrypter;
import fr.radi3nt.multi.packets.process.sending.PacketCompressor;
import fr.radi3nt.multi.packets.process.sending.PacketEncryptor;
import fr.radi3nt.multi.sockets.shared.distant.connection.connections.Connection;
import fr.radi3nt.multi.sockets.shared.distant.connection.handle.ConnectionHandler;

import javax.crypto.NullCipher;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.Inflater;

public class PacketMainServer {

    public static void main(String[] args) {

        AtomicInteger id = new AtomicInteger(0);
        Scanner sc = new Scanner(System.in);

        ContextPacketIndexer contextPacketIndexer = new ContextPacketIndexer();
        contextPacketIndexer.addInPacketType(PacketInResponseKeepAlive.PACKET_TYPE, PacketInResponseKeepAlive::new);
        contextPacketIndexer.addInPacketType(PacketInClientDisconnect.PACKET_TYPE, PacketInClientDisconnect::new);
        //contextPacketIndexer.addPacketType(PacketOutServerAskKeepAlive.PACKET_TYPE, PacketOutServerAskKeepAlive::new);

        AtomicReference<SocketConnectionListener> socketConnectionListener = new AtomicReference<>();
        socketConnectionListener.set(new SocketConnectionListener(PacketMainClient.PORT, new ConnectionHandler() {
            @Override
            public void onConnection(Connection connection) {
                int currentId = id.getAndIncrement();

                CipherEncryptionHandler cipherEncryptionHandler = new CipherEncryptionHandler(new NullCipher(), new NullCipher());
                ServerPlayerConnection serverPlayerConnection = new ServerPlayerConnection(new NetworkManager(connection.getConnectionManager(), new SocketPacketProtocol(connection.getCommunicationManager(), connection.getListenerManager(), contextPacketIndexer, new PacketEncryptor(cipherEncryptionHandler), new PacketDecrypter(cipherEncryptionHandler), new PacketCompressor(), new PacketDecompressor(new Inflater()), new SocketPacketPropertyList(size -> new ByteBufferPacketDataBuffer(ByteBuffer.allocate(size)), contextPacketIndexer))));

                System.out.println("Server: accepted client " + currentId);

                serverPlayerConnection.addInterceptor(new PacketInterceptor() {
                    @Override
                    public PacketIn onPacketIn(PacketIn packet) {

                        if (packet instanceof PacketInResponseKeepAlive) {
                            System.out.println("Client is still there");
                        }
                        if (packet instanceof PacketInClientDisconnect) {
                            connection.getConnectionManager().close();
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
                        while (connection.getConnectionManager().isConnected()) {
                            connection.getListenerManager().update();
                        }

                        System.out.println("terminated client " + currentId + " packet listening");
                    }
                });
                thread.start();

                while (connection.getConnectionManager().isConnected()) {
                    if (sc.hasNext()) {
                        if (!connection.getConnectionManager().isConnected())
                            return;
                        String command = sc.nextLine();
                            switch (command) {
                                case "send": {
                                    serverPlayerConnection.sendPacket(new PacketOutServerAskKeepAlive());
                                    break;
                                }
                                case "disconnect": {
                                    serverPlayerConnection.sendPacket(new PacketOutServerDisconnect("Server kicked you"));
                                    connection.getConnectionManager().close();
                                    return;
                                }
                                case "stop": {
                                    socketConnectionListener.get().delete();
                                    break;
                                }
                            }
                        }
                }
            }
        }));

        socketConnectionListener.get().open();

        Thread thread = new Thread(() -> {
            while (socketConnectionListener.get().isOpened()) {
                socketConnectionListener.get().update();
            }
            System.out.println("terminated connection listener");
        });
        thread.start();



    }

}
