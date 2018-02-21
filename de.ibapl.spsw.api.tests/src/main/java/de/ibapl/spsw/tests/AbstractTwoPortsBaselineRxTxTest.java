package de.ibapl.spsw.tests;

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
import gnu.io.CommPortIdentifier;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InterruptedIOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.ibapl.spsw.api.SerialPortSocket;

/**
 * Unit test for simple App. Timeout is computed 8 data bits + 2 stop bits +
 * parity bit + start bit == 12
 * This is only for regression tests
 */
public abstract class AbstractTwoPortsBaselineRxTxTest {

	protected static final int PORT_RECOVERY_TIME = AbstractPortTest.PORT_RECOVERY_TIME;
	protected static final boolean HARDWARE_SUPPORTS_RTS_CTS = AbstractPortTest.HARDWARE_SUPPORTS_RTS_CTS;
	

    private static final Logger LOG = Logger.getLogger(AbstractTwoPortsBaselineRxTxTest.class.getName());
    private final static int DEFAULT_TEST_BUFFER_SIZE = 128;

    private byte[] dataOut;
    private byte[] dataIn;
	private static String readSerialPortName;
	private static String writeSerialPortName;

	protected RXTXPort readSpc;
	protected RXTXPort writeSpc;

	protected int flowControl =  HARDWARE_SUPPORTS_RTS_CTS ? (SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT) : SerialPort.FLOWCONTROL_NONE; 
    protected int parity = SerialPort.PARITY_NONE;
    protected int stopBits = SerialPort.STOPBITS_1;
    protected int dataBits = SerialPort.DATABITS_8;

	@BeforeClass
	public static void setUpClass() throws Exception {
		try (InputStream is = AbstractTwoPortMultipleBytesTest.class.getClassLoader()
				.getResourceAsStream("junit-spsw-config.properties")) {
			if (is == null) {
				readSerialPortName = null;
				writeSerialPortName = null;
			} else {
				Properties p = new Properties();
				p.load(is);
				readSerialPortName = p.getProperty("readPort", null);
				writeSerialPortName = p.getProperty("writePort", null);
			}
		}
	}

	@Before
	public void setUp() throws Exception {
		if (readSerialPortName != null) {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(readSerialPortName);
            readSpc = (RXTXPort) portIdentifier.open(this.getClass().getName(), 1000);
		}
		if (writeSerialPortName != null) {
			if (writeSerialPortName.equals(readSerialPortName)) {
				writeSpc = readSpc;
			} else {
                CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(writeSerialPortName);
                writeSpc = (RXTXPort) portIdentifier.open(this.getClass().getName(), 1000);
			}
		}
	}

	@After
	public void tearDown() throws Exception {
		if (writeSpc != null) {
			if (writeSpc != readSpc) {
					writeSpc.close();
			}
			writeSpc = null;
		}
		if (readSpc != null) {
				readSpc.close();
			readSpc = null;
		}

		Runtime.getRuntime().gc();
		Runtime.getRuntime().runFinalization();
		if (PORT_RECOVERY_TIME > 0) {
			Thread.sleep(PORT_RECOVERY_TIME * 1000);
		}
	}

    private void initBuffers(final int size) {
        dataOut = new byte[size];
        for (int i = 0; i < size; i++) {
            dataOut[i] = (byte) (Math.round(Math.random() * 0xFF) & 0xFF);
        }
        dataIn = new byte[size];
    }

    private void writeBytes() {
        try {
            writeSpc.getOutputStream().write(dataOut);
            LOG.fine("data written");
        } catch (InterruptedIOException iioe) {
            LOG.log(Level.SEVERE, "{0} Bytes send total: {1}", new Object[]{iioe.getMessage(), iioe.bytesTransferred});
            Assert.fail(iioe.getMessage() + " Bytes send total: " + iioe.bytesTransferred);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
            Assert.fail(e.getMessage());
        }

        int received = 0;
        while (dataIn.length > received) {
            try {
                int length = readSpc.getInputStream().read(dataIn, received, dataIn.length - received);
                if (length >= 0) {
                    received += length;
                } else {
                    throw new RuntimeException();
                }
            } catch (InterruptedIOException iioe) {
                LOG.log(Level.SEVERE, "{0} Bytes received total: {1} Bytes received this: {2}", new Object[]{iioe.getMessage(), received, iioe.bytesTransferred});
                Assert.fail(iioe.getMessage() + " Bytes received total: " + received + " Bytes received this: " + iioe.bytesTransferred);
            } catch (IOException e) {
                LOG.severe(e.getMessage());
            }
        }
        Assert.assertArrayEquals(dataOut, dataIn);
    }

