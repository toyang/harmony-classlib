/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
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
* @author Boris V. Kuznetsov
* @version $Revision$
*/

package javax.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 * @com.intel.drl.spec_ref
 * 
 */

public abstract class ServerSocketFactory {
    static ServerSocketFactory defaultFactory;
    protected ServerSocketFactory() {
    }

    public static synchronized ServerSocketFactory getDefault() {
        if (defaultFactory == null) {
                defaultFactory = new DefaultServerSocketFactory();
        }
        return defaultFactory;
    }

    public ServerSocket createServerSocket() throws IOException {
        throw new SocketException("Unbound server socket: not implemented");
    }

    public abstract ServerSocket createServerSocket(int port)
            throws IOException;

    public abstract ServerSocket createServerSocket(int port, int backlog)
            throws IOException;

    public abstract ServerSocket createServerSocket(int port, int backlog,
            InetAddress iAddress) throws IOException;

}