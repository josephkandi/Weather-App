#include <jni.h>
#include <string>

extern "C" JNIEXPORT jboolean JNICALL Java_com_peruzal_weather_helpers_RootUtils_isRooted(JNIEnv *env, jclass obj){
    bool isRooted = true;
    return isRooted;
}