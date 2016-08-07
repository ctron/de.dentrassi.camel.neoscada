package de.dentrassi.camel.neoscada.server;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ServerTest1 {
	public static void main(final String[] args) throws Exception {
		final CamelContext context = new DefaultCamelContext();

		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("neoscada-server:MyItem").log("MyItem: ${body}");
				from("timer:foo").setBody(simple("${random(0,100)}")).to("neoscada-server:MyItem");

			}
		});

		// start

		context.start();

		// sleep

		while (true) {
			Thread.sleep(Long.MAX_VALUE);
		}
	}
}
