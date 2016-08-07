package de.dentrassi.camel.neoscada.server;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.eclipse.scada.core.Variant;
import org.eclipse.scada.da.server.common.chain.DataItemInputOutputChained;

public class ServerProducer extends DefaultProducer {

	private final DataItemInputOutputChained item;

	public ServerProducer(final ServerEndpoint endpoint, final DataItemInputOutputChained item) {
		super(endpoint);
		this.item = item;
	}

	@Override
	public void process(final Exchange exchange) throws Exception {
		final Object value = exchange.getIn().getBody();
		this.item.updateData(Variant.valueOf(value), null, null);
	}

}
