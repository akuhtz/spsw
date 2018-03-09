/*-
 * #%L
 * SPSW Provider
 * %%
 * Copyright (C) 2009 - 2017 Arne Plöse
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */
package de.ibapl.spsw.tests;

import java.util.Iterator;

import de.ibapl.spsw.api.Baudrate;

/**
 * Unit test for simple App. Timeout is computed 8 data bits + 2 stop bits +
 * parity bit + start bit == 12
 */
public abstract class AbstractReadWriteAllBaudratesTest extends AbstractReadWriteTest {

	@Override
	public Iterator<PortConfiguration> getTestPortConfigurations() {
		return new PortConfigurationFactory().setBuffersize(64).getParityIterator();
	}

}