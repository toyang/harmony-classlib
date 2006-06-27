/*
 *  Copyright 2005 The Apache Software Foundation or its licensors, as applicable.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
* @author Alexander V. Astapchuk
* @version $Revision$
*/

package java.security.serialization;

import java.security.Timestamp;
import java.security.cert.CertPath;
import java.util.Date;

import org.apache.harmony.testframework.serialization.SerializationTest;
import org.apache.harmony.security.tests.support.TestCertUtils;


/**
 * Serialization tests for <code>Timestamp</code>
 * 
 */

public class TimestampTest extends SerializationTest {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TimestampTest.class);
    }

    /**
     * @see com.intel.drl.test.SerializationTest#getData()
     */
    protected Object[] getData() {
        CertPath cpath = TestCertUtils.getCertPath();
        return new Object[] { new Timestamp(new Date(1146633251341L), cpath) };
    }
}
