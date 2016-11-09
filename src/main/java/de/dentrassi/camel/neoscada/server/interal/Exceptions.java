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
package de.dentrassi.camel.neoscada.server.interal;

import java.util.LinkedList;

public final class Exceptions {
	private Exceptions() {
	}

	public static void handleErrors(final LinkedList<Exception> errors) throws Exception {
		if (errors != null && !errors.isEmpty()) {
			final Exception e = errors.pollFirst();
			for (final Exception se : errors) {
				e.addSuppressed(se);
			}
			throw e;
		}
	}

	public static void handleRuntimeErrors(final LinkedList<Exception> errors) {
		if (errors != null && !errors.isEmpty()) {
			final Exception first = errors.pollFirst();
			final RuntimeException e;
			if (first instanceof RuntimeException) {
				e = (RuntimeException) first;
			} else {
				e = new RuntimeException(first);
			}
			for (final Exception se : errors) {
				e.addSuppressed(se);
			}
			throw e;
		}
	}
}
