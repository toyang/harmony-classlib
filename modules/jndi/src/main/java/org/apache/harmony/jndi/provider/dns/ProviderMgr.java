/*
 * Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Alexei Y. Zakharov
 * @version $Revision: 1.1.2.4 $
 */

package org.apache.harmony.jndi.provider.dns;

import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * Contains some useful routines that are used in other classes.
 *
 * @author Alexei Zakharov
 * @version $Revision: 1.1.2.4 $
 */
public class ProviderMgr {

    static final int LOG_NONE = 0;
    static final int LOG_ERROR = 1;
    static final int LOG_WARNING = 2;
    static final int LOG_DEBUG = 3;

    static final boolean CHECK_NAMES = false;

    static final Logger logger =
            Logger.getLogger(ProviderConstants.LOGGER_NAME);

    /**
     * Parses the given domain name and converts it into
     * <code>length label length label ... length label</code> sequence of bytes.
     * 
     * @param name a domain name, a dot-separated list of labels
     * @param buffer target buffer in which the result will be written
     * @param startIdx the index to start at while writing to the buffer array
     * @return updated index of the buffer array
     */
    public static int writeName(String name, byte[] buffer, int startIdx)
            throws DomainProtocolException
    {
        StringTokenizer st; 
        int idx = startIdx;
        
        if (name != null) {
            // initial check
            if (buffer == null) {
                throw new NullPointerException("buffer is null");
            }
            if (startIdx > buffer.length || startIdx < 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            // parsing the name
            //if (CHECK_NAMES && !checkName(name)) {
            //    throw new DomainProtocolException(
            //           "The syntax of the domain name " +
            //            name + " does not conform to RFC 1035");
            //}
            st = new StringTokenizer(name, ".");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                byte[] tokenBytes;
                int tokenBytesLen;

                if (token == null || token.length() == 0) {
                    break;
                }
                tokenBytes = token.getBytes();
                tokenBytesLen = tokenBytes.length;
                if (tokenBytesLen > ProviderConstants.LABEL_MAX_CHARS) {
                    throw new DomainProtocolException("The domain label is " +
                            "too long: " + token);
                }
                if (idx + tokenBytesLen + 1 > buffer.length) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                buffer[idx++] = (byte) tokenBytesLen;
                for (int i = 0; i < tokenBytesLen; i++) {
                    buffer[idx++] = tokenBytes[i];
                }
                if (idx - startIdx + 1 > ProviderConstants.NAME_MAX_CHARS) {
                    throw new DomainProtocolException("The domain name " +
                            "is more than " + ProviderConstants.NAME_MAX_CHARS +
                            " octets long: " + name);
                }
            }
            // every domain name should end with an zero octet
            buffer[idx++] = (byte) 0;
        }
        return idx;
    }

    /**
     * Parses the domain name from the sequence of bytes.
     * 
     * @param mesBytes byte representation of the message
     * @param startIdx the position to start the parsing at
     * @param result the string buffer to store parsed strings into
     * @return updated index of <code>mesBytes</code> array
     * @throws DomainProtocolException if something went wrong
     */
    public static int parseName(byte[] mesBytes, int startIdx,
            StringBuffer result) throws DomainProtocolException
    {
        int idx = startIdx;
        boolean firstTime = true;

        if (mesBytes == null) {
            throw new NullPointerException("Input byte array is null");
        }
        if (result == null) {
            throw new NullPointerException("The result string buffer is null");
        }
        while (true) {
            int n = parse8Int(mesBytes, idx++);

            if (n == 0) {
                // end of the domain name reached
                break;
            }
            if ((n & 0xc0) == 0xc0) {
                // compressed label
                int namePtr = parse16Int(mesBytes, --idx) & 0x3fff;

                idx += 2;
                if (!firstTime) {
                    result.append('.');
                }
                parseName(mesBytes, namePtr, result);
                break;
            }
            else {
                // plain label
                if (n > ProviderConstants.LABEL_MAX_CHARS) {
                    throw new DomainProtocolException("Domain label is too " +
                            " long");
                }
                if (idx + n > mesBytes.length) {
                    throw new DomainProtocolException("Truncated data while " +
                            " parsing the domain name");
                }
                // append parsed label
                if (firstTime) {
                    firstTime = false;                
                } else {
                    result.append('.');
                }
                result.append(new String(mesBytes, idx, n));
                idx += n;
            }
        }
        return idx;
    }
    
