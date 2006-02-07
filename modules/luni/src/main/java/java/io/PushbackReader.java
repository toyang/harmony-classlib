/* Copyright 1998, 2004 The Apache Software Foundation or its licensors, as applicable
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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.io;


/**
 * PushbackReader is a filter class which allows chars read to be pushed back
 * into the stream so that they can be reread. Parsers may find this useful.
 * There is a progammable limit to the number of chars which may be pushed back.
 * If the buffer of pushed back chars is empty, chars are read from the source
 * reader.
 * 
 */
public class PushbackReader extends FilterReader {
	/**
	 * The <code>char</code> array containing the chars to read.
	 */
	char[] buf;

	/**
	 * The current position within the char array <code>buf</code>. A value
	 * equal to buf.length indicates no chars available. A value of 0 indicates
	 * the buffer is full.
	 */
	int pos;

	/**
	 * Constructs a new PushbackReader on the Reader <code>in</code>. The
	 * size of the pushback buffer is set to the default, or 1 character.
	 * 
	 * @param in
	 *            the Reader to allow pushback operations on.
	 * 
	 */
	public PushbackReader(Reader in) {
		super(in);
		buf = new char[1];
		pos = 1;
	}

	/**
	 * Constructs a new PushbackReader on the Reader <code>in</code>. The
	 * size of the pushback buffer is set to <code>size</code> characters.
	 * 
	 * @param in
	 *            the Reader to allow pushback operations on.
	 * @param size
	 *            the size of the pushback buffer (<code>size>=0</code>) in
	 *            characters.
	 */
	public PushbackReader(Reader in, int size) {
		super(in);
		if (size > 0) {
			buf = new char[size];
			pos = size;
		} else
			throw new IllegalArgumentException(com.ibm.oti.util.Msg
					.getString("K0058")); //$NON-NLS-1$
	}

	/**
	 * Close this PushbackReader. This implementation closes this reader,
	 * releases the buffer used to pushback characters, and closes the target
	 * reader.
	 * 
	 * @throws IOException
	 *             If an error occurs attempting to close this Reader.
	 */
	public void close() throws IOException {
		synchronized (lock) {
			buf = null;
			in.close();
		}
	}

	/**
	 * Mark this PushbackReader. Since mark is not supported, this method will
	 * always throw IOException.
	 * 
	 * @param readAheadLimit
	 *            ignored, this method always throws IOException.
	 * 
	 * @throws IOException
	 *             Since mark is not supported byt PushbackReader.
	 */
	public void mark(int readAheadLimit) throws IOException {
		throw new IOException(com.ibm.oti.util.Msg.getString("K007f")); //$NON-NLS-1$
	}

	/**
	 * Answers a boolean indicating whether or not this PushbackReader supports
	 * mark() and reset(). This implementation always answers false since
	 * PushbackReaders do not support mark/reset.
	 * 
	 * @return boolean indicates whether or not mark() and reset() are
	 *         supported.
	 */
	public boolean markSupported() {
		return false;
	}

	/**
	 * Reads a single character from this PushbackReader and returns the result
	 * as an int. The 2 lowest-order bytes are returned or -1 of the end of
	 * stream was encountered. If the pushback buffer does not contain any
	 * available chars then a char from the target input reader is returned.
	 * 
	 * @return int The char read or -1 if end of stream.
	 * 
	 * @throws IOException
	 *             If an IOException occurs.
	 */
	public int read() throws IOException {
		synchronized (lock) {
			if (buf != null) {
				/* Is there a pushback character available? */
				if (pos < buf.length) {
					return buf[pos++];
				}
				/**
				 * Assume read() in the InputStream will return 2 lowest-order
				 * bytes or -1 if end of stream.
				 */
				return in.read();
			}
			throw new IOException();
		}
	}

