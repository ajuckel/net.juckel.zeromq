package net.juckel.zeromq.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.loadLibrary("zmq");
		System.loadLibrary("jzmq");
	}

	public void stop(BundleContext context) throws Exception {
	}
}
