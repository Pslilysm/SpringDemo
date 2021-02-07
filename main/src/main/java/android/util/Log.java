package android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Log system, used like android
 * @since 2021.02.02
 * @author CXD
 */
public class Log {

    private Log() {
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable tr) {
        Logger logger = LoggerFactory.getLogger(tag);
        logger.debug(msg, tr);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable tr) {
        Logger logger = LoggerFactory.getLogger(tag);
        logger.info(msg, tr);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        Logger logger = LoggerFactory.getLogger(tag);
        logger.warn(msg, tr);
    }

    public static void w(String tag, Throwable tr) {
        w(tag, null, tr);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Logger logger = LoggerFactory.getLogger(tag);
        logger.error(msg, tr);
    }

}