    private void writeSingle() {
        try {
            for (int i = 0; i < dataOut.length; i++) {
                writeSpc.getOutputStream().write(dataOut[i]);
            }
            LOG.fine("data written");
        } catch (InterruptedIOException iioe) {
            LOG.log(Level.SEVERE, "{0} Bytes send total: {1}", new Object[]{iioe.getMessage(), iioe.bytesTransferred});
            Assert.fail(iioe.getMessage() + " Bytes send total: " + iioe.bytesTransferred);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
            Assert.fail(e.getMessage());
        }

        for (int i= 0; i < dataIn.length; i++) {
            try {
                int received = readSpc.getInputStream().read();
                if (received >= 0) {
                    dataIn[i] = (byte)received;
                } else {
                    throw new RuntimeException();
                }
            } catch (InterruptedIOException iioe) {
                LOG.log(Level.SEVERE, "{0} Bytes received total: {1} Bytes received this: {2}", new Object[]{iioe.getMessage(), i, iioe.bytesTransferred});
                Assert.fail(iioe.getMessage() + " Bytes received total: " + i + " Bytes received this: " + iioe.bytesTransferred);
            } catch (IOException e) {
                LOG.severe(e.getMessage());
            }
        }
        Assert.assertArrayEquals(dataOut, dataIn);
    }

    private void runTest(int baudrate, int buffersize) throws Exception {
        Assume.assumeNotNull(readSpc);
        Assume.assumeNotNull(writeSpc);
        LOG.info(MessageFormat.format("do run test @baudrate: {0}, buffer size: {1}", baudrate, buffersize));

        readSpc.setSerialPortParams(baudrate, dataBits, stopBits, parity);
        readSpc.setFlowControlMode(flowControl);
        if (writeSpc != readSpc) {
        writeSpc.setSerialPortParams(baudrate, dataBits, stopBits, parity);
        writeSpc.setFlowControlMode(flowControl);
        }
        
        printPorts(readSpc, writeSpc);

        writeBytes();
        writeSingle(); 
    }

    private void printPorts(RXTXPort sPort0, RXTXPort sPort1) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\n\tName:        %-20s %-20s\n", sPort0.getName(), sPort1.getName()));
        sb.append(String.format("\tBaudrate:    %-20d %-20d\n", sPort0.getBaudRate(), sPort1.getBaudRate()));
        sb.append(String.format("\tStopBits:    %-20d %-20d\n", sPort0.getStopBits(), sPort1.getStopBits()));
        sb.append(String.format("\tParity:      %-20d %-20d\n", sPort0.getParity(), sPort1.getParity()));
        sb.append(String.format("\tFlowControl: %-20d %-20d", sPort0.getFlowControlMode(), sPort1.getFlowControlMode()));

        LOG.log(Level.INFO, sb.toString());
    }

    @Test(timeout = 2000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 300)
    public void test_0000300() throws Exception {
        runTest(300, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 600)
    public void test_0000600() throws Exception {
        runTest(600, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 1200)
    public void test_0001200() throws Exception {
        runTest(1200, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 2400)
    public void test_0002400() throws Exception {
        runTest(2400, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 4800)
    public void test_0004800() throws Exception {
        runTest(4800, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 9600)
    public void test_0009600() throws Exception {
        runTest(9600, DEFAULT_TEST_BUFFER_SIZE);
    }


    //@Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 19200)
    public void test_0019200() throws Exception {
        runTest(19200, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 38400)
    public void test_0038400() throws Exception {
        runTest(38400, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 57600)
    public void test_0057600() throws Exception {
        runTest(57600, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 115200)
    public void test_0115200() throws Exception {
        runTest(115200, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 230400)
    public void test_0230400() throws Exception {
        runTest(230400, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 460800)
    public void test_0460800() throws Exception {
        runTest(460800, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 500000)
    public void test_0500000() throws Exception {
        runTest(500000, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 576000)
    public void test_0576000() throws Exception {
        runTest(576000, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 1000000)
    public void test_1000000() throws Exception {
        runTest(1000000, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 1152000)
    public void test_1152000() throws Exception {
        runTest(1152000, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 2000000)
    public void test_2000000() throws Exception {
        runTest(2000000, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 3000000)
    public void test_3000000() throws Exception {
        runTest(3000000, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Ignore
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 2 * 12 * 1000) / 4000000)
    public void test_4000000() throws Exception {
        runTest(4000000, DEFAULT_TEST_BUFFER_SIZE);
    }

}