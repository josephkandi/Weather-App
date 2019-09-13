#include <jni.h>
#include <string>
#include <sys/stat.h>

using namespace std;

const string BINARY_SU = "su";
const string BINARY_BUSYBOX = "busybox";
const string suPaths[] = {
    "/data/local/",
    "/data/local/bin/",
    "/data/local/xbin/",
    "/sbin/",
    "/su/bin/",
    "/system/bin/",
    "/system/bin/.ext/",
    "/system/bin/failsafe/",
    "/system/sd/xbin/",
    "/system/usr/we-need-root/",
    "/system/xbin/",
    "/cache/",
    "/data/",
    "/dev/"
};


bool fileExists(const string &fileName) {
    struct stat buf;
    return (stat(fileName.c_str(), &buf) == 0);
}

bool binaryExists(const string &binary) {
    for (int i = 0; i < sizeof(suPaths)/ sizeof(*suPaths) ; ++i) {
        const string fullPath = suPaths[i] + binary;
        if(fileExists(fullPath)){
            return true;
        }
    }
    return false;
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_peruzal_weather_helpers_RootUtils_isRooted(JNIEnv *env, jclass obj){
    return binaryExists(BINARY_SU) || binaryExists(BINARY_BUSYBOX);
}



