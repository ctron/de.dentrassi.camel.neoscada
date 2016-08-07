package de.dentrassi.camel.neoscada.server;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.scada.da.core.server.Hive;
import org.eclipse.scada.da.server.ngp.Exporter;
import org.eclipse.scada.protocol.ngp.common.ProtocolConfigurationFactory;

public class HiveRunner implements AutoCloseable {
	private final Hive hive;
	private final Exporter exporter;

	public HiveRunner(final Hive hive, final ProtocolConfigurationFactory protocolConfigurationFactory,
			final Collection<InetSocketAddress> addresses) throws Exception {

		this.hive = hive;
		this.hive.start();

		this.exporter = new Exporter(hive, protocolConfigurationFactory, addresses);
		try {
			this.exporter.start();
		} catch (final Exception e) {
			hive.stop();
			throw e;
		}
	}

	@Override
	public void close() throws Exception {
		final LinkedList<Exception> errors = new LinkedList<>();

		if (this.hive != null) {
			try {
				this.hive.stop();
			} catch (final Exception e) {
				errors.add(e);
			}
		}
		if (this.exporter != null) {
			try {
				this.exporter.stop();
			} catch (final Exception e) {
				errors.add(e);
			}
		}

		if (!errors.isEmpty()) {
			final Exception e = errors.pollFirst();
			errors.stream().forEach(e::addSuppressed);
			throw e;
		}
	}
}
