package fr.radi3nt.multi.sockets.shared.distant.connection.address;

public class Address {

    private final String hostName;
    private final int port;

    public Address(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

}
