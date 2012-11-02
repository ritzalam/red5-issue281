package org.red5.app.issue281;

import java.util.List;
import java.util.concurrent.atomic.*;
import org.red5.server.adapter.*;
import org.red5.server.api.IConnection;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.service.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnMultiThreadApplication extends MultiThreadedApplicationAdapter {

	private static final Logger log = LoggerFactory.getLogger(ConnMultiThreadApplication.class);

	private IScope appScope;

	private AtomicInteger counter = new AtomicInteger();
	private IConnection conn;
	
	/** {@inheritDoc} */
	@Override
	public boolean connect(IConnection conn, IScope scope, Object[] params) {
		appScope = scope;
		createSharedObject(appScope, "message", false);
		this.conn = conn;
		return super.connect(conn, scope, params);
	}
	
	/** {@inheritDoc} */
	@Override
	public void disconnect(IConnection conn, IScope scope) {
		super.disconnect(conn, scope);
		log.info("Message counter: {}", counter.get());
		//System.exit(9999);
	}
	
	public void sendMessage(List<String> params) {
		// increment our local receive counter
		counter.getAndIncrement();		
		ServiceUtils.invokeOnConnection(conn, "receiveMessage", params.toArray());
	}
}

