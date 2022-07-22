package io.avaje.applog;

import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class JULCaptureHandler extends Handler {

  LogRecord lastRecord;

  public static JULCaptureHandler install() {
    removeHandlersForRootLogger();
    JULCaptureHandler handler = new JULCaptureHandler();
    LogManager.getLogManager().getLogger("").addHandler(handler);
    return handler;
  }

  private static Logger getRootLogger() {
    return LogManager.getLogManager().getLogger("");
  }

  private static void removeHandlersForRootLogger() {
    Logger rootLogger = getRootLogger();
    Handler[] handlers = rootLogger.getHandlers();
    for (Handler handler : handlers) {
      rootLogger.removeHandler(handler);
    }
  }

  public void close() {
  }

  public void flush() {
  }

  public void publish(LogRecord record) {
    this.lastRecord = record;
  }

}
