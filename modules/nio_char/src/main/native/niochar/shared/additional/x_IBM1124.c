#include "x_IBM1124.h"

#define jlong2addr(a, x) ((a *)((int)(x)))

JNIEXPORT void JNICALL Java_org_apache_harmony_niochar_charset_additional_x_1IBM1124_00024Encoder_nEncode
  (JNIEnv *env, jobject obj, jlong outAddr, jint absolutePos, jcharArray array, jint arrayOffset, jintArray result){

    static jboolean table[] = {
      
     0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,
     0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,0x1A,0x1B,0x1C,0x1D,0x1E,0x1F,
     0x20,0x21,0x22,0x23,0x24,0x25,0x26,0x27,0x28,0x29,0x2A,0x2B,0x2C,0x2D,0x2E,0x2F,
     0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0x3A,0x3B,0x3C,0x3D,0x3E,0x3F,
     0x40,0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4A,0x4B,0x4C,0x4D,0x4E,0x4F,
     0x50,0x51,0x52,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0x5B,0x5C,0x5D,0x5E,0x5F,
     0x60,0x61,0x62,0x63,0x64,0x65,0x66,0x67,0x68,0x69,0x6A,0x6B,0x6C,0x6D,0x6E,0x6F,
     0x70,0x71,0x72,0x73,0x74,0x75,0x76,0x77,0x78,0x79,0x7A,0x7B,0x7C,0x7D,0x7E,0x7F,
     0x80,0x81,0x82,0x83,0x84,0x85,0x86,0x87,0x88,0x89,0x8A,0x8B,0x8C,0x8D,0x8E,0x8F,
     0x90,0x91,0x92,0x93,0x94,0x95,0x96,0x97,0x98,0x99,0x9A,0x9B,0x9C,0x9D,0x9E,0x9F,
     0xA0,0x00,0x00,0x00,0x00,0x00,0x00,0xFD,0x00,0x00,0x00,0x00,0x00,0xAD,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
      
     0x00,0xA1,0xA2,0x00,0xA4,0xA5,0xA6,0xA7,0xA8,0xA9,0xAA,0xAB,0xAC,0x00,0xAE,0xAF,
     0xB0,0xB1,0xB2,0xB3,0xB4,0xB5,0xB6,0xB7,0xB8,0xB9,0xBA,0xBB,0xBC,0xBD,0xBE,0xBF,
     0xC0,0xC1,0xC2,0xC3,0xC4,0xC5,0xC6,0xC7,0xC8,0xC9,0xCA,0xCB,0xCC,0xCD,0xCE,0xCF,
     0xD0,0xD1,0xD2,0xD3,0xD4,0xD5,0xD6,0xD7,0xD8,0xD9,0xDA,0xDB,0xDC,0xDD,0xDE,0xDF,
     0xE0,0xE1,0xE2,0xE3,0xE4,0xE5,0xE6,0xE7,0xE8,0xE9,0xEA,0xEB,0xEC,0xED,0xEE,0xEF,
     0x00,0xF1,0xF2,0x00,0xF4,0xF5,0xF6,0xF7,0xF8,0xF9,0xFA,0xFB,0xFC,0x00,0xFE,0xFF,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0xA3,0xF3,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
      
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0xF0,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
     0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
     };

     static int encodeIndex[] = {
      0,-1,-1,-1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
      -1,2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
      -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
      -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
      -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
      -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
      -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
      -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
     };

    jint position = absolutePos;
    int i;
    jchar input1;
    jchar *in = (*env)->GetCharArrayElements(env, array, NULL);
    jint *res = (*env)->GetIntArrayElements(env, result, NULL);
    for(i=0; i<res[0]; i++) {
         jchar input = in[arrayOffset+i];
         if( input > 0x2116) { 
             if (input >= 0xD800 && input <= 0xDFFF) {
               if(i+1<res[0]) {
                 input1 = in[arrayOffset+i+1];
                 if(input1 >= 0xD800 && input1 <= 0xDFFF) {
                   res[0] = absolutePos - position; res[1] = 2;
                   (*env)->ReleaseIntArrayElements(env, result, res, 0);
                   (*env)->ReleaseCharArrayElements(env, array, in, 0);
                   return;
                 }
               } else {
                 res[0]=absolutePos - position; res[1] = 0;
                 (*env)->ReleaseIntArrayElements(env, result, res, 0);
                 (*env)->ReleaseCharArrayElements(env, array, in, 0);
                 return;
               }
               res[0]=absolutePos - position; res[1] = -1;
               (*env)->ReleaseIntArrayElements(env, result, res, 0);
               (*env)->ReleaseCharArrayElements(env, array, in, 0);
               return;
             }
             res[0]=absolutePos - position; res[1] = 1;
             (*env)->ReleaseCharArrayElements(env, array, in, 0);
             (*env)->ReleaseIntArrayElements(env, result, res, 0);
             return;
         }
         if( input < 0xA1 ) {
             *(jlong2addr(jbyte, outAddr) + position++) = (jbyte)input;
         }else{
             int index = (int)input >> 8;
             index = encodeIndex[index];
             if(index < 0) {
                 res[0]=absolutePos - position; res[1] = 1;
                 (*env)->ReleaseCharArrayElements(env, array, in, 0);
                 (*env)->ReleaseIntArrayElements(env, result, res, 0);
                 return;
             }
             index <<= 8;
             index += (int)input & 0xFF;
             if(table[input] != 0){
                 *(jlong2addr(jbyte, outAddr) + position++) = table[input];
             }else{
                 res[0]= absolutePos - position; res[1]=1;
                 (*env)->ReleaseIntArrayElements(env, result, res, 0);
                 (*env)->ReleaseCharArrayElements(env, array, in, 0);
                 return;
             }
         }
    }
    res[0]=position - absolutePos;
    (*env)->ReleaseIntArrayElements(env, result, res, 0);
    (*env)->ReleaseCharArrayElements(env, array, in, 0);
    return;
}

