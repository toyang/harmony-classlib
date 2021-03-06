/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.harmony.lang.management;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryManagerMXBean;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;

import org.apache.harmony.lang.management.DynamicMXBeanImpl;
import org.apache.harmony.lang.management.GarbageCollectorMXBeanImpl;
import org.apache.harmony.lang.management.MemoryManagerMXBeanImpl;


public class MemoryManagerImplTest extends MultiInstanceDynamicMXBeanImplTestBase {

    protected void setUp() throws Exception {
        super.setUp();
        List<MemoryManagerMXBean> allBeans = ManagementFactory
                .getMemoryManagerMXBeans();
        mbList = new ArrayList<DynamicMXBeanImpl>();
        for (MemoryManagerMXBean bean : allBeans) {
            mbList.add((MemoryManagerMXBeanImpl) bean);
        }// end for
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // -----------------------------------------------------------------
    // DynamicMBean behaviour tests follow ....
    // -----------------------------------------------------------------
    public final void testGetAttributes() throws Exception {
        for (DynamicMXBeanImpl mb : mbList) {
            AttributeList attributes = mb.getAttributes(attribs.keySet()
                    .toArray(new String[] {}));
            assertNotNull(attributes);
            assertTrue(attributes.size() == attribs.size());

            // Check through the returned values
            Iterator<?> it = attributes.iterator();
            while (it.hasNext()) {
                Attribute element = (Attribute) it.next();
                assertNotNull(element);
                String name = element.getName();
                Object value = element.getValue();
                if (name.equals("Valid")) {
                    // This could be true or false - just so long as we don't
                    // get an exception raised...
                    boolean validVal = ((Boolean) value).booleanValue();
                } else if (name.equals("Name")) {
                    assertNotNull(value);
                    assertTrue(value instanceof String);
                    assertTrue(((String) value).length() > 0);
                } else if (name.equals("MemoryPoolNames")) {
                    assertNotNull(value);
                    assertTrue(value instanceof String[]);
                    String[] strVals = (String[]) value;
                    for (int i = 0; i < strVals.length; i++) {
                        String poolName = strVals[i];
                        assertNotNull(poolName);
                        assertTrue(poolName.length() > 0);
                    }// end for
                }
            }// end while
        }// end for
    }

    public final void testSetAttributes() {
        AttributeList badList = new AttributeList();
        Attribute garbage = new Attribute("Name", "Bez");
        badList.add(garbage);
        for (DynamicMXBeanImpl mb : mbList) {
            // No writable attributes for this type
            AttributeList setAttrs = mb.setAttributes(badList);
            assertNotNull(setAttrs);
            assertTrue(setAttrs.size() == 0);
        }
    }

    public final void testGetMBeanInfo() {
        for (DynamicMXBeanImpl mb : mbList) {
            MBeanInfo mbi = mb.getMBeanInfo();
            assertNotNull(mbi);

            // Now make sure that what we got back is what we expected.

            // Class name
            assertTrue(mbi.getClassName().equals(mb.getClass().getName()));

            // No public constructors
            MBeanConstructorInfo[] constructors = mbi.getConstructors();
            assertNotNull(constructors);
            assertTrue(constructors.length == 0);

            // No public operations
            MBeanOperationInfo[] operations = mbi.getOperations();
            assertNotNull(operations);
            assertTrue(operations.length == 0);

            // No notifications
            MBeanNotificationInfo[] notifications = mbi.getNotifications();
            assertNotNull(notifications);
            assertTrue(notifications.length == 0);

            // Description is just the class name (until I hear it should be
            // different)
            assertTrue(mbi.getDescription().equals(mb.getClass().getName()));

            // The number of attributes depends on what kind of
            // MemoryManagerMXBean we have.
            MBeanAttributeInfo[] attributes = mbi.getAttributes();
            assertNotNull(attributes);
            String runtimeType = mb.getClass().getName();
            if (runtimeType.equals(MemoryManagerMXBeanImpl.class.getName())) {
                assertEquals(3, attributes.length);
                validateMemoryManagerAttributes(attributes);
            } else if (runtimeType.equals(GarbageCollectorMXBeanImpl.class
                    .getName())) {
                assertEquals(5, attributes.length);
                validateGCAttributes(attributes);
            } else {
                fail("Unexpected kind of memory manager MXBean : "
                        + runtimeType);
            }
        }// end for
    }

    /**
     * @param attributes
     */
    private void validateMemoryManagerAttributes(MBeanAttributeInfo[] attributes) {
        for (int i = 0; i < attributes.length; i++) {
            MBeanAttributeInfo info = attributes[i];
            assertNotNull(info);
            validateAttributeInfo(info);
        }// end for
    }

    /**
     * @param attributes
     */
    private void validateGCAttributes(MBeanAttributeInfo[] attributes) {
        GarbageCollectorMXBeanImplTest gcTest = new GarbageCollectorMXBeanImplTest();
        for (int i = 0; i < attributes.length; i++) {
            MBeanAttributeInfo info = attributes[i];
            assertNotNull(info);
            gcTest.validateAttributeInfo(info);
        }// end for
    }

    public final void testGetAttribute() throws Exception {
        for (DynamicMXBeanImpl mb : mbList) {
            // The good attributes...
            assertTrue(mb.getAttribute("MemoryPoolNames") instanceof String[]);
            String[] arr = (String[]) mb.getAttribute("MemoryPoolNames");
            for (int i = 0; i < arr.length; i++) {
                String element = arr[i];
                assertNotNull(element);
                assertTrue(element.length() > 0);
            }// end for

            assertTrue(mb.getAttribute("Name") instanceof String);
            assertTrue(((String) mb.getAttribute("Name")).length() > 0);

            // This could be true or false - just so long as we don't get an
            // exception raised...
            boolean validVal = ((Boolean) (mb.getAttribute("Valid")));

            // A nonexistent attribute should throw an
            // AttributeNotFoundException
            try {
                long rpm = ((Long) (mb.getAttribute("RPM")));
                fail("Should have thrown an AttributeNotFoundException.");
            } catch (AttributeNotFoundException ignore) {
            }

            // Type mismatch should result in a casting exception
            try {
                String bad = (String) (mb.getAttribute("MemoryPoolNames"));
                fail("Should have thrown a ClassCastException");
            } catch (ClassCastException ignore) {
            }
        }// end for
    }

    public final void testSetAttribute() throws Exception {
        for (DynamicMXBeanImpl mb : mbList) {
            // Let's try and set some non-writable attributes.
            Attribute attr = new Attribute("Name", "Dando");
            try {
                mb.setAttribute(attr);
                fail("Should have thrown an AttributeNotFoundException.");
            } catch (AttributeNotFoundException ignore) {
            }

            attr = new Attribute("Valid", new Boolean(true));
            try {
                mb.setAttribute(attr);
                fail("Should have thrown an AttributeNotFoundException.");
            } catch (AttributeNotFoundException ignore) {
            }

            attr = new Attribute("MemoryPoolNames", new String[] { "X", "Y",
                    "Z" });
            try {
                mb.setAttribute(attr);
                fail("Should have thrown an AttributeNotFoundException.");
            } catch (AttributeNotFoundException ignore) {
            }
        }// end for
    }

    @Override
    protected void populateTestAttributes() {
        attribs = new Hashtable<String, AttributeData>();
        attribs.put("MemoryPoolNames", new AttributeData("[Ljava.lang.String;",
                true, false, false));
        attribs.put("Name", new AttributeData(String.class.getName(), true,
                false, false));
        attribs.put("Valid", new AttributeData(Boolean.TYPE.getName(), true,
                false, true));
    }
}
