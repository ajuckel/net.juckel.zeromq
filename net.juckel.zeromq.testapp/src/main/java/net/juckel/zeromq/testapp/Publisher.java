package net.juckel.zeromq.testapp;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.zeromq.ZMQ;

public class Publisher implements IApplication {
    public Object start(IApplicationContext context) throws Exception {
        final String[] args = (String[]) context.getArguments().get(
                IApplicationContext.APPLICATION_ARGS);
        if (args.length != 2) {
            System.out.println("usage: publisher <bind-to> <msg-count>");
            return Integer.valueOf(-1);
        }

        final Charset utf8 = Charset.forName("UTF-8");
        String bindTo = args[0];
        ZMQ.Context ctx = ZMQ.context(1);
        ZMQ.Socket pub = ctx.socket(ZMQ.PUB);
        pub.bind(bindTo);

        final String[] topics = { "FOO", "BAR", "BAZ" };
        for (int i = 0; i < Integer.parseInt(args[1]); i++) {
            pub.send(topics[i % 3].getBytes(utf8), ZMQ.SNDMORE);
            byte[] message = new byte[8];
            ByteBuffer.wrap(message).putDouble(15d);
            pub.send(message, 0);
            Thread.sleep(10);
        }
        return Integer.valueOf(0);
    }

    public void stop() {
    }
}
