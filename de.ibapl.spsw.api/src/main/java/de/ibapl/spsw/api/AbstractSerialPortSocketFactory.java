/* jSSC (Java Simple Serial Connector) - serial port communication library.
 * © Alexey Sokolov (scream3r), 2010-2014.
 *
 * This file is part of jSSC.
 *
 * jSSC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jSSC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSSC.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you use jSSC in public project you can inform me about this by e-mail,
 * of course if you want it.
 *
 * e-mail: scream3r.org@gmail.com
 * web-site: http://scream3r.org | http://code.google.com/p/java-simple-serial-connector/
 */
package de.ibapl.spsw.api;

/*
 * #%L
 * SPSW Java
 * %%
 * Copyright (C) 2009 - 2014 atmodem4j
 * %%
 * atmodem4j - A serial port socket wrapper- http://atmodem4j.sourceforge.net/
 * Copyright (C) 2009-2014, atmodem4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
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
 * #L%
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author scream3r
 */
public abstract class AbstractSerialPortSocketFactory implements SerialPortSocketFactory {

    private final static Logger LOG = Logger.getLogger("de.ibapl.spsw");

    protected String getPortnamesPath() {
        switch (getOsName()) {
            case "linux": {
                return "/dev/";
            }
            case "SunOS": {
                return "/dev/term/";
            }
            case "Mac OS X":
            case "Darwin": {
                return "/dev/";
            }
            case "windows": {
                return "";
            }
            default: {
                LOG.log(Level.SEVERE, "Unknown OS, os.name: {0} mapped to: {1}", new Object[]{System.getProperty("os.name"), getOsName()});
                return null;
            }
        }
    }

    protected Pattern getPortnamesRegExp() {
        switch (getOsName()) {
            case "linux": {
                return Pattern.compile("(ttyS|ttyUSB|ttyACM|ttyAMA|rfcomm|ttyO)[0-9]{1,3}");
            }
            case "SunOS": {
                return Pattern.compile("[0-9]*|[a-z]*");
            }
            case "Mac OS X":
            case "Darwin": {
                return Pattern.compile("tty.(serial|usbserial|usbmodem).*");
            }
            case "windows": {
                return Pattern.compile("");
            }
            default: {
                LOG.log(Level.SEVERE, "Unknown OS, os.name: {0} mapped to: {1}", new Object[]{System.getProperty("os.name"), getOsName()});
                return null;
            }
        }
    }

