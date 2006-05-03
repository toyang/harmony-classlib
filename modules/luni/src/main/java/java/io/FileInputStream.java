/* Copyright 1998, 2006 The Apache Software Foundation or its licensors, as applicable
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


import java.nio.channels.FileChannel;

import org.apache.harmony.luni.platform.IFileSystem;
import org.apache.harmony.luni.platform.Platform;
import org.apache.harmony.nio.FileChannelFactory;

/**
 * FileInputStream is a class for reading bytes from a file. This class may also
 * be used with other InputStreams, ie: BufferedInputStream, to read data from a
 * file with buffering.
 * 
 * @see FileOutputStream
 */
public class FileInputStream extends InputStream implements Closeable{
	/**
	 * The FileDescriptor representing this FileInputStream.
	 */
	FileDescriptor fd;

	// The unique file channel associated with this FileInputStream (lazily
	// initialized).
	private FileChannel channel;

    private IFileSystem fileSystem = Platform.getFileSystem();

    private Object repositioningLock = new Object();

	/**
	 * Constructs a new FileInputStream on the File <code>file</code>. If the
	 * file does not exist, the <code>FileNotFoundException</code> is thrown.
	 * 
	 * @param file
	 *            the File on which to stream reads.
	 * 
	 * @throws FileNotFoundException
	 *             If the <code>file</code> is not found.
	 * 
	 * @see java.lang.SecurityManager#checkRead(FileDescriptor)
	 * @see java.lang.SecurityManager#checkRead(String)
	 * @see java.lang.SecurityManager#checkRead(String, Object)
	 */
	public FileInputStream(File file) throws FileNotFoundException {
		super();
		SecurityManager security = System.getSecurityManager();
		if (security != null)
			security.checkRead(file.getPath());
		fd = new FileDescriptor();
		fd.descriptor = fileSystem.open(file.properPath(true),
                IFileSystem.O_RDONLY);
        channel = FileChannelFactory.getFileChannel(this, fd.descriptor,
                IFileSystem.O_RDONLY);
	}

	/**
	 * Constructs a new FileInputStream on the FileDescriptor <code>fd</code>.
	 * The file must already be open, therefore no
	 * <code>FileNotFoundException</code> will be thrown.
	 * 
	 * @param fd
	 *            the FileDescriptor on which to stream reads.
	 * 
	 * @see java.lang.SecurityManager#checkRead(FileDescriptor)
	 * @see java.lang.SecurityManager#checkRead(String)
	 * @see java.lang.SecurityManager#checkRead(String, Object)
	 */
	public FileInputStream(FileDescriptor fd) {
		super();
		if (fd == null) {
            throw new NullPointerException();
        }
        SecurityManager security = System.getSecurityManager();
        if (security != null)
            security.checkRead(fd);
        this.fd = fd;
        channel = FileChannelFactory.getFileChannel(this, fd.descriptor,
                IFileSystem.O_RDONLY);
	}

	/**
	 * Constructs a new FileInputStream on the file named <code>fileName</code>.
	 * If the file does not exist, the <code>FileNotFoundException</code> is
	 * thrown. The <code>fileName</code> may be absolute or relative to the
	 * System property <code>"user.dir"</code>.
	 * 
	 * @param fileName
	 *            the file on which to stream reads.
	 * 
	 * @throws FileNotFoundException
	 *             If the <code>fileName</code> is not found.
	 */
	public FileInputStream(String fileName) throws FileNotFoundException {
		this(new File(fileName));
	}

	/**
	 * Answers a int representing then number of bytes that are available before
	 * this InputStream will block. This method always returns the size of the
	 * file minus the current position.
	 * 
	 * @return the number of bytes available before blocking.
	 * 
	 * @throws IOException
	 *             If an error occurs in this stream.
	 */
	public int available() throws IOException {
        openCheck();
        synchronized (repositioningLock) {
            long currentPosition = fileSystem.seek(fd.descriptor, 0L,
                    IFileSystem.SEEK_CUR);
            long endOfFilePosition = fileSystem.seek(fd.descriptor, 0L,
                    IFileSystem.SEEK_END);
            fileSystem.seek(fd.descriptor, currentPosition,
                    IFileSystem.SEEK_SET);
            return (int) (endOfFilePosition - currentPosition);
        }
    }

	/**
	 * Close the FileInputStream.
	 * 
	 * @throws IOException
	 *             If an error occurs attempting to close this FileInputStream.
	 */
	public void close() throws IOException {
		synchronized (channel) {
            synchronized (this) {
                //FIXME: System.in, out, err may not want to be closed?                
                if (channel.isOpen() && fd.descriptor >= 0) {
                    channel.close();
                }
                fd.descriptor = -1;
            }
        }
	}

