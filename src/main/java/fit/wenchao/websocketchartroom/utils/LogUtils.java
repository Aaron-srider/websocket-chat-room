package fit.wenchao.websocketchartroom.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtils {
    public static void log(String logLevel, String format, Object... args) {
        if ("info".equals(logLevel)) {
            log.info(format, args);
        } else if ("error".equals(logLevel)) {
            log.error(format, args);
        }
    }
}
