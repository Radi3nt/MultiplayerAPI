package fr.radi3nt.multi.sockets.shared.distant.request.impl;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SocketRequestIdCreator {

    private static final long MAX_VALUE = Long.MAX_VALUE;
    private static final AtomicLong atomicLong = new AtomicLong(-Long.MAX_VALUE);
    private static final Set<Long> usedIds = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static long generateId() {
        long id = atomicLong.getAndIncrement();
        while (usedIds.contains(id))
            id = atomicLong.getAndIncrement();

        usedIds.add(id);

        if (atomicLong.get()>=MAX_VALUE)
            atomicLong.set(-Long.MAX_VALUE);

        return id;
    }

    public static void releaseId(long id) {
        usedIds.remove(id);
    }

}
