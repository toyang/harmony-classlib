/*
 * THE FILE HAS BEEN AUTOGENERATED BY THE IJH TOOL.
 * Please be aware that all changes made to this file manually
 * will be overwritten by the tool if it runs again.
 */

#include <jni.h>


/* Header for class org.apache.harmony.niochar.charset.CP_1251 */

#ifndef _ORG_APACHE_HARMONY_NIOCHAR_CHARSET_CP_1251_H
#define _ORG_APACHE_HARMONY_NIOCHAR_CHARSET_CP_1251_H

#ifdef __cplusplus
extern "C" {
#endif


#ifdef __cplusplus
}
#endif

#endif /* _ORG_APACHE_HARMONY_NIOCHAR_CHARSET_CP_1251_H */


/* Header for class org.apache.harmony.niochar.charset.CP_1251$Decoder */

#ifndef _ORG_APACHE_HARMONY_NIOCHAR_CHARSET_CP_1251_DECODER_H
#define _ORG_APACHE_HARMONY_NIOCHAR_CHARSET_CP_1251_DECODER_H

#ifdef __cplusplus
extern "C" {
#endif


/* Static final fields */

#undef org_apache_harmony_niochar_charset_CP_1251_Decoder_INIT
#define org_apache_harmony_niochar_charset_CP_1251_Decoder_INIT 0L

#undef org_apache_harmony_niochar_charset_CP_1251_Decoder_ONGOING
#define org_apache_harmony_niochar_charset_CP_1251_Decoder_ONGOING 1L

#undef org_apache_harmony_niochar_charset_CP_1251_Decoder_END
#define org_apache_harmony_niochar_charset_CP_1251_Decoder_END 2L

#undef org_apache_harmony_niochar_charset_CP_1251_Decoder_FLUSH
#define org_apache_harmony_niochar_charset_CP_1251_Decoder_FLUSH 3L


/* Native methods */

/*
 * Method: org.apache.harmony.niochar.charset.CP_1251$Decoder.nDecode([CIIJI)I
 */
JNIEXPORT jint JNICALL
Java_org_apache_harmony_niochar_charset_CP_11251_00024Decoder_nDecode(JNIEnv *, jobject, 
    jcharArray, jint, jint, jlong, jint);


#ifdef __cplusplus
}
#endif

#endif /* _ORG_APACHE_HARMONY_NIOCHAR_CHARSET_CP_1251_DECODER_H */


/* Header for class org.apache.harmony.niochar.charset.CP_1251$Encoder */

#ifndef _ORG_APACHE_HARMONY_NIOCHAR_CHARSET_CP_1251_ENCODER_H
#define _ORG_APACHE_HARMONY_NIOCHAR_CHARSET_CP_1251_ENCODER_H

#ifdef __cplusplus
extern "C" {
#endif


/* Static final fields */

#undef org_apache_harmony_niochar_charset_CP_1251_Encoder_INIT
#define org_apache_harmony_niochar_charset_CP_1251_Encoder_INIT 0L

#undef org_apache_harmony_niochar_charset_CP_1251_Encoder_ONGOING
#define org_apache_harmony_niochar_charset_CP_1251_Encoder_ONGOING 1L

#undef org_apache_harmony_niochar_charset_CP_1251_Encoder_END
#define org_apache_harmony_niochar_charset_CP_1251_Encoder_END 2L

#undef org_apache_harmony_niochar_charset_CP_1251_Encoder_FLUSH
#define org_apache_harmony_niochar_charset_CP_1251_Encoder_FLUSH 3L


/* Native methods */

/*
 * Method: org.apache.harmony.niochar.charset.CP_1251$Encoder.nEncode(JI[CI[I)V
 */
JNIEXPORT void JNICALL
Java_org_apache_harmony_niochar_charset_CP_11251_00024Encoder_nEncode(JNIEnv *, jobject, 
    jlong, jint, jcharArray, jint, jintArray);


#ifdef __cplusplus
}
#endif

#endif /* _ORG_APACHE_HARMONY_NIOCHAR_CHARSET_CP_1251_ENCODER_H */