JNIEXPORT jint JNICALL Java_org_apache_harmony_niochar_charset_additional_x_1IBM1124_00024Decoder_nDecode
  (JNIEnv *env, jobject obj, jcharArray outArr, jint arrPosition, jint remaining, jlong inAddr, jint absolutePos)
{ 

    unsigned int table[] = {
     0x0401,0x0402,0x0490,0x0404,0x0405,0x0406,0x0407,
     0x0408,0x0409,0x040A,0x040B,0x040C,0x00AD,0x040E,0x040F,
     0x0410,0x0411,0x0412,0x0413,0x0414,0x0415,0x0416,0x0417,
     0x0418,0x0419,0x041A,0x041B,0x041C,0x041D,0x041E,0x041F,
     0x0420,0x0421,0x0422,0x0423,0x0424,0x0425,0x0426,0x0427,
     0x0428,0x0429,0x042A,0x042B,0x042C,0x042D,0x042E,0x042F,
     0x0430,0x0431,0x0432,0x0433,0x0434,0x0435,0x0436,0x0437,
     0x0438,0x0439,0x043A,0x043B,0x043C,0x043D,0x043E,0x043F,
     0x0440,0x0441,0x0442,0x0443,0x0444,0x0445,0x0446,0x0447,
     0x0448,0x0449,0x044A,0x044B,0x044C,0x044D,0x044E,0x044F,
     0x2116,0x0451,0x0452,0x0491,0x0454,0x0455,0x0456,0x0457,
     0x0458,0x0459,0x045A,0x045B,0x045C,0x00A7,0x045E,0x045F
    };

   jchar *out = (*env)->GetCharArrayElements(env, outArr, NULL);

   jint position = absolutePos;	
   int i; 
   unsigned char input;
   for(i=0; i < remaining; i++) {
        input = *(jlong2addr(jbyte, inAddr) + position++);
        if(input < 0xA1) {              	
            out[arrPosition+i] = (jchar)input;
        }else{
            out[arrPosition+i] = (jchar)table[input - 161];
        }
   }
   (*env)->ReleaseCharArrayElements(env, outArr, out, 0);
   return position-absolutePos;
}
