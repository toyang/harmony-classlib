/* Copyright 2006 The Apache Software Foundation or its licensors, as applicable
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

package org.apache.harmony.luni.tests.java.util;

import java.io.Serializable;
import java.util.DuplicateFormatFlagsException;

import junit.framework.TestCase;

import org.apache.harmony.testframework.serialization.SerializationTest;
import org.apache.harmony.testframework.serialization.SerializationTest.SerializableAssert;

public class DuplicateFormatFlagsExceptionTest extends TestCase {

	/**
	 * @tests java.util.DuplicateFormatFlagsException#DuplicateFormatFlagsException(String)
	 */
	public void test_duplicateFormatFlagsException() {
		try {
            new DuplicateFormatFlagsException(null);
			fail("should throw NullPointerException.");
		} catch (NullPointerException e) {
			// desired
		}
	}

	/**
	 * @tests java.util.DuplicateFormatFlagsException#getFlags()
	 */
	public void test_getFlags() {
		String strFlags = "MYTESTFLAGS";
		DuplicateFormatFlagsException duplicateFormatException = new DuplicateFormatFlagsException(
				strFlags);
		assertEquals(strFlags, duplicateFormatException.getFlags());
	}

	/**
	 * @tests java.util.DuplicateFormatFlagsException#getMessage()
	 */
	public void test_getMessage() {
		String strFlags = "MYTESTFLAGS";
		DuplicateFormatFlagsException duplicateFormatException = new DuplicateFormatFlagsException(
				strFlags);
		assertTrue(null != duplicateFormatException.getFlags());

	}

    // comparator for DuplicateFormatFlagsException objects
    private static final SerializableAssert exComparator = new SerializableAssert() {
        public void assertDeserialized(Serializable initial,
                Serializable deserialized) {

            SerializationTest.THROWABLE_COMPARATOR.assertDeserialized(initial,
                    deserialized);

            DuplicateFormatFlagsException initEx = (DuplicateFormatFlagsException) initial;
            DuplicateFormatFlagsException desrEx = (DuplicateFormatFlagsException) deserialized;

            assertEquals("Flags", initEx.getFlags(), desrEx.getFlags());
        }
    };

    /**
     * @tests serialization/deserialization.
     */
    public void testSerializationSelf() throws Exception {

        SerializationTest.verifySelf(new DuplicateFormatFlagsException(
                "TESTDESC"), exComparator);
    }

    /**
     * @tests serialization/deserialization compatibility with RI.
     */
    public void testSerializationCompatibility() throws Exception {

        SerializationTest.verifyGolden(this, new DuplicateFormatFlagsException(
                "TESTDESC"), exComparator);
    }
}
