package net.juckel.zeromq.testapp;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.zeromq.ZMQ;

public class RemoteLat implements IApplication {

    public Object start(IApplicationContext context) throws Exception {
        final String[] args = (String[]) context.getArguments().get(
                IApplicationContext.APPLICATION_ARGS);
        if (args.length != 3) {
            System.out.println("usage: remote_lat <connect-to> "
                    + "<message size> <roundtrip count>");
            return null;
        }

        String connectTo = args[0];
        int messageSize = Integer.parseInt(args[1]);
        int roundtripCount = Integer.parseInt(args[2]);

        ZMQ.Context ctx = ZMQ.context(1);
        ZMQ.Socket s = ctx.socket(ZMQ.REQ);

        // Add your socket options here.
        // For example ZMQ_RATE, ZMQ_RECOVERY_IVL and ZMQ_MCAST_LOOP for PGM.

        s.connect(connectTo);

        long start = System.currentTimeMillis();

        byte data[] = new byte[messageSize];
        for (int i = 0; i != roundtripCount; i++) {
            s.send(data, 0);
            data = s.recv(0);
            assert (data.length == messageSize);
        }

        long end = System.currentTimeMillis();

        long elapsed = (end - start) * 1000;
        double latency = (double) elapsed / roundtripCount / 2;

        System.out.println("message size: " + messageSize + " [B]");
        System.out.println("roundtrip count: " + roundtripCount);
        System.out.println("mean latency: " + latency + " [us]");
        return null;
    }

    public void stop() {
    }
}
