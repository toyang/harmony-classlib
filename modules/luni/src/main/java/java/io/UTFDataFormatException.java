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
 * This IO exception is thrown when a program attempts to read a UTF-8 String
 * and the encoding is incorrect.
 * 
 * @see DataInputStream#readUTF()
 */
public class UTFDataFormatException extends IOException {

    private static final long serialVersionUID = 420743449228280612L;
    
	/**
	 * Constructs a new instance of this class with its walkback filled in.
	 */
	public UTFDataFormatException() {
		super();
	}

	/**
	 * Constructs a new instance of this class with its walkback and message
	 * filled in.
	 * 
	 * @param detailMessage
	 *            the detail message for the exception.
	 */
	public UTFDataFormatException(String detailMessage) {
		super(detailMessage);
	}

}