    /**
     * Compares two labels and returns the matching count (number of the
     *  matching labels down from the root).
     * 
     * @param name1 first name
     * @param name2 second name
     * @return number of equal labels from the root to leaves
     */
    public static int getMatchingCount(String name1, String name2) {
        StringTokenizer st1, st2;
        int k = 0;

        if (name1 == null || name2 == null) {
            return 0;
        }
        st1 = new StringTokenizer(name1, ".");
        st2 = new StringTokenizer(name2, ".");
        while (st1.hasMoreTokens() && st2.hasMoreTokens()) {
            if (st1.nextToken().equalsIgnoreCase(st2.nextToken())) {
                k++;
            } else {
                break;
            }
        }
        return k;
    }

    /**
     * Returns the name of parent DNS zone for given zone.
     * @param name the current DNS zone name
     * @return the name of the parent
     */
    public static String getParentName(String name) {
        int n;

        if (name == null) {
            return null;
        }
        if (name.trim().equals(".") || name.trim().length() == 0) {
            return ".";
        }
        n = name.indexOf('.');
        if (n != -1 && name.length() > n + 1)  {
            return name.substring(n + 1, name.length());
        }
        return ".";
    }

    /**
     * Writes a 16-bit integer value into the buffer, high byte first
     * 
     * @param value the value to write, first 16 bits will be taken
     * @param buffer the buffer to write into
     * @param startIdx a starting index
     * @return updated index
     */
    public static int write16Int(int value, byte[] buffer, int startIdx) {
        int idx = startIdx;

        buffer[idx++] = (byte) ((value & 0xff00) >> 8);
        buffer[idx++] = (byte) (value & 0xff);
        return idx;
    }


    /**
     * Writes a 32-bit integer value into the buffer, highest byte first
     * 
     * @param value the value to write, first 32 bits will be taken 
     * @param buffer the buffer to write into
     * @param startIdx a starting index
     * @return updated index
     */
    public static int write32Int(long value, byte[] buffer, int startIdx) {
        int idx = startIdx;

        buffer[idx++] = (byte) ((value & 0xff000000l) >> 24);
        buffer[idx++] = (byte) ((value & 0xff0000) >> 16);
        buffer[idx++] = (byte) ((value & 0xff00) >> 8);
        buffer[idx++] = (byte) (value & 0xff);
        return idx;
    } 

    /**
     * Parses 8 bit integer.
     * Buffer index should be updated manually after call to this method.
     * @param buffer sequence of bytes
     * @param startIdx the index to start at
     * @return parsed integer value
     */
    public static int parse8Int(byte[] buffer, int idx) {
        return ((int) buffer[idx]) & 0xff;
    }

    /**
     * Parses 16 bit integer.
     * Buffer index should be updated manually after call to this method.
     * @param buffer sequence of bytes
     * @param startIdx the index to start at
     * @return parsed integer value
     */
    public static int parse16Int(byte[] buffer, int idx) {
        int a = (((int) buffer[idx]) & 0xff) << 8;
        int b = ((int) buffer[idx + 1]) & 0xff;

        return (a | b);
    }

    /**
     * Parses 32 bit integer.
     * Buffer index should be updated manually after call to this method.
     * @param buffer sequence of bytes
     * @param startIdx the index to start at
     * @return parsed integer value
     */
    public static long parse32Int(byte[] buffer, int idx) {
        long a = (((long) buffer[idx]) & 0xff) << 24;
        long b = (((long) buffer[idx + 1]) & 0xff) << 16;
        long c = (((long) buffer[idx + 2]) & 0xff) << 8;
        long d = ((long) buffer[idx + 3]) & 0xff;

        return (a | b | c | d); 
    }
   
    /**
     * Writes character string preceded with length octet.
     * @param value string value to write
     * @param buffer buffer to write to
     * @param startIdx index in buffer to start from
     * @return updated index
     * @throws NullPointerExcetpion if some argument is null
     * @throws DomainProtocolException if string is too long
     */
    public static int writeCharString(String value, byte[] buffer, int startIdx)
            throws DomainProtocolException
    {
        byte[] bytes;
        int idx = startIdx;

        if (value == null || buffer == null) {
            throw new NullPointerException("value or buffer is null");
        }
        if (value.length() > 255) {
            throw new DomainProtocolException("Character string is too long");
        }
        bytes = value.getBytes();
        buffer[idx++] = (byte) bytes.length; 
        for (int i = 0; i < bytes.length; i++) {
            buffer[idx++] = bytes[i];
        }
        return idx;
    }

    /**
     * Parses the string of characters preceded with length octet.
     * @param mesBytes message bytes
     * @param startIdx the index to start parsing from
     * @param result string buffer to write the result too 
     * @return updated index
     */
    public static int parseCharString(byte[] mesBytes, int startIdx,
            StringBuffer result)
    {
        int len;

        if (mesBytes == null || result == null) {
            throw new NullPointerException("mesBytes or result is null");
        }
        len = mesBytes[startIdx];
        result.append(new String(mesBytes, startIdx + 1, len));
        return startIdx + 1 + len;
    }
    
