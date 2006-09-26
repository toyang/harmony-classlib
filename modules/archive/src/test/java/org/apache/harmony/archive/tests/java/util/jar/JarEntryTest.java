/* Copyright 1998, 2005 The Apache Software Foundation or its licensors, as applicable
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
package org.apache.harmony.archive.tests.java.util.jar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.CodeSigner;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import tests.support.resource.Support_Resources;

public class JarEntryTest extends junit.framework.TestCase {

	java.util.zip.ZipEntry zipEntry;

	java.util.jar.JarEntry jarEntry;

	java.util.jar.JarFile jarFile;

	final String jarName = "hyts_patch.jar";

	final String entryName = "foo/bar/A.class";

	final String entryName2 = "Blah.txt";

	final String attJarName = "hyts_att.jar";

	final String attEntryName = "HasAttributes.txt";

	final String attEntryName2 = "NoAttributes.txt";

	File resources;

	/**
	 * @tests java.util.jar.JarEntry#JarEntry(java.util.zip.ZipEntry)
	 */
	public void test_ConstructorLjava_util_zip_ZipEntry() {
		// Test for method java.util.jar.JarEntry(java.util.zip.ZipEntry)
		assertNotNull("Jar file is null", jarFile);
		zipEntry = jarFile.getEntry(entryName);
		assertNotNull("Zip entry is null", zipEntry);
		jarEntry = new JarEntry(zipEntry);
		assertNotNull("Jar entry is null", jarEntry);
		assertTrue("Wrong entry constucted--wrong name", jarEntry.getName()
				.equals(entryName));
		assertEquals("Wrong entry constucted--wrong size",
				311, jarEntry.getSize());
	}

	/**
	 * @tests java.util.jar.JarEntry#getAttributes()
	 */
	public void test_getAttributes() {
		// Test for method java.util.jar.Attributes
		// java.util.jar.JarEntry.getAttributes()
		JarFile attrJar = null;
		File file = null;
		try {
			Support_Resources.copyFile(resources, null, attJarName);
			file = new File(resources, attJarName);
			attrJar = new JarFile(file);
		} catch (Exception e) {
			assertTrue(file + " does not exist", file.exists());
			fail("Exception opening file: " + e.toString());
		}

		try {
			jarEntry = attrJar.getJarEntry(attEntryName);
			assertNotNull("Should have Manifest attributes", jarEntry
					.getAttributes());
		} catch (Exception e) {
			fail("Exception during 2nd test: " + e.toString());
		}

		try {
			jarEntry = attrJar.getJarEntry(attEntryName2);
			assertNull("Shouldn't have any Manifest attributes", jarEntry
					.getAttributes());
			attrJar.close();
		} catch (Exception e) {
			fail("Exception during 1st test: " + e.toString());
		}
	}

	/**
	 * @tests java.util.jar.JarEntry#getCertificates()
	 */
	public void test_getCertificates() {
		// Test for method java.security.cert.Certificate []
		// java.util.jar.JarEntry.getCertificates()
		zipEntry = jarFile.getEntry(entryName2);
		jarEntry = new JarEntry(zipEntry);
		assertNull("Shouldn't have any Certificates", jarEntry
				.getCertificates());
	}

    
	/**
	 * @tests java.util.jar.JarEntry#getCodeSigners()
	 */
	public void test_getCodeSigners() throws IOException {
	    String jarFileName = "TestCodeSigners.jar";
	    Support_Resources.copyFile(resources, null, jarFileName);
	    File file = new File(resources, jarFileName);
	    JarFile jarFile = new JarFile(file);
	    JarEntry jarEntry = jarFile.getJarEntry("Test.class");
	    InputStream in = jarFile.getInputStream(jarEntry);
	    byte[] buffer = new byte[1024];
	    while(in.available()>0)
	    {
	        in.read(buffer);
	    }
	    CodeSigner[] codeSigners = jarEntry.getCodeSigners();
	    assertEquals(2, codeSigners.length);
	    List<?> certs_bob = codeSigners[0].getSignerCertPath().getCertificates();
	    List<?> certs_alice = codeSigners[1].getSignerCertPath().getCertificates();
	    if(1 == certs_bob.size())
	    {
	        List<?> temp = certs_bob;
	        certs_bob = certs_alice;
	        certs_alice = temp;
	    }
	    assertEquals(2,certs_bob.size());
	    assertEquals(1,certs_alice.size());
            
            assertNull("getCodeSigners() of a primitive JarEntry should return null", 
                 new JarEntry("aaa").getCodeSigners());
	}    
    
	/**
	 * Sets up the fixture, for example, open a network connection. This method
	 * is called before a test is executed.
	 */

	@Override
    protected void setUp() {
		try {
			resources = Support_Resources.createTempFolder();
			Support_Resources.copyFile(resources, null, jarName);
			jarFile = new JarFile(new File(resources, jarName));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception during setup: " + e.toString());
		}
	}

	/**
	 * Tears down the fixture, for example, close a network connection. This
	 * method is called after a test is executed.
	 */

	@Override
    protected void tearDown() {
		try {
			if (jarFile != null) {
                jarFile.close();
            }
		} catch (java.io.IOException e) {
			System.out.println("Exception during tearDown");
		}
	}

}
