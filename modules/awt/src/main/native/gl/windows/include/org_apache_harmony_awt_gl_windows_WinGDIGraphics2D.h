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
 * @author Alexey A. Petrenko
 */
/*
 * THE FILE HAS BEEN AUTOGENERATED BY INTEL IJH TOOL.
 * Please be aware that all changes made to this file manually
 * will be overwritten by the tool if it runs again.
 */

#include <jni.h>


/* Header for class org.apache.harmony.awt.gl.windows.WinGDIGraphics2D */

#ifndef _ORG_APACHE_HARMONY_AWT_GL_WINDOWS_WINGDIGRAPHICS2D_H
#define _ORG_APACHE_HARMONY_AWT_GL_WINDOWS_WINGDIGRAPHICS2D_H

#ifdef __cplusplus
extern "C" {
#endif


/* Static final fields */

#undef org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_debugOutput
#define org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_debugOutput 0L


/* Native methods */

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    gdiPlusStartup
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_gdiPlusStartup
  (JNIEnv *, jclass);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    gdiPlusShutdown
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_gdiPlusShutdown
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    createGraphicsInfo
 * Signature: (JIIII)J
 */
JNIEXPORT jlong JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_createGraphicsInfo
  (JNIEnv *, jobject, jlong, jint, jint, jint, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    createGraphicsInfoFor
 * Signature: (JC)J
 */
JNIEXPORT jlong JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_createGraphicsInfoFor
  (JNIEnv *, jobject, jlong, jchar);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    createCompatibleImageInfo
 * Signature: (JII)J
 */
JNIEXPORT jlong JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_createCompatibleImageInfo__JII
  (JNIEnv *, jclass, jlong, jint, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    createCompatibleImageInfo
 * Signature: ([BII)J
 */
JNIEXPORT jlong JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_createCompatibleImageInfo___3BII
  (JNIEnv *, jclass, jbyteArray, jint, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    copyImageInfo
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_copyImageInfo
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    disposeGraphicsInfo
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_disposeGraphicsInfo
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    copyArea
 * Signature: (JIIIIII)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_copyArea
  (JNIEnv *, jobject, jlong, jint, jint, jint, jint, jint, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    setSolidBrush
 * Signature: (JIIII)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_setSolidBrush
  (JNIEnv *, jobject, jlong, jint, jint, jint, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    setLinearGradientBrush
 * Signature: (JIIIIIIIIIIIIZ)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_setLinearGradientBrush
  (JNIEnv *, jobject, jlong, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jboolean);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    fillRects
 * Signature: (J[II)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_fillRects
  (JNIEnv *, jobject, jlong, jintArray, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    getDC
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_getDC
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    setPen
 * Signature: (JFIIF[FIF)Z
 */
JNIEXPORT jboolean JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_setPen
  (JNIEnv *, jobject, jlong, jfloat, jint, jint, jfloat, jfloatArray, jint, jfloat);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    deletePen
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_deletePen
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    drawShape
 * Signature: (J[FII)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_drawShape
  (JNIEnv *, jobject, jlong, jfloatArray, jint, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    fillShape
 * Signature: (J[FII)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_fillShape
  (JNIEnv *, jobject, jlong, jfloatArray, jint, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    drawLine
 * Signature: (JIIII)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_drawLine
  (JNIEnv *, jobject, jlong, jint, jint, jint, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    drawRect
 * Signature: (JIIII)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_drawRect
  (JNIEnv *, jobject, jlong, jint, jint, jint, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    drawOval
 * Signature: (JIIII)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_drawOval
  (JNIEnv *, jobject, jlong, jint, jint, jint, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    fillRect
 * Signature: (JIIII)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_fillRect
  (JNIEnv *, jobject, jlong, jint, jint, jint, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    setClip
 * Signature: (J[II)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_setClip
  (JNIEnv *, jobject, jlong, jintArray, jint);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    resetClip
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_resetClip
  (JNIEnv *, jobject, jlong);

/*
 * Class:     org_apache_harmony_awt_gl_windows_WinGDIGraphics2D
 * Method:    setNativeTransform
 * Signature: (J[D)V
 */
JNIEXPORT void JNICALL Java_org_apache_harmony_awt_gl_windows_WinGDIGraphics2D_setNativeTransform
  (JNIEnv *, jobject, jlong, jdoubleArray);

#ifdef __cplusplus
}
#endif
#endif
