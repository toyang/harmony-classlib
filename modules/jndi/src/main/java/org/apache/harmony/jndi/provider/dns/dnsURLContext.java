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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

/**
 * DNS context that is capable of serving requests with DNS URL's given as
 * names.
 * 
 * @author Alexei Zakharov
 * @version $Revision: 1.1.2.4 $
 */
public class dnsURLContext extends DNSContext {

    /**
     * Constructs new DNS URL context.
     * @param env environment
     * @throws NamingException if such exception was encountered
     */
    public dnsURLContext(Hashtable env) throws NamingException {
        super(env);
    }

    /**
     * @param name well formed DNS URL that points to some context
     * @param attrNames array of attribute identifiers
     * @return collection of attributes
     * @throws NamingException if such exception was encountered
     * @throws NullPointerException if <code>name</code> is null
     * @see DNSContext#getAttributes(String, String[])
     */
    public Attributes getAttributes(String name, String[] attrNames)
            throws NamingException
    {
        process(name);
        return super.getAttributes(new DNSName(), attrNames);
    }
    
    /**
     * @param name well formed DNS URL
     * @return retrieved collection of attributes
     * @throws NamingException if such exception was encountered
     * @throws NullPointerException if <code>name</code> is null
     * @see DNSContext#getAttributes(String) 
     */
    public Attributes getAttributes(String name) throws NamingException {
        return getAttributes(name, null);
    }
    
    /**
     * @param name well formed DNS URL
     * @return collection of <code>NameClassPair</code> 
     * @throws NamingException if such exception was encountered
     * @throws NullPointerException if <code>name</code> is null
     * @see DNSContext#list(String)
     */
    public NamingEnumeration list(String name) throws NamingException {
        process(name);
        return super.list(new DNSName());
    }

    /**
     * @param name well formed DNS URL
     * @return collection of <code>Binding</code> 
     * @throws NamingException if such exception was encountered
     * @throws NullPointerException if <code>name</code> is null
     * @see DNSContext#listBindings(String)
     */
    public NamingEnumeration listBindings(String name) throws NamingException {
        process(name);
        return super.listBindings(new DNSName());
    }

    /**
     * @param name well formed DNS URL
     * @return found object
     * @throws NamingException if such exception was encountered
     * @throws NullPointerException if <code>name</code> is null
     * @see DNSContext#lookup(String)
     */
    public Object lookup(String name) throws NamingException {
        process(name);
        return super.lookup(new DNSName());
    }

    /**
     * @param name well formed DNS URL
     * @return found object
     * @throws NamingException if such exception was encountered
     * @throws NullPointerException if <code>name</code> is null
     * @see DNSContext#lookupLink(String)
     */
    public Object lookupLink(String name) throws NamingException {
        return lookup(name);
    }

    /**
     * Service method
     * @param name DNS URL
     * @throws NamingException if was encountered
     * @throws NullPointerException if name is null
     */
    private void process(String name) throws NamingException {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        addToEnvironment(Context.PROVIDER_URL, name);
    }
}
