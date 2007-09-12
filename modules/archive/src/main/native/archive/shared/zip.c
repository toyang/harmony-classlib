/* 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "iohelp.h"
#include "exceptions.h"
#include "jclglob.h"
#include "jclprots.h"
#include "zip.h"

void zfree PROTOTYPE ((void *opaque, void *address));
void *zalloc PROTOTYPE ((void *opaque, U_32 items, U_32 size));

/**
  * Throw java.lang.InternalError
  */
void
throwNewInternalError (JNIEnv * env, const char *message)
{
  throwNewExceptionByName(env, "java/lang/InternalError", message);
}

/**
  * Throw java.util.zip.ZipException with the message provided
  */
void
throwJavaZIOException (JNIEnv * env, const char *message)
{
  throwNewExceptionByName(env, "java/util/zip/ZipException", message);
}

JNIEXPORT jint JNICALL
Java_java_util_zip_ZipFile_openZipImpl (JNIEnv * env, jobject recv,
                                        jbyteArray zipName)
{
  VMI_ACCESS_FROM_ENV (env);
  PORT_ACCESS_FROM_ENV (env);

  I_32 retval;
  JCLZipFile *jclZipFile;
  JCLZipFileLink *zipfileHandles;
  jsize length;
  char pathCopy[HyMaxPath];
#ifndef HY_ZIP_API
  HyZipCachePool *zipCachePool;
#else /* HY_ZIP_API */
  HyZipFunctionTable *zipFuncs;
#endif /* HY_ZIP_API */

  jclZipFile = jclmem_allocate_memory (env, sizeof (*jclZipFile));
  if (!jclZipFile)
    return 3;

  length = (*env)->GetArrayLength (env, zipName);
  length = length < HyMaxPath - 1 ? length : HyMaxPath - 1;
  ((*env)->GetByteArrayRegion (env, zipName, 0, length, pathCopy));
  pathCopy[length++] = '\0';
  ioh_convertToPlatform (pathCopy);

#ifndef HY_ZIP_API
  /* Open the zip file (caching will be managed automatically by zipsup) */
  zipCachePool = (*VMI)->GetZipCachePool (VMI);
  retval =
    zip_openZipFile (privatePortLibrary, pathCopy, &(jclZipFile->hyZipFile),
                     zipCachePool);
#else /* HY_ZIP_API */
  /* Open the zip file (caching will be managed automatically) */
  zipFuncs = (*VMI)->GetZipFunctions(VMI);
  retval = zipFuncs->zip_openZipFile(VMI, pathCopy, &(jclZipFile->hyZipFile));
#endif /* HY_ZIP_API */

  if (retval)
    {
      jclmem_free_memory (env, jclZipFile);     /* free on fail */

      if (retval == ZIP_ERR_FILE_OPEN_ERROR)
        return 1;
      else
        return 2;
    }

  /* Add the zipFile we just allocated to the list of zip files -- we will
   * free this on UnLoad if its not already free'd.
   */
  zipfileHandles = JCL_CACHE_GET (env, zipfile_handles);
  MUTEX_ENTER (zipfileHandles->mutex);
  jclZipFile->last = (JCLZipFile *) zipfileHandles;
  jclZipFile->next = zipfileHandles->next;
  if (zipfileHandles->next != NULL)
    zipfileHandles->next->last = jclZipFile;
  zipfileHandles->next = jclZipFile;
  MUTEX_EXIT (zipfileHandles->mutex);

  (*env)->SetLongField (env, recv,
                        JCL_CACHE_GET (env,
                                       FID_java_util_zip_ZipFile_descriptor),
			            ((IDATA) jclZipFile));
  return 0;
}

