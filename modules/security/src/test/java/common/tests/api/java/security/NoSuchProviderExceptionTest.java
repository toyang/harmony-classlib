/* Copyright 2005 The Apache Software Foundation or its licensors, as applicable
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

package tests.api.java.security;

import java.security.NoSuchProviderException;

public class NoSuchProviderExceptionTest extends junit.framework.TestCase {

	/**
	 * @tests java.security.NoSuchProviderException#NoSuchProviderException()
	 */
	public void test_Constructor() {
		// Test for method java.security.NoSuchProviderException()
		try {
			throw new NoSuchProviderException();
		} catch (NoSuchProviderException e) {
			assertNull("Message should be null", e.getMessage());
			assertEquals("Unexpected toString value",
					"java.security.NoSuchProviderException", e.toString());
		}
	}

	/**
	 * @tests java.security.NoSuchProviderException#NoSuchProviderException(java.lang.String)
	 */
	public void test_ConstructorLjava_lang_String() {
		// Test for method
		// java.security.NoSuchProviderException(java.lang.String)
		try {
			throw new NoSuchProviderException("Test string");
		} catch (NoSuchProviderException e) {
			assertEquals("Wrong message", "Test string", e.getMessage());
			assertEquals("Unexpected toString value",
					"java.security.NoSuchProviderException: Test string", e
							.toString());
		}
	}

	/**
	 * Sets up the fixture, for example, open a network connection. This method
	 * is called before a test is executed.
	 */
	protected void setUp() {
	}

	/**
	 * Tears down the fixture, for example, close a network connection. This
	 * method is called after a test is executed.
	 */
	protected void tearDown() {
	}
}