package net.juckel.zeromq.testapp;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.zeromq.ZMQ;

public class Subscriber implements IApplication {

	public Object start(IApplicationContext context) throws Exception {
		final String[] args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		if (args.length != 2) {
			System.out.println("usage: subscriber <connect-to> <key>");
			return Integer.valueOf(-1);
		}

        final Charset utf8 = Charset.forName("UTF8");
		String connectTo = args[0];
		ZMQ.Context ctx = ZMQ.context(1);
		ZMQ.Socket sub = ctx.socket(ZMQ.SUB);
		sub.connect(connectTo);

		String filter = args[1];
		sub.subscribe(filter.getBytes(utf8));

		double total = 0.0d;
		while (true) {
			String msg = new String(sub.recv(0), utf8).trim();
			byte[] payload = sub.recv(0);
			total += ByteBuffer.wrap(payload).getDouble();
            System.out.println("Message received: " + msg + "; Running total: " + total);

		}
	}

	public void stop() {
		// TODO Auto-generated method stub

	}

}
