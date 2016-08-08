/*
 * Copyright (C) 2016 Jens Reimann <jreimann@redhat.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dentrassi.camel.neoscada.server;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.eclipse.scada.core.Variant;
import org.eclipse.scada.core.server.OperationParameters;
import org.eclipse.scada.da.core.WriteResult;
import org.eclipse.scada.da.server.common.chain.DataItemInputOutputChained;
import org.eclipse.scada.utils.concurrent.InstantErrorFuture;
import org.eclipse.scada.utils.concurrent.NotifyFuture;

public class ServerEndpoint extends DefaultEndpoint {

	private final CamelHive hive;
	private final DataItemInputOutputChained item;

	public ServerEndpoint(final String endpointUri, final ServerComponent component, final CamelHive hive,
			final String itemId) {
		super(endpointUri, component);
		this.hive = hive;
		this.item = new DataItemInputOutputChained(itemId, hive.getOperationService()) {

			@Override
			protected NotifyFuture<WriteResult> startWriteCalculatedValue(final Variant value,
					final OperationParameters operationParameters) {
				return handleCommand(value, operationParameters);
			}
		};
		this.hive.registerItem(this.item);
	}

	protected NotifyFuture<WriteResult> handleCommand(final Variant value,
			final OperationParameters operationParameters) {

		return new InstantErrorFuture<>(new UnsupportedOperationException("Write not supported"));

	}

	@Override
	public Producer createProducer() throws Exception {
		return new ServerProducer(this, this.item);
	}

	@Override
	public Consumer createConsumer(final Processor processor) throws Exception {
		return null;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
