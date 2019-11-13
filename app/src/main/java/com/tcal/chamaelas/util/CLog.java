package com.tcal.chamaelas.util;

import android.util.Log;

/**
 * Support class to standardize log messages
 *
 * All messages, independent of level, will be on format BASE_TAG: [tag] message
 */
public class CLog {
    private static final String BASE_TAG = "ChamaElasApp";

    /**
     * Debug level message
     *
     * @param tag     sub-tag, will be logged as [tag] on begin o
     * @param message log message
     */
    public static void d(String tag, String message) {
        Log.d(BASE_TAG, getPrefixedMessage(tag, message));
    }

    /**
     * Error level message
     *
     * @param tag     sub-tag, will be logged as [tag] on begin o
     * @param message log message
     */
    public static void e(String tag, String message) {
        Log.e(BASE_TAG, getPrefixedMessage(tag, message));
    }

    /**
     * Error level message
     *
     * @param tag     sub-tag, will be logged as [tag] on begin o
     * @param message log message
     * @param tr      Throwable / Exception to be logged
     */
    public static void e(String tag, String message, Throwable tr) {
        Log.e(BASE_TAG, getPrefixedMessage(tag, message), tr);
    }

    /**
     * Information level message
     *
     * @param tag     sub-tag, will be logged as [tag] on begin o
     * @param message log message
     */
    public static void i(String tag, String message) {
        Log.i(BASE_TAG, getPrefixedMessage(tag, message));
    }

    /**
     * Verbose level message
     *
     * @param tag     sub-tag, will be logged as [tag] on begin o
     * @param message log message
     */
    public static void v(String tag, String message) {
        Log.v(BASE_TAG, getPrefixedMessage(tag, message));
    }

    /**
     * Warning level message
     *
     * @param tag     sub-tag, will be logged as [tag] on begin o
     * @param message log message
     */
    public static void w(String tag, String message) {
        Log.w(BASE_TAG, getPrefixedMessage(tag, message));
    }

    /**
     * Prefix a message
     *
     * @param prefix  prefix string
     * @param message message to be prefixed
     * @return prefixed message
     */
    private static String getPrefixedMessage(String prefix, String message) {
        return "[" + prefix + "] " + message;
    }
}