    /**
     * Sets or drops specific bit(s) of the given number.
     * 
     * @param value target integer value 
     * @param mask specifies bit(s) position(s)
     * @param bit set if <code>true</code>, drop if <code>false</code>
     * @return updated <code>value</code>
     */
    public static int setBit(int value, int mask, boolean bit) {
        if (bit) {
            value |= mask; 
        } else {
            value &= ~mask;
        }
        return value; 
    }

    /**
     * Checks if any of specified bits is set.
     * 
     * @param value the number to look at
     * @param mask a bit mask
     * @return <code>true</code> of <code>false</code>
     */
    public static boolean checkBit(int value, int mask) {
        if ((value & mask) == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Compares two DNS names.
     * <ol>
     * <li>Case insensitive</li>
     * <li>Appends "." to the end of the name if necessary before comparison</li>
     * </ol>   
     * @param name1 name1
     * @param name2 name2
     * @return <code>true</code> if names are equal; <code>false</code> otherwise
     */
    public static boolean namesAreEqual(String name1, String name2)
    {
        if (!name1.endsWith(".")) {
            name1 += ".";
        }
        if (!name2.endsWith(".")) {
            name2 += ".";
        }
        return name1.equalsIgnoreCase(name2);
    }

    /**
     * Removes _Service._Proto fields from SRV-style qName if any. Adds
     * final dot to the end of name
     * @param name name to process
     * @return converted name
     */
/*
    public static String removeSRVExtra(String name) {
        StringTokenizer st;
        int k = 0;
        StringBuffer res = new StringBuffer();
        
        if (name == null) {
            return name;
        }
        st = new StringTokenizer(name, ".", false);
        while (st.hasMoreTokens()) { 
            String token = st.nextToken();

            if ((k != 0 && k != 1) || !token.startsWith("_")) {
                res.append(token);
            }
            k++;
        }
        return res.toString();
    }
*/  
      
    /**
     * Converts all letters to lower case and adds "." to the end of zone name
     * if necessary.
     * @param zone zone name
     * @return expanded zone name
     */
    public static String normalizeName(String zone) {
        if (zone == null) {
            return zone;
        }
        else {
            return zone.endsWith(".") ? zone.toLowerCase() :
                    zone.toLowerCase() + ".";
        }
    }
    

    /**
     * Creates the text representation of IPv4 address.
     * 
     * @param ip the first four bytes should contain an IPv4 address
     * @return string in <code>n.n.n.n</code> format
     * @throws java.lang.IllegalArgumentException if given array has the length
     *  less than four
     */
    public static String getIpStr(byte[] ip) {
        StringBuffer sb = new StringBuffer();

        if (ip == null || ip.length < 4) {
            throw new IllegalArgumentException("Given array is null or has the"+
                    " length less than four");
        }
        for (int i = 0; i < 4; i++) {
            if (i > 0) {
                sb.append(".");
            }
            sb.append("" + (((int) ip[i]) & 0xff));
        }
        return sb.toString();
    }

    /**
     * Parses the text representation of IPv4 address.
     * 
     * @param ipStr string in <code>n.n.n.n</code> format.
     * @return four bytes with parsed IP address
     * @throws java.lang.NullPointerException if <code>ipStr</code> is null
     * @throws java.lang.IllegalArgumentException if given string is not in
     *  appropriate format
     */
    public static byte[] parseIpStr(String ipStr) {
        StringTokenizer st;
        byte[] b = new byte[4];
        final String errMsg1 = "Given string is not in appropriate format"; 

        if (ipStr != null) {
            int k = 0;

            st = new StringTokenizer(ipStr, ".");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                int n;

                try {
                    n = Integer.parseInt(token);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(errMsg1);
                }
                b[k++] = (byte) n;
            }
            if (k != 4) {
                throw new IllegalArgumentException(errMsg1);
            }
        } else {
            throw new NullPointerException(
                    "Given string representation is null");
        }
        return b;
    }

    /**
     * @param str string name of the DNS record class
     * @return integer number for the class; <code>-1</code> if not found
     */
    public static int getRecordClassNumber(String str) {
        for (int i = 0; i < ProviderConstants.rrClassNames.length; i++) {
            if (ProviderConstants.rrClassNames[i] != null) {
                if (str.equalsIgnoreCase(ProviderConstants.rrClassNames[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * @param str string name of the DNS record type
     * @return integer number for the type; <code>-1</code> if not found
     */
    public static int getRecordTypeNumber(String str) {
        for (int i = 0; i < ProviderConstants.rrTypeNames.length; i++) {
            if (ProviderConstants.rrTypeNames[i] != null) {
                if (str.equalsIgnoreCase(ProviderConstants.rrTypeNames[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

}
