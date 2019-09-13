package com.peruzal.weather.helpers;

public class RootUtils {
    static {
        System.loadLibrary("native-lib");
    }

    public static native boolean isRooted();
}