	/**
	 * This method ensures that all resources for this file are released when it
	 * is about to be garbage collected.
	 * 
	 * @throws IOException
	 *             If an error occurs attempting to finalize this
	 *             FileInputStream.
	 */
	protected void finalize() throws IOException {
		close();
	}

	/**
	 * Answers the FileChannel equivalent to this input stream.
	 * <p>
	 * The file channel is read-only and has an initial position within the file
	 * that is the same as the current position of the FileInputStream within
	 * the file. All changes made to the underlying file descriptor state via
	 * the channel are visible by the input stream and vice versa.
	 * </p>
	 * 
	 * @return the file channel representation for this FileInputStream.
	 */
	public FileChannel getChannel() {
		return channel;
	}

	/**
	 * Answers the FileDescriptor representing the operating system resource for
	 * this FileInputStream.
	 * 
	 * @return the FileDescriptor for this FileInputStream.
	 * 
	 * @throws IOException
	 *             If an error occurs attempting to get the FileDescriptor of
	 *             this FileInputStream.
	 */
	public final FileDescriptor getFD() throws IOException {
		return fd;
	}

	/**
	 * Reads a single byte from this FileInputStream and returns the result as
	 * an int. The low-order byte is returned or -1 of the end of stream was
	 * encountered.
	 * 
	 * @return the byte read or -1 if end of stream.
	 * 
	 * @throws IOException
	 *             If the stream is already closed or another IOException
	 *             occurs.
	 */
	public int read() throws IOException {
		byte[] readed = new byte[1];
        int result = read(readed, 0, 1);
        return result == -1 ? -1 : readed[0] & 0xff;
	}

	/**
	 * Reads bytes from the FileInputStream and stores them in byte array
	 * <code>buffer</code>. Answer the number of bytes actually read or -1 if
	 * no bytes were read and end of stream was encountered.
	 * 
	 * @param buffer
	 *            the byte array in which to store the read bytes.
	 * @return the number of bytes actually read or -1 if end of stream.
	 * 
	 * @throws IOException
	 *             If the stream is already closed or another IOException
	 *             occurs.
	 */
	public int read(byte[] buffer) throws IOException {
		return read(buffer, 0, buffer.length);
	}

	/**
	 * Reads at most <code>count</code> bytes from the FileInputStream and
	 * stores them in byte array <code>buffer</code> starting at
	 * <code>offset</code>. Answer the number of bytes actually read or -1 if
	 * no bytes were read and end of stream was encountered.
	 * 
	 * @param buffer
	 *            the byte array in which to store the read bytes.
	 * @param offset
	 *            the offset in <code>buffer</code> to store the read bytes.
	 * @param count
	 *            the maximum number of bytes to store in <code>buffer</code>.
	 * @return the number of bytes actually read or -1 if end of stream.
	 * 
	 * @throws IOException
	 *             If the stream is already closed or another IOException
	 *             occurs.
	 */
	public int read(byte[] buffer, int offset, int count) throws IOException {
        if (count < 0 || offset < 0 || offset > buffer.length
                || count > buffer.length - offset) {
            throw new IndexOutOfBoundsException();
        }

        openCheck();
        synchronized (repositioningLock) {
            return (int) fileSystem.read(fd.descriptor, buffer, offset, count);
        }
	}

	/**
	 * Skips <code>count</code> number of bytes in this FileInputStream.
	 * Subsequent <code>read()</code>'s will not return these bytes unless
	 * <code>reset()</code> is used. This method may perform multiple reads to
	 * read <code>count</code> bytes. This default implementation reads
	 * <code>count</code> bytes into a temporary buffer.
	 * 
	 * @param count
	 *            the number of bytes to skip.
	 * @return the number of bytes actually skipped.
	 * 
	 * @throws IOException
	 *             If the stream is already closed or another IOException
	 *             occurs.
	 */
    public long skip(long count) throws IOException {
        openCheck();
        synchronized (repositioningLock) {
            final long currentPosition = fileSystem.seek(fd.descriptor, 0L,
                    IFileSystem.SEEK_CUR);
            final long newPosition = fileSystem.seek(fd.descriptor,
                    currentPosition + count, IFileSystem.SEEK_SET);
            return newPosition - currentPosition;
        }
    }

    private synchronized void openCheck() throws IOException {
        if (fd.descriptor < 0) {
            throw new IOException();
        }
    }
}
