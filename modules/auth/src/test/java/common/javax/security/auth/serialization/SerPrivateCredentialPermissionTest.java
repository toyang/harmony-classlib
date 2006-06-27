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
* @author Maxim V. Makarov
* @version $Revision$
*/

package javax.security.auth.serialization;

import javax.security.auth.PrivateCredentialPermission;

import org.apache.harmony.testframework.serialization.SerializationTest;


/**
 * Serialization test for PrivateCredentialPermission class
 */

public class SerPrivateCredentialPermissionTest extends SerializationTest {

    protected Object[] getData() {
        return new Object[] {new PrivateCredentialPermission("a.b.Cred a.c.Princ \"duke\"", "read")};
    }
}