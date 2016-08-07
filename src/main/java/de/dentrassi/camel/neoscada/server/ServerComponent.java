package de.dentrassi.camel.neoscada.server;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;
import org.eclipse.scada.core.ConnectionInformation;
import org.eclipse.scada.da.common.ngp.ProtocolConfigurationFactoryImpl;
import org.eclipse.scada.protocol.ngp.common.ProtocolConfigurationFactory;

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;

public class ServerComponent extends UriEndpointComponent {

	private int port = 2101;

	private CamelHive hive;
	private HiveRunner runner;

	public ServerComponent() {
		super(ServerEndpoint.class);
	}

	public ServerComponent(final CamelContext context) {
		super(context, ServerEndpoint.class);
	}

	@Override
	protected Endpoint createEndpoint(final String uri, final String remaining, final Map<String, Object> parameters)
			throws Exception {
		return new ServerEndpoint(uri, this, this.hive, remaining);
	}

	@Override
	protected void doStart() throws Exception {
		super.doStart();

		this.hive = new CamelHive();
		this.runner = new HiveRunner(this.hive, makeProtocol(), makeAddresses());
	}

	private ProtocolConfigurationFactory makeProtocol() {
		return new ProtocolConfigurationFactoryImpl(ConnectionInformation.fromURI(makeUri()));
	}

	private final Escaper esc = UrlEscapers.urlFormParameterEscaper();

	private String makeUri() {
		final Map<String, String> parameters = new HashMap<>();

		final StringBuilder sb = new StringBuilder();
		for (final Map.Entry<String, String> entry : parameters.entrySet()) {
			if (sb.length() == 0) {
				sb.append("?");
			} else {
				sb.append("&");
			}

			sb.append(this.esc.escape(entry.getKey()));
			sb.append("=");
			sb.append(this.esc.escape(entry.getValue()));
		}

		return String.format("da:ngp://%s:%s%s", "dummy", this.port, sb);
	}

	private Collection<InetSocketAddress> makeAddresses() {
		return Collections.singletonList(new InetSocketAddress(this.port));
	}

	@Override
	protected void doStop() throws Exception {
		if (this.runner != null) {
			this.runner.close();
			this.runner = null;
		}
		super.doStop();
	}

	/**
	 * Set the NGP exporter port
	 */
	public void setPort(final int port) {
		this.port = port;
	}

	public int getPort() {
		return this.port;
	}
}
