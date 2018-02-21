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

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.ibapl.spsw.api.Baudrate;
import de.ibapl.spsw.api.DataBits;
import de.ibapl.spsw.api.FlowControl;
import de.ibapl.spsw.api.Parity;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.SerialPortSocketFactory;
import de.ibapl.spsw.api.StopBits;
import de.ibapl.spsw.api.TimeoutIOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Unit test for simple App.
 * Use @Ignore if your hardware can't handle higer speeds.
 * 
 */
public abstract class AbstractTwoPortSingleByteTest extends AbstractPortTest {

    private final static int DEFAULT_TEST_BUFFER_SIZE = 1024 * 2;

    public class ReceiverThread extends Thread {

        boolean done;
        final Object LOCK = new Object();
        byte[] dataIn;
        byte[] dataOut;
        long startTimeStamp;
        Exception ex;
        Error err;

        @Override
        public void run() {
            try {
                int offset = 0;
                while (true) {
                    try {
                        final int data = readSpc.getInputStream().read();
                        if (data >= 0) {
                            dataIn[offset] = (byte) data;
                            Assert.assertEquals("Error @Offset: " + offset, dataOut[offset], dataIn[offset]);
                            offset++;
                            if (offset == dataOut.length) {
                                break;
                            }
                        } else {
                            if (offset < dataIn.length) {
                                LOG.log(Level.SEVERE, "Bytes missing: {0}", dataIn.length - offset);
                                printArrays("Too short");
                            }
                            break;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                LOG.log(Level.INFO, "Byte total read: {0}", offset);
                LOG.log(Level.INFO, "Receive time in millis:: {0}", (System.currentTimeMillis() - startTimeStamp));
                synchronized (LOCK) {
                    done = true;
                    LOCK.notifyAll();
                    LOG.log(Level.INFO, "Send Thread finished");
                }
            } catch (Exception ex) {
                synchronized (LOCK) {
                    done = true;
                    this.ex = ex;
                    LOG.log(Level.SEVERE, "Send Thread Exception", ex);
                    LOCK.notifyAll();
                }
            } catch (Error err) {
                synchronized (LOCK) {
                    done = true;
                    this.err = err;
                    printArrays("Error");
                    LOG.log(Level.SEVERE, "Send Thread Error", err);
                    LOCK.notifyAll();
                }
            }
        }

        private void initBuffers(final int size) {
            dataOut = new byte[size];
            for (int i = 0; i < size; i++) {
//                  dataOut[i] = (byte) (i & 0xFF);
                dataOut[i] = (byte) (Math.round(Math.random() * 0xFF) & 0xFF);
            }
            dataIn = new byte[size];
        }

        private void printArrays(String title) {
            System.err.println(title);
            for (int i = 0; i < dataOut.length; i++) {
                System.err.println(i + "\t: " + dataOut[i] + "\t" + dataIn[i]);
            }
        }

    }

    private ReceiverThread receiverThread;

    @Override
    public void setUp() throws Exception {
    	super.setUp();
    	receiverThread = new ReceiverThread();
    }

    @Override
    public void tearDown() throws Exception {
        receiverThread = null;
        super.tearDown();
    }

    private void runTest(Baudrate baudrate, int buffersize) throws Exception {
    	assumeRWTest();
    	LOG.info(MessageFormat.format("do run test @baudrate: {0}, buffer size: {1}", baudrate.value, buffersize));
        receiverThread.initBuffers(buffersize);
        receiverThread.startTimeStamp = System.currentTimeMillis();

        openRaw(baudrate, DataBits.DB_8, StopBits.SB_1, Parity.NONE, FlowControl.getFC_NONE());
        printPorts();

        receiverThread.start();

        for (int i = 0; i < receiverThread.dataOut.length; i++) {
        	writeSpc.getOutputStream().write(receiverThread.dataOut[i]);
        	
        }
        LOG.log(Level.INFO, "Send time in millis:: {0}", (System.currentTimeMillis() - receiverThread.startTimeStamp));
        synchronized (receiverThread.LOCK) {
            while (!receiverThread.done) {
                receiverThread.LOCK.wait();
                if (receiverThread.ex != null) {
                    throw receiverThread.ex;
                }
                if (receiverThread.err != null) {
                    throw receiverThread.err;
                }
            }
            LOG.log(Level.INFO, "Thread finished received");
        }
            Assert.assertArrayEquals(receiverThread.dataOut, receiverThread.dataIn);
    }

    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 300)
    public void test_0000300() throws Exception {
        runTest(Baudrate.B300, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 1200)
    public void test_0001200() throws Exception {
        runTest(Baudrate.B1200, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 2400)
    public void test_0002400() throws Exception {
        runTest(Baudrate.B2400, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 9600)
    public void test_0009600() throws Exception {
        runTest(Baudrate.B9600, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 19200)
    public void test_0019200() throws Exception {
        runTest(Baudrate.B19200, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore //if your hardware can't handle this speed.
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 57600)
    public void test_0057600() throws Exception {
        runTest(Baudrate.B57600, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore //if your hardware can't handle this speed.
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 115200)
    public void test_0115200() throws Exception {
        runTest(Baudrate.B115200, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore //if your hardware can't handle this speed.
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 230400)
    public void test_0230400() throws Exception {
        runTest(Baudrate.B230400, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore //if your hardware can't handle this speed.
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 460800)
    public void test_0460800() throws Exception {
        runTest(Baudrate.B460800, DEFAULT_TEST_BUFFER_SIZE);
    }

    //@Ignore //if your hardware can't handle this speed.
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 500000)
    public void test_0500000() throws Exception {
        runTest(Baudrate.B500000, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Ignore //if your hardware can't handle this speed.
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 576000)
    public void test_0576000() throws Exception {
        runTest(Baudrate.B576000, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Ignore //if your hardware can't handle this speed.
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 1000000)
    public void test_1000000() throws Exception {
        runTest(Baudrate.B1000000, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Ignore //if your hardware can't handle this speed.
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 1152000)
    public void test_1152000() throws Exception {
        runTest(Baudrate.B1152000, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Ignore //if your hardware can't handle this speed.
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 2000000)
    public void test_2000000() throws Exception {
        runTest(Baudrate.B2000000, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Ignore //if your hardware can't handle this speed.
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 3000000)
    public void test_3000000() throws Exception {
        runTest(Baudrate.B3000000, DEFAULT_TEST_BUFFER_SIZE);
    }

    @Ignore //if your hardware can't handle this speed.
    @Test(timeout = 1000 + (DEFAULT_TEST_BUFFER_SIZE * 10 * 1000) / 4000000)
    public void test_4000000() throws Exception {
        runTest(Baudrate.B4000000, DEFAULT_TEST_BUFFER_SIZE);
    }

    //TODO does not work as expected Linux Kernel fills in buffer size?
    @Ignore
    @Test
    public void testWriteBufferFull() throws Exception {
        assumeRWTest();
		Assume.assumeTrue(HARDWARE_SUPPORTS_RTS_CTS);
        LOG.log(Level.INFO, "run testWriteSingleByteTimeout");
		// Set a high baudrate to speed up things
		openRaw(Baudrate.B115200, DataBits.DB_8, StopBits.SB_1, Parity.EVEN, FlowControl.getFC_RTS_CTS());
		setTimeouts(100, 1000, 10000);

		final byte[] b = new byte[1024*1024];
		for (int i = 0; i < b.length; i++ ) {
			b[i] = (byte)i;
		}
		int round = 1;
		int dataWritten;
		int overallDataWritten = 0;

		do {
			try {
				writeSpc.getOutputStream().write(0xFF);
				dataWritten = 1; //b.length;
			} catch (TimeoutIOException e) {
				dataWritten = e.bytesTransferred;
			}
			try {
				writeSpc.getOutputStream().flush();
			} catch (TimeoutIOException e) {
			}
			round++;
			overallDataWritten += dataWritten;
			LOG.log(Level.INFO,
					"Wrote: " + dataWritten + " (" + overallDataWritten + ") in " + round + " rounds; OutBuf:  " + writeSpc.getOutBufferBytesCount() + "inBuffer: " + readSpc.getInBufferBytesCount());
		} while (dataWritten > 0);

		LOG.log(Level.INFO,
				"Wrote: " + overallDataWritten + " in " + round + " rounds; OutBuf:  " + writeSpc.getOutBufferBytesCount());
		LOG.log(Level.INFO, "disable flow control to sped up closing");
		byte[] read = new byte[256];
		assertEquals(256, readSpc.getInputStream().read(read));
		new Thread(() -> {
			try {
				writeSpc.getOutputStream().write(b, 0, 1024);
				fail();
			} catch (TimeoutIOException tIoException) {
				assertEquals(512, tIoException.bytesTransferred);
			}catch (Exception e) {
				fail();
			}
		}).start();

		Thread.sleep(2000);
		
		assertEquals(256, readSpc.getInputStream().read(read));
		
		
		
		writeSpc.setBaudrate(Baudrate.B500000);
		writeSpc.setFlowControl(FlowControl.getFC_NONE());
    }
    
    /**
     * The receiving (spc[1] should stop transfer via RTS/CTS ...
     * @throws Exception
     */
    //TODO Does not work as expected .... 
    @Ignore
    @Test
    public void testFillInbuffer() throws Exception {
    	assumeRWTest();
        Assume.assumeTrue(HARDWARE_SUPPORTS_RTS_CTS);
		LOG.log(Level.INFO, "run testWriteSingleByteTimeout");
		// Set a high baudrate to speed up things
		openRaw(Baudrate.B115200, DataBits.DB_8, StopBits.SB_1, Parity.NONE, FlowControl.getFC_RTS_CTS());
		setTimeouts(100, 1000, 10000);

		final byte[] b = new byte[1024];
		for (int i = 0; i < b.length; i++ ) {
			b[i] = (byte)i;
		}
		int round = 0;
		int dataWritten;
		int overallDataWritten = 0;

		do {
			try {
				writeSpc.getOutputStream().write(b);
				dataWritten = b.length;
			} catch (TimeoutIOException e) {
				dataWritten = e.bytesTransferred;
			}
			try {
				writeSpc.getOutputStream().flush();
			} catch (TimeoutIOException e) {
			}
			round++;
			overallDataWritten += dataWritten;
			LOG.log(Level.INFO,
					"Wrote: " + dataWritten + " (" + overallDataWritten + ") in " + round + " rounds; OutBuf:  " + writeSpc.getOutBufferBytesCount() + "inBuffer: " + readSpc.getInBufferBytesCount());
			if (round == 1024) {
				break;
			}
		} while (dataWritten > 0);

		LOG.log(Level.INFO,
				"Wrote: " + overallDataWritten + " in " + round + " rounds; OutBuf:  " + writeSpc.getOutBufferBytesCount());
		LOG.log(Level.INFO, "disable flow control to sped up closing");
		int dataread = 0;
		int readTotal = 0;;
		do {
			try {
				dataread = readSpc.getInputStream().read(b);
				LOG.log(Level.INFO, "Read: " + dataread);
			} catch (TimeoutIOException e) {
				dataread = e.bytesTransferred;
				LOG.log(Level.INFO, "Got timeout");
			}
			readTotal += dataread;
		} while (dataread > 0);
		assertEquals(overallDataWritten, readTotal);
		
		
		
		writeSpc.setBaudrate(Baudrate.B500000);
		writeSpc.setFlowControl(FlowControl.getFC_NONE());
    }
    
}