	/**
	 * Reads at most <code>count</code> chars from this PushbackReader and
	 * stores them in char array <code>buffer</code> starting at
	 * <code>offset</code>. Answer the number of chars actually read or -1 if
	 * no chars were read and end of stream was encountered. This implementation
	 * reads chars from the pushback buffer first, then the target stream if
	 * more chars are required to satisfy <code>count</code>.
	 * 
	 * @param buffer
	 *            the char array in which to store the read chars.
	 * @param offset
	 *            the offset in <code>buffer</code> to store the read chars.
	 * @param count
	 *            the maximum number of chars to store in <code>buffer</code>.
	 * @return the number of chars actually read or -1 if end of stream.
	 * 
	 * @throws IOException
	 *             If an IOException occurs.
	 */
	public int read(char[] buffer, int offset, int count) throws IOException {
		// avoid int overflow
		if (0 <= offset && offset <= buffer.length && 0 <= count
				&& count <= buffer.length - offset) {
			synchronized (lock) {
				if (buf != null) {
					int copiedChars = 0, copyLength = 0, newOffset = offset;
					/* Are there pushback chars available? */
					if (pos < buf.length) {
						copyLength = (buf.length - pos >= count) ? count
								: buf.length - pos;
						System.arraycopy(buf, pos, buffer, newOffset,
								copyLength);
						newOffset += copyLength;
						copiedChars += copyLength;
						/* Use up the chars in the local buffer */
						pos += copyLength;
					}
					/* Have we copied enough? */
					if (copyLength == count) {
						return count;
					}
					int inCopied = in.read(buffer, newOffset, count
							- copiedChars);
					if (inCopied > 0) {
						return inCopied + copiedChars;
					}
					if (copiedChars == 0) {
						return inCopied;
					}
					return copiedChars;
				}
				throw new IOException();
			}
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Answers a <code>boolean</code> indicating whether or not this
	 * PushbackReader is ready to be read without blocking. If the result is
	 * <code>true</code>, the next <code>read()</code> will not block. If
	 * the result is <code>false</code> this Reader may or may not block when
	 * <code>read()</code> is sent.
	 * 
	 * @return boolean <code>true</code> if the receiver will not block when
	 *         <code>read()</code> is called, <code>false</code> if unknown
	 *         or blocking will occur.
	 * 
	 * @throws IOException
	 *             If the Reader is already closed or some other IO error
	 *             occurs.
	 */
	public boolean ready() throws IOException {
		synchronized (lock) {
			if (buf != null)
				return (buf.length - pos > 0 || in.ready());
			throw new IOException(com.ibm.oti.util.Msg.getString("K0080")); //$NON-NLS-1$
		}
	}

	/**
	 * Resets this PushbackReader. Since mark is not supported, always throw
	 * IOException.
	 * 
	 * @throws IOException
	 *             Since mark is not supported.
	 */
	public void reset() throws IOException {
		throw new IOException(com.ibm.oti.util.Msg.getString("K007f")); //$NON-NLS-1$
	}

	/**
	 * Push back all the chars in <code>buffer</code>. The chars are pushed
	 * so that they would be read back buffer[0], buffer[1], etc. If the push
	 * back buffer cannot handle the entire contents of <code>buffer</code>,
	 * an IOException will be thrown. Some of the buffer may already be in the
	 * buffer after the exception is thrown.
	 * 
	 * @param buffer
	 *            the char array containing chars to push back into the reader.
	 * 
	 * @throws IOException
	 *             If the pushback buffer becomes, or is, full.
	 */
	public void unread(char[] buffer) throws IOException {
		unread(buffer, 0, buffer.length);
	}

	/**
	 * Push back <code>count</code> number of chars in <code>buffer</code>
	 * starting at <code>offset</code>. The chars are pushed so that they
	 * would be read back buffer[offset], buffer[offset+1], etc. If the push
	 * back buffer cannot handle the chars copied from <code>buffer</code>,
	 * an IOException will be thrown. Some of the chars may already be in the
	 * buffer after the exception is thrown.
	 * 
	 * @param buffer
	 *            the char array containing chars to push back into the reader.
	 * @param offset
	 *            the location to start taking chars to push back.
	 * @param count
	 *            the number of chars to push back.
	 * 
	 * @throws IOException
	 *             If the pushback buffer becomes, or is, full.
	 */
	public void unread(char[] buffer, int offset, int count) throws IOException {
		if (count > pos)
			// Pushback buffer full
			throw new IOException(com.ibm.oti.util.Msg.getString("K007e"));
		// avoid int overflow
		if (0 <= offset && offset <= buffer.length && 0 <= count
				&& count <= buffer.length - offset) {
			synchronized (lock) {
				for (int i = offset + count - 1; i >= offset; i--)
					unread(buffer[i]);
			}
		} else
			throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Push back one <code>char</code>. Takes the char <code>oneChar</code>
	 * and puts in in the local buffer of chars to read back before accessing
	 * the target input stream.
	 * 
	 * @param oneChar
	 *            the char to push back into the stream.
	 * 
	 * @throws IOException
	 *             If the pushback buffer is already full.
	 */
	public void unread(int oneChar) throws IOException {
		synchronized (lock) {
			if (buf != null) {
				if (pos != 0)
					buf[--pos] = (char) oneChar;
				else
					throw new IOException(com.ibm.oti.util.Msg
							.getString("K007e")); //$NON-NLS-1$
			} else
				throw new IOException();
		}
	}
}