    public String getArch() {
        String osArch = System.getProperty("os.arch");
        switch (getOsName()) {
            case "linux":
                if ("arm".equals(osArch)) {
                    String floatStr = "sf";
                    if (System.getProperty("java.library.path").contains("gnueabihf") || System.getProperty("java.library.path").contains("armhf")) {
                        floatStr = "hf";
                    } else {
                        LOG.log(Level.WARNING, "Can't find hardware|software floating point in libpath try readelf");
                        try {
                            Process readelfProcess = Runtime.getRuntime().exec("readelf -A /proc/self/exe");
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(readelfProcess.getInputStream()))) {
                                String buffer;
                                while ((buffer = reader.readLine()) != null && !buffer.isEmpty()) {
                                    if (buffer.toLowerCase().contains("Tag_ABI_VFP_args".toLowerCase())) {
                                        floatStr = "hf";
                                        break;
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            LOG.severe("Please install binutils to detect architecture ... use hf as default");
                            floatStr = "hf";
                            //Do nothing
                        }
                    }
                    return osArch + floatStr;
                } else {
                    return osArch;
                }
            default:
                return osArch;
        }

    }

    public String getOsName() {
        switch (System.getProperty("os.name")) {
            case "Linux":
                return "linux";
            case "Mac OS":
                throw new UnsupportedOperationException("Mac OS is currently not supported yet");
            case "Mac OS X":
                throw new UnsupportedOperationException("Mac OS X is currently not supported yet");
            case "Windows 95":
                return "windows";
            case "Windows 98":
                return "windows";
            case "Windows Me":
                return "windows";
            case "Windows NT":
                return "windows";
            case "Windows 2000":
                return "windows";
            case "Windows XP":
                return "windows";
            case "Windows 2003":
                return "windows";
            case "Windows Vista":
                return "windows";
            case "Windows 2008":
                return "windows";
            case "Windows 7":
                return "windows";
            case "Windows 8":
                return "windows";
            case "Windows 2012":
                return "windows";
            case "Windows CE":
                throw new UnsupportedOperationException("Windows CE is currently not supported yet");
            case "OS/2":
                throw new UnsupportedOperationException("OS/2 is currently not supported yet");
            case "MPE/iX":
                throw new UnsupportedOperationException("MPE/iX is currently not supported yet");
            case "HP-UX":
                throw new UnsupportedOperationException("HP-UX is currently not supported yet");
            case "AIX":
                throw new UnsupportedOperationException("AIX is currently not supported yet");
            case "OS/390":
                throw new UnsupportedOperationException("OS/390 is currently not supported yet");
            case "FreeBSD":
                throw new UnsupportedOperationException("FreeBSD is currently not supported yet");
            case "Irix":
                throw new UnsupportedOperationException("Irix is currently not supported yet");
            case "Digital Unix":
                throw new UnsupportedOperationException("Digital Unix is currently not supported yet");
            case "NetWare 4.11":
                throw new UnsupportedOperationException("NetWare 4.11 is currently not supported yet");
            case "OSF1":
                throw new UnsupportedOperationException("OSF1 is currently not supported yet");
            case "OpenVMS":
                throw new UnsupportedOperationException("OpenVMS is currently not supported yet");
            default:
                throw new RuntimeException("Can't figure out OS: " + System.getProperty("os.name"));
        }

    }

    public abstract boolean isLibLoaded();

    public abstract boolean loadNativeLib();

    //since 2.1.0 -> Fully rewrited port name comparator
    protected class PortnamesComparator implements Comparator<String> {

        @Override
        public int compare(String valueA, String valueB) {

            if (valueA.equalsIgnoreCase(valueB)) {
                return valueA.compareTo(valueB);
            }

            int minLength = Math.min(valueA.length(), valueB.length());

            int shiftA = 0;
            int shiftB = 0;

            for (int i = 0; i < minLength; i++) {
                char charA = valueA.charAt(i - shiftA);
                char charB = valueB.charAt(i - shiftB);
                if (charA != charB) {
                    if (Character.isDigit(charA) && Character.isDigit(charB)) {
                        int[] resultsA = getNumberAndLastIndex(valueA, i - shiftA);
                        int[] resultsB = getNumberAndLastIndex(valueB, i - shiftB);

                        if (resultsA[0] != resultsB[0]) {
                            return resultsA[0] - resultsB[0];
                        }

                        if (valueA.length() < valueB.length()) {
                            i = resultsA[1];
                            shiftB = resultsA[1] - resultsB[1];
                        } else {
                            i = resultsB[1];
                            shiftA = resultsB[1] - resultsA[1];
                        }
                    } else {
                        if (Character.toLowerCase(charA) - Character.toLowerCase(charB) != 0) {
                            return Character.toLowerCase(charA) - Character.toLowerCase(charB);
                        }
                    }
                }
            }
            return valueA.compareToIgnoreCase(valueB);
        }

        /**
         * Evaluate port <b>index/number</b> from <b>startIndex</b> to the
         * number end. For example: for port name <b>serial-123-FF</b> you
         * should invoke this method with <b>startIndex = 7</b>
         *
         * @return If port <b>index/number</b> correctly evaluated it value will
         * be returned<br>
         * <b>returnArray[0] = index/number</b><br>
         * <b>returnArray[1] = stopIndex</b><br>
         *
         * If incorrect:<br>
         * <b>returnArray[0] = -1</b><br>
         * <b>returnArray[1] = startIndex</b><br>
         *
         * For this name <b>serial-123-FF</b> result is:
         * <b>returnArray[0] = 123</b><br>
         * <b>returnArray[1] = 10</b><br>
         */
        private int[] getNumberAndLastIndex(String str, int startIndex) {
            String numberValue = "";
            int[] returnValues = {-1, startIndex};
            for (int i = startIndex; i < str.length(); i++) {
                returnValues[1] = i;
                char c = str.charAt(i);
                if (Character.isDigit(c)) {
                    numberValue += c;
                } else {
                    break;
                }
            }
            try {
                returnValues[0] = Integer.valueOf(numberValue);
            } catch (Exception ex) {
                //Do nothing
            }
            return returnValues;
        }
    };
    //<-since 2.1.0

    /**
     * Get sorted array of serial ports in the system using default
     * settings:<br>
     *
     * <b>Search path</b><br>
     * Windows - ""(always ignored)<br>
     * Linux - "/dev/"<br>
     * Solaris - "/dev/term/"<br>
     * MacOSX - "/dev/"<br>
     *
     * <b>RegExp</b><br>
     * Windows - ""<br>
     * Linux - "(ttyS|ttyUSB|ttyACM|ttyAMA|rfcomm)[0-9]{1,3}"<br>
     * Solaris - "[0-9]*|[a-z]*"<br>
     * MacOSX - "tty.(serial|usbserial|usbmodem).*"<br>
     *
     * @return String array. If there is no ports in the system String[] with
     * <b>zero</b> length will be returned (since jSSC-0.8 in previous versions
     * null will be returned)
     */
    public Set<String> getPortNames(boolean hideBusyPorts) {
        return getPortNames(getPortnamesPath(), getPortnamesRegExp(), new PortnamesComparator(), hideBusyPorts);
    }

    /**
     * Get sorted array of serial ports in the system located on searchPath
     *
     * @param searchPath Path for searching serial ports <b>(not null)</b><br>
     * The default search paths:<br>
     * Linux, MacOSX: <b>/dev/</b><br>
     * Solaris: <b>/dev/term/</b><br>
     * Windows: <b>this parameter ingored</b>
     *
     * @return String array. If there is no ports in the system String[]
     *
     * @since 2.3.0
     */
    public Set<String> getPortNames(String searchPath, boolean hideBusyPorts) {
        return getPortNames(searchPath, getPortnamesRegExp(), new PortnamesComparator(), hideBusyPorts);
    }

    /**
     * Get sorted array of serial ports in the system matched pattern
     *
     * @param pattern RegExp pattern for matching port names <b>(not null)</b>
     *
     * @return String array. If there is no ports in the system String[]
     *
     * @since 2.3.0
     */
    public Set<String> getPortNames(Pattern pattern, boolean hideBusyPorts) {
        return getPortNames(getPortnamesPath(), pattern, new PortnamesComparator(), hideBusyPorts);
    }

    /**
     * Get sorted array of serial ports in the system matched pattern
     *
     * @param comparator Comparator for sotring port names <b>(not null)</b>
     *
     * @return String array. If there is no ports in the system String[]
     *
     * @since 2.3.0
     */
    public Set<String> getPortNames(Comparator<String> comparator, boolean hideBusyPorts) {
        return getPortNames(getPortnamesPath(), getPortnamesRegExp(), comparator, hideBusyPorts);
    }

    /**
     * Get sorted array of serial ports in the system located on searchPath,
     * matched pattern
     *
     * @param searchPath Path for searching serial ports <b>(not null)</b><br>
     * The default search paths:<br>
     * Linux, MacOSX: <b>/dev/</b><br>
     * Solaris: <b>/dev/term/</b><br>
     * Windows: <b>this parameter ingored</b>
     * @param pattern RegExp pattern for matching port names <b>(not null)</b>
     *
     * @return String array. If there is no ports in the system String[]
     *
     * @since 2.3.0
     */
    public Set<String> getPortNames(String searchPath, Pattern pattern, boolean hideBusyPorts) {
        return getPortNames(searchPath, pattern, new PortnamesComparator(), hideBusyPorts);
    }

    /**
     * Get sorted array of serial ports in the system located on searchPath and
     * sorted by comparator
     *
     * @param searchPath Path for searching serial ports <b>(not null)</b><br>
     * The default search paths:<br>
     * Linux, MacOSX: <b>/dev/</b><br>
     * Solaris: <b>/dev/term/</b><br>
     * Windows: <b>this parameter ingored</b>
     * @param comparator Comparator for sotring port names <b>(not null)</b>
     *
     * @return String array. If there is no ports in the system String[]
     *
     * @since 2.3.0
     */
    public Set<String> getPortNames(String searchPath, Comparator<String> comparator, boolean hideBusyPorts) {
        return getPortNames(searchPath, getPortnamesRegExp(), comparator, hideBusyPorts);
    }

    /**
     * Get sorted array of serial ports in the system matched pattern and sorted
     * by comparator
     *
     * @param pattern RegExp pattern for matching port names <b>(not null)</b>
     * @param comparator Comparator for sotring port names <b>(not null)</b>
     *
     * @return String array. If there is no ports in the system String[]
     *
     * @since 2.3.0
     */
    public Set<String> getPortNames(Pattern pattern, Comparator<String> comparator, boolean hideBusyPorts) {
        return getPortNames(getPortnamesPath(), pattern, comparator, hideBusyPorts);
    }

    /**
     * Get sorted array of serial ports in the system located on searchPath,
     * matched pattern and sorted by comparator
     *
     * @param searchPath Path for searching serial ports <b>(not null)</b><br>
     * The default search paths:<br>
     * Linux, MacOSX: <b>/dev/</b><br>
     * Solaris: <b>/dev/term/</b><br>
     * Windows: <b>this parameter ingored</b>
     * @param pattern RegExp pattern for matching port names <b>(not null)</b>
     * @param comparator Comparator for sotring port names <b>(not null)</b>
     * @param hideBusyPorts
     *
     * @return String array. If there is no ports in the system String[]
     *
     * @since 2.3.0
     */
    public Set<String> getPortNames(String searchPath, Pattern pattern, Comparator<String> comparator, boolean hideBusyPorts) {
        if (searchPath == null || pattern == null || comparator == null) {
            return Collections.emptySet();
        }
        switch (getOsName()) {
            case "windows":
                if (!isLibLoaded()) {
                    //Make sure lib is loaded to avoid Link error
                    loadNativeLib();
                }
                return getWindowsPortNames(pattern, comparator, hideBusyPorts);
            default:
                return getUnixBasedPortNames(searchPath, pattern, comparator, hideBusyPorts);
        }
    }

    /**
     * Get serial port names in Windows
     *
     * @since 2.3.0
     */
    protected Set<String> getWindowsPortNames(Pattern pattern, Comparator<String> comparator, boolean hideBusyPorts) {
        try {
            String[] portNames = getWindowsBasedPortNames(hideBusyPorts);
            if (portNames == null) {
                return Collections.emptySet();
            }
            TreeSet<String> ports = new TreeSet<>(comparator);
            for (String portName : portNames) {
                if (pattern.matcher(portName).find()) {
                    ports.add(portName);
                }
            }
            return ports;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Get serial port names like an array of String
     *
     * @return unsorted array of String with port names
     */
    protected abstract String[] getWindowsBasedPortNames(boolean hideBusyPorts) throws IOException;

    /**
     * Universal method for getting port names of _nix based systems
     */
    protected Set<String> getUnixBasedPortNames(String searchPath, Pattern pattern, Comparator<String> comparator, boolean hideBusyPorts) {
        searchPath = (searchPath.equals("") ? searchPath : (searchPath.endsWith("/") ? searchPath : searchPath + "/"));
        File dir = new File(searchPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files.length > 0) {
                TreeSet<String> portsTree = new TreeSet<>(comparator);
                for (File file : files) {
                    String fileName = file.getName();
                    if (!file.isDirectory() && !file.isFile() && pattern.matcher(fileName).find()) {
                        String portName = searchPath + fileName;
                        if (hideBusyPorts) {
                            try (SerialPortSocket sp = createSerialPortSocket(portName)) {
                                sp.openAsIs();
                                portsTree.add(portName);
                            } catch (IOException ex) {
                                LOG.log(Level.FINEST, "find busy ports: " + portName, ex);
                            }
                        } else {
                            portsTree.add(portName);
                        }
                    }
                }
                return portsTree;
            }
        }
        return Collections.emptySet();
    }
}