package fr.radi3nt.multi.sockets.shared.util;

import fr.radi3nt.multi.sockets.shared.distant.request.ResponseRequest;

public class Timings {

    public static void waitForRequest(ResponseRequest request) {
        while (!request.hasResponse()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
