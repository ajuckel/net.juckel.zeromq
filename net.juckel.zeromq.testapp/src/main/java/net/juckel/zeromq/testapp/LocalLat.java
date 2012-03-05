package net.juckel.zeromq.testapp;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.zeromq.ZMQ;

public class LocalLat implements IApplication {
    public Object start(IApplicationContext context) throws Exception {
        final String[] args = (String[]) context.getArguments().get(
                IApplicationContext.APPLICATION_ARGS);
        if (args.length != 3) {
            System.out.println("usage: local_lat <bind-to> "
                    + "<message-size> <roundtrip-count>");
            return Integer.valueOf(-1);
        }

        String bindTo = args[0];
        int messageSize = Integer.parseInt(args[1]);
        int roundtripCount = Integer.parseInt(args[2]);

        ZMQ.Context ctx = ZMQ.context(1);
        ZMQ.Socket s = ctx.socket(ZMQ.REP);

        // Add your socket options here.
        // For example ZMQ_RATE, ZMQ_RECOVERY_IVL and ZMQ_MCAST_LOOP for PGM.

        s.bind(bindTo);

        for (int i = 0; i != roundtripCount; i++) {
            byte[] data = s.recv(0);
            assert (data.length == messageSize);
            s.send(data, 0);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Integer.valueOf(0);
    }

    public void stop() {
    }
}
