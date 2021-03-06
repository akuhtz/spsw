/*
 * SPSW - Drivers for the serial port, https://github.com/aploese/spsw/
 * Copyright (C) 2009-2019, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package de.ibapl.spsw.tests;

import java.util.Iterator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import de.ibapl.spsw.api.Speed;
import de.ibapl.spsw.tests.tags.BaselineTest;
import de.ibapl.spsw.tests.tags.ByteChannelTest;
import de.ibapl.spsw.tests.tags.IOStreamTest;

/**
 * @author Arne Plöse
 */
public abstract class AbstractReadWriteBufferSizeTest extends AbstractReadWriteTest {

	public Iterator<PortConfiguration> getBaselinePortConfigurations() {
		return new Iterator<PortConfiguration>() {
			final PortConfigurationFactory pcf = new PortConfigurationFactory().setSpeed(Speed._230400_BPS);

			int currentSize = 1;
			final int maxSize = 1024 * 4; // 4kB seems the size of the inputbuffer for some devices FTDI 232 on
											// Windows(8k FTDI 232 on linux) whereas others can handle 16k...

			@Override
			public boolean hasNext() {
				return currentSize <= maxSize;
			}

			@Override
			public PortConfiguration next() {
				int result = currentSize;
				if (currentSize == 0) {
					currentSize = 1;
				} else {
					currentSize *= 2;
				}
				return pcf.ofBuffersize(result);
			}
		};
	}

	@IOStreamTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_WriteBytes_ReadBytes(PortConfiguration pc) throws Exception {
		write_Read_nonThreaded(SocketIoType.STREAM, SocketIoType.STREAM, pc);
	}

	@IOStreamTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_WriteBytes_ReadSingle(PortConfiguration pc) throws Exception {
		write_Read_nonThreaded(SocketIoType.STREAM, SocketIoType.SINGLE_BYTE, pc);
	}

	@IOStreamTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_WriteSingle_ReadBytes(PortConfiguration pc) throws Exception {
		write_Read_nonThreaded(SocketIoType.SINGLE_BYTE, SocketIoType.STREAM, pc);
	}

	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_WriteSingle_ReadSingle(PortConfiguration pc) throws Exception {
		write_Read_nonThreaded(SocketIoType.SINGLE_BYTE, SocketIoType.SINGLE_BYTE, pc);
	}

	@IOStreamTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_WriteBytes_ReadBytes_Threaded(PortConfiguration pc) throws Exception {
		write_Read_Threaded(SocketIoType.STREAM, SocketIoType.STREAM, pc);
	}

	@IOStreamTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_WriteBytes_ReadSingle_Threaded(PortConfiguration pc) throws Exception {
		write_Read_Threaded(SocketIoType.STREAM, SocketIoType.SINGLE_BYTE, pc);
	}

	@IOStreamTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_WriteSingle_ReadBytes_Threaded(PortConfiguration pc) throws Exception {
		write_Read_Threaded(SocketIoType.SINGLE_BYTE, SocketIoType.STREAM, pc);
	}

	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_WriteSingle_ReadSingle_Threaded(PortConfiguration pc) throws Exception {
		write_Read_Threaded(SocketIoType.SINGLE_BYTE, SocketIoType.SINGLE_BYTE, pc);
	}

	@ByteChannelTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_Channel_WriteBytes_ReadBytes(PortConfiguration pc) throws Exception {
		write_Read_nonThreaded(SocketIoType.CHANNEL, SocketIoType.CHANNEL, pc);
	}

	@ByteChannelTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_Channel_WriteBytes_ReadSingle(PortConfiguration pc) throws Exception {
		write_Read_nonThreaded(SocketIoType.CHANNEL, SocketIoType.SINGLE_BYTE, pc);
	}

	@ByteChannelTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_Channel_WriteSingle_ReadBytes(PortConfiguration pc) throws Exception {
		write_Read_nonThreaded(SocketIoType.SINGLE_BYTE, SocketIoType.CHANNEL, pc);
	}

	@ByteChannelTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_Channel_WriteBytes_ReadBytes_Threaded(PortConfiguration pc) throws Exception {
		write_Read_Threaded(SocketIoType.CHANNEL, SocketIoType.CHANNEL, pc);
	}

	@ByteChannelTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_Channel_WriteBytes_ReadSingle_Threaded(PortConfiguration pc) throws Exception {
		write_Read_Threaded(SocketIoType.CHANNEL, SocketIoType.SINGLE_BYTE, pc);
	}

	@ByteChannelTest
	@BaselineTest
	@ParameterizedTest
	@MethodSource({ "getBaselinePortConfigurations" })
	public void test_Channel_WriteSingle_ReadBytes_Threaded(PortConfiguration pc) throws Exception {
		write_Read_Threaded(SocketIoType.SINGLE_BYTE, SocketIoType.CHANNEL, pc);
	}

}