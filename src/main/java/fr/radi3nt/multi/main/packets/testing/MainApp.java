package fr.radi3nt.multi.main.packets.testing;

import fr.radi3nt.multi.main.packets.client.PacketMainClient;
import fr.radi3nt.multi.main.packets.server.PacketMainServer;

public class MainApp {

    public static void main(String[] args) {
        System.out.println("App: Launched");
        Thread server = new Thread(() -> PacketMainServer.main(args));
        server.start();

        Thread client = new Thread(() -> PacketMainClient.main(args));
        client.start();

    }

}