JNIEXPORT jobject JNICALL
Java_java_util_zip_ZipFile_getEntryImpl (JNIEnv * env, jobject recv,
                                         jlong zipPointer, jstring entryName)
{
#ifdef HY_ZIP_API
  VMI_ACCESS_FROM_ENV(env);
#endif /* HY_ZIP_API */
  PORT_ACCESS_FROM_ENV (env);

  I_32 retval;
  I_32 extraval;
  HyZipFile *zipFile;
  HyZipEntry zipEntry;
  jobject java_ZipEntry, extra;
  jclass entryClass;
  jmethodID mid;
  const char *entryCopy;
  JCLZipFile *jclZipFile = (JCLZipFile *) (IDATA) zipPointer;
#ifdef HY_ZIP_API
  HyZipFunctionTable *zipFuncs = (*VMI)->GetZipFunctions(VMI);
#endif /* HY_ZIP_API */

  if (jclZipFile == (void *) -1)
    {
      throwNewIllegalStateException (env, "");
      return NULL;
    }
  zipFile = &(jclZipFile->hyZipFile);
  entryCopy = (*env)->GetStringUTFChars (env, entryName, NULL);
  if (entryCopy == NULL)
    return (jobject) NULL;

#ifndef HY_ZIP_API
  zip_initZipEntry (PORTLIB, &zipEntry);
  retval = zip_getZipEntry (PORTLIB, zipFile, &zipEntry, entryCopy, TRUE);
#else /* HY_ZIP_API */
  zipFuncs->zip_initZipEntry (VMI, &zipEntry);
  retval = zipFuncs->zip_getZipEntry (VMI, zipFile, &zipEntry, entryCopy, TRUE);
#endif /* HY_ZIP_API */
  (*env)->ReleaseStringUTFChars (env, entryName, entryCopy);
  if (retval)
    {
#ifndef HY_ZIP_API
      zip_freeZipEntry (PORTLIB, &zipEntry);
#else /* HY_ZIP_API */
	  zipFuncs->zip_freeZipEntry (VMI, &zipEntry);
#endif /* HY_ZIP_API */
      return (jobject) NULL;
    }

  extra = NULL;
  if (zipEntry.extraFieldLength > 0)
    {
      extraval =
#ifndef HY_ZIP_API
        zip_getZipEntryExtraField (PORTLIB, zipFile, &zipEntry, NULL,
#else /* HY_ZIP_API */
	    zipFuncs->zip_getZipEntryExtraField (VMI, zipFile, &zipEntry, NULL,
#endif /* HY_ZIP_API */
                                 zipEntry.extraFieldLength);
      if (extraval || zipEntry.extraField == NULL)
        {
#ifndef HY_ZIP_API
          zip_freeZipEntry (PORTLIB, &zipEntry);
#else /* HY_ZIP_API */
    	  zipFuncs->zip_freeZipEntry (VMI, &zipEntry);
#endif /* HY_ZIP_API */
          if (extraval)
            {
              char buf[50];
              sprintf (buf, "Error %d getting extra field of zip entry", extraval);
              throwNewInternalError (env, buf);
            }
          return (jobject) NULL;
        }
      extra = ((*env)->NewByteArray (env, zipEntry.extraFieldLength));
      if (((*env)->ExceptionCheck (env)))
        {
#ifndef HY_ZIP_API
          zip_freeZipEntry (PORTLIB, &zipEntry);
#else /* HY_ZIP_API */
    	  zipFuncs->zip_freeZipEntry (VMI, &zipEntry);
#endif /* HY_ZIP_API */
          return (jobject) NULL;
        }
      ((*env)->
       SetByteArrayRegion (env, extra, 0, zipEntry.extraFieldLength,
                           zipEntry.extraField));
    }

  entryClass = JCL_CACHE_GET (env, CLS_java_util_zip_ZipEntry);
  entryClass = (*env)->NewLocalRef(env, entryClass);
  if (entryClass == NULL) {
#ifndef HY_ZIP_API
    zip_freeZipEntry (PORTLIB, &zipEntry);
#else /* HY_ZIP_API */
    zipFuncs->zip_freeZipEntry (VMI, &zipEntry);
#endif /* HY_ZIP_API */
    return (jobject) NULL;
  }
  mid = JCL_CACHE_GET (env, MID_java_util_zip_ZipEntry_init);
  /* Build a new ZipEntry from the C struct */
  java_ZipEntry = ((*env)->NewObject (env, entryClass, mid, entryName, NULL,
                                      extra,
                                      (jlong) zipEntry.lastModTime,
                                      (jlong) zipEntry.uncompressedSize,
                                      (jlong) zipEntry.compressedSize,
                                      (jlong) zipEntry.crc32,
                                      zipEntry.compressionMethod,
                                      (jlong) zipEntry.lastModDate,
                                      (jlong) zipEntry.dataPointer));
#ifndef HY_ZIP_API
  zip_freeZipEntry (PORTLIB, &zipEntry);
#else /* HY_ZIP_API */
  zipFuncs->zip_freeZipEntry (VMI, &zipEntry);
#endif /* HY_ZIP_API */
  return java_ZipEntry;
}

JNIEXPORT void JNICALL
Java_java_util_zip_ZipFile_closeZipImpl (JNIEnv * env, jobject recv, jlong zipPointer)
{
  PORT_ACCESS_FROM_ENV (env);
#ifdef HY_ZIP_API
  VMI_ACCESS_FROM_ENV(env);
#endif /* HY_ZIP_API */

  I_32 retval = 0;
  jfieldID descriptorFID;
  JCLZipFileLink *zipfileHandles;
  JCLZipFile *jclZipFile = (JCLZipFile *) (IDATA) zipPointer;
#ifdef HY_ZIP_API
  HyZipFunctionTable *zipFuncs = (*VMI)->GetZipFunctions(VMI);
#endif /* HY_ZIP_API */

  if (jclZipFile != (void *) -1)
    {
      retval =
#ifndef HY_ZIP_API
        zip_closeZipFile (privatePortLibrary, &(jclZipFile->hyZipFile));
#else /* HY_ZIP_API */
        zipFuncs->zip_closeZipFile (VMI, &(jclZipFile->hyZipFile));
#endif /* HY_ZIP_API */
      descriptorFID = JCL_CACHE_GET (env, FID_java_util_zip_ZipFile_descriptor);
      (*env)->SetLongField (env, recv, descriptorFID, -1);

      /* Free the zip struct */
	  zipfileHandles = JCL_CACHE_GET (env, zipfile_handles);
      MUTEX_ENTER (zipfileHandles->mutex);
      if (jclZipFile->last != NULL)
        jclZipFile->last->next = jclZipFile->next;
      if (jclZipFile->next != NULL)
        jclZipFile->next->last = jclZipFile->last;
      MUTEX_EXIT (zipfileHandles->mutex);

      jclmem_free_memory (env, jclZipFile);
      if (retval)
        {
          throwJavaZIOException (env, "");
          return;
        }
    }
}

/**
  * Throw java.lang.IllegalStateException
  */
void
throwNewIllegalStateException (JNIEnv * env, const char *message)
{
  throwNewExceptionByName(env, "java/lang/IllegalStateException", message);
}

/**
  * Throw java.lang.IllegalArgumentException
  */
void
throwNewIllegalArgumentException (JNIEnv * env, const char *message)
{
  throwNewExceptionByName(env, "java/lang/IllegalArgumentException", message);
}

JNIEXPORT void JNICALL
Java_java_util_zip_ZipFile_ntvinit (JNIEnv * env, jclass cls)
{
  PORT_ACCESS_FROM_ENV (env);
  jmethodID mid;
  jfieldID descriptorFID;
  jclass javaClass;
  jclass weakJavaClass;
  JCLZipFileLink *zipfileHandles;

  javaClass = (*env)->FindClass (env, "java/util/zip/ZipEntry");
  weakJavaClass = (*env)->NewGlobalRef (env, javaClass);
  if (!weakJavaClass)
    return;
  mid =
    ((*env)->
     GetMethodID (env, javaClass, "<init>",
                  "(Ljava/lang/String;Ljava/lang/String;[BJJJJIJJ)V"));
  if (!mid)
    return;
  JCL_CACHE_SET (env, CLS_java_util_zip_ZipEntry, weakJavaClass);
  JCL_CACHE_SET (env, MID_java_util_zip_ZipEntry_init, mid);

  descriptorFID = (*env)->GetFieldID (env, cls, "descriptor", "J");
  if (!descriptorFID)
    return;
  JCL_CACHE_SET (env, FID_java_util_zip_ZipFile_descriptor, descriptorFID);

  javaClass = (*env)->FindClass (env, "java/util/zip/ZipFile$ZFEnum");
  if (!javaClass)
    return;
  descriptorFID =
    (*env)->GetFieldID (env, javaClass, "nextEntryPointer", "J");
  if (!descriptorFID)
    return;
  JCL_CACHE_SET (env, FID_java_util_zip_ZipFile_nextEntryPointer,
                 descriptorFID);

  zipfileHandles = jclmem_allocate_memory (env, sizeof (JCLZipFileLink));
  if (!zipfileHandles)
    return;
  zipfileHandles->last = NULL;
  zipfileHandles->next = NULL;
  MUTEX_INIT (zipfileHandles->mutex);
  JCL_CACHE_SET (env, zipfile_handles, zipfileHandles);
}

JNIEXPORT jlong JNICALL
Java_java_util_zip_ZipFile_00024ZFEnum_resetZip (JNIEnv * env, jobject recv,
                                                 jlong descriptor)
{
  PORT_ACCESS_FROM_ENV (env);
#ifdef HY_ZIP_API
  VMI_ACCESS_FROM_ENV(env);
#endif /* HY_ZIP_API */

  IDATA nextEntryPointer;
  JCLZipFile *jclZipFile = (JCLZipFile *) (IDATA) descriptor;
#ifdef HY_ZIP_API
  HyZipFunctionTable *zipFuncs = (*VMI)->GetZipFunctions(VMI);
#endif /* HY_ZIP_API */

  if (jclZipFile == (void *) -1)
    {
      throwNewIllegalStateException (env, "");
      return 0;
    }
#ifndef HY_ZIP_API
  zip_resetZipFile (privatePortLibrary,
#else /* HY_ZIP_API */
  zipFuncs->zip_resetZipFile (VMI,
#endif /* HY_ZIP_API */
		    &(jclZipFile->hyZipFile), 
                    &nextEntryPointer);
  return nextEntryPointer;
}

JNIEXPORT jobject JNICALL
Java_java_util_zip_ZipFile_00024ZFEnum_getNextEntry (JNIEnv * env,
                                                     jobject recv,
                                                     jlong descriptor,
                                                     jlong nextEntry)
{
  PORT_ACCESS_FROM_ENV (env);
#ifdef HY_ZIP_API
  VMI_ACCESS_FROM_ENV(env);
#endif /* HY_ZIP_API */

  I_32 retval;
  I_32 extraval;
  HyZipFile *zipFile;
  HyZipEntry zipEntry;
  jobject java_ZipEntry, extra;
  jclass javaClass;
  jmethodID mid;
  jstring entryName = NULL;
  IDATA nextEntryPointer;
  JCLZipFile *jclZipFile = (JCLZipFile *) (IDATA) descriptor;
#ifdef HY_ZIP_API
  HyZipFunctionTable *zipFuncs = (*VMI)->GetZipFunctions(VMI);
#endif /* HY_ZIP_API */

  if (jclZipFile == (void *) -1)
    {
      throwNewIllegalStateException (env, "");
      return NULL;
    }
  zipFile = &(jclZipFile->hyZipFile);
#ifndef HY_ZIP_API
  zip_initZipEntry (PORTLIB, &zipEntry);
#else /* HY_ZIP_API */
  zipFuncs->zip_initZipEntry (VMI, &zipEntry);
#endif /* HY_ZIP_API */

  nextEntryPointer = (IDATA) nextEntry;
  retval =
#ifndef HY_ZIP_API
    zip_getNextZipEntry (PORTLIB, zipFile, &zipEntry, &nextEntryPointer);
#else /* HY_ZIP_API */
    zipFuncs->zip_getNextZipEntry (VMI, zipFile, &zipEntry, &nextEntryPointer);
#endif /* HY_ZIP_API */
  if (retval)
    {
      if (retval != ZIP_ERR_NO_MORE_ENTRIES)
        {
          char buf[40];
          sprintf (buf, "Error %d getting next zip entry", retval);
          throwNewInternalError (env, buf);
        }
      return (jobject) NULL;
    }

  /* Build a new ZipEntry from the C struct */
  entryName = ((*env)->NewStringUTF (env, zipEntry.filename));

  if (((*env)->ExceptionCheck (env)))
    return NULL;

  extra = NULL;
  if (zipEntry.extraFieldLength > 0)
    {
      extraval =
#ifndef HY_ZIP_API
        zip_getZipEntryExtraField (PORTLIB, zipFile, &zipEntry, NULL,
#else /* HY_ZIP_API */
        zipFuncs->zip_getZipEntryExtraField (VMI, zipFile, &zipEntry, NULL,
#endif /* HY_ZIP_API */
                                 zipEntry.extraFieldLength);
      if (extraval || zipEntry.extraField == NULL)
        {
#ifndef HY_ZIP_API
          zip_freeZipEntry (PORTLIB, &zipEntry);
#else /* HY_ZIP_API */
    	  zipFuncs->zip_freeZipEntry (VMI, &zipEntry);
#endif /* HY_ZIP_API */
          if (extraval)
            {
              char buf[50];
              sprintf (buf, "Error %d getting extra field of zip entry", extraval);
              throwNewInternalError (env, buf);
            }
          return (jobject) NULL;
        }
      extra = ((*env)->NewByteArray (env, zipEntry.extraFieldLength));
      if (((*env)->ExceptionCheck (env)))
        {
          /* free the extraField entry */
#ifndef HY_ZIP_API
          zip_freeZipEntry (PORTLIB, &zipEntry);
#else /* HY_ZIP_API */
          zipFuncs->zip_freeZipEntry (VMI, &zipEntry); //not valid zipEntry (-1)
#endif /* HY_ZIP_API */
          return NULL;
        }
      ((*env)->
       SetByteArrayRegion (env, extra, 0, zipEntry.extraFieldLength,
                           zipEntry.extraField));
      jclmem_free_memory (env, zipEntry.extraField);
      zipEntry.extraField = NULL;
    }

  javaClass = JCL_CACHE_GET (env, CLS_java_util_zip_ZipEntry);
  javaClass = (*env)->NewLocalRef(env, javaClass);
  if (javaClass == NULL) {
      return NULL;
  }
  mid = JCL_CACHE_GET (env, MID_java_util_zip_ZipEntry_init);
  java_ZipEntry = ((*env)->NewObject (env, javaClass, mid, entryName, NULL,     /* comment */
                                      extra,
                                      (jlong) zipEntry.lastModTime,
                                      (jlong) zipEntry.uncompressedSize,
                                      (jlong) zipEntry.compressedSize,
                                      (jlong) zipEntry.crc32,
                                      zipEntry.compressionMethod,
                                      (jlong) zipEntry.lastModDate,
                                      (jlong) zipEntry.dataPointer));
#ifndef HY_ZIP_API
  zip_freeZipEntry (PORTLIB, &zipEntry);
#else /* HY_ZIP_API */
  zipFuncs->zip_freeZipEntry (VMI, &zipEntry);
#endif /* HY_ZIP_API */
  (*env)->SetLongField (env, recv,
                        JCL_CACHE_GET (env,
                                       FID_java_util_zip_ZipFile_nextEntryPointer),
                        nextEntryPointer);
  return java_ZipEntry;
}

JNIEXPORT jbyteArray JNICALL
Java_java_util_zip_ZipFile_inflateEntryImpl2 (JNIEnv * env, jobject recv,
					                          jlong descriptor,
                                              jstring entryName)
{
  PORT_ACCESS_FROM_ENV (env);
#ifdef HY_ZIP_API
  VMI_ACCESS_FROM_ENV(env);
#endif /* HY_ZIP_API */

  I_32 retval;
  HyZipFile *zipFile;
  HyZipEntry zipEntry;
  const char *entryCopy;
  jbyteArray buf;
  JCLZipFile *jclZipFile = (JCLZipFile *) (IDATA) descriptor;
#ifdef HY_ZIP_API
  HyZipFunctionTable *zipFuncs = (*VMI)->GetZipFunctions(VMI);
#endif /* HY_ZIP_API */

  /* Build the zipFile */
  if (jclZipFile == (void *) -1)
    {
      throwNewIllegalStateException (env, "");
      return NULL;
    }
  zipFile = &(jclZipFile->hyZipFile);
  entryCopy = (*env)->GetStringUTFChars (env, entryName, NULL);
  if (entryCopy == NULL)
    return NULL;

#ifndef HY_ZIP_API
  zip_initZipEntry (privatePortLibrary, &zipEntry);
#else /* HY_ZIP_API */
  zipFuncs->zip_initZipEntry (VMI, &zipEntry);
#endif /* HY_ZIP_API */
  retval =
#ifndef HY_ZIP_API
    zip_getZipEntry (privatePortLibrary, zipFile, &zipEntry, entryCopy, TRUE);
#else /* HY_ZIP_API */
    zipFuncs->zip_getZipEntry (VMI, zipFile, &zipEntry, entryCopy, TRUE);
#endif /* HY_ZIP_API */
  (*env)->ReleaseStringUTFChars (env, entryName, entryCopy);
  if (retval)
    {
#ifndef HY_ZIP_API
      zip_freeZipEntry (privatePortLibrary, &zipEntry);
#else /* HY_ZIP_API */
      zipFuncs->zip_freeZipEntry (VMI, &zipEntry);
#endif /* HY_ZIP_API */
      if (retval == ZIP_ERR_OUT_OF_MEMORY)
        throwNewOutOfMemoryError (env, "");
      return NULL;
    }

  buf = (*env)->NewByteArray (env, zipEntry.uncompressedSize);
  if (!buf)
    {
      throwNewOutOfMemoryError (env, "");
      return NULL;
    }

  retval =
#ifndef HY_ZIP_API
    zip_getZipEntryData (privatePortLibrary, zipFile, &zipEntry, NULL,
#else /* HY_ZIP_API */
    zipFuncs->zip_getZipEntryData (VMI, zipFile, &zipEntry, NULL,
#endif /* HY_ZIP_API */
                         zipEntry.uncompressedSize);
  if (retval == 0)
    (*env)->SetByteArrayRegion (env, buf, 0, zipEntry.uncompressedSize,
                                zipEntry.data);
#ifndef HY_ZIP_API
  zip_freeZipEntry (privatePortLibrary, &zipEntry);
#else /* HY_ZIP_API */
  zipFuncs->zip_freeZipEntry (VMI, &zipEntry);
#endif /* HY_ZIP_API */
  if (!retval)
    return buf;

  if (retval == ZIP_ERR_OUT_OF_MEMORY)
    throwNewOutOfMemoryError (env, "");
  else
    throwJavaZIOException (env, "");

  return NULL;
}
