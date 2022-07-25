package io.avaje.applog;


import org.junit.jupiter.api.Test;

import java.lang.System.Logger.Level;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Sanity check only - no logging capture, no asserts.
 */
class NoCaptureTest {

  @Test
  void getByName() {
    System.Logger logger = AppLog.getLogger("my.foo");
    logger.log(Level.INFO, "Name Hello {0}", "world");
  }

  @Test
  void getByClass() {
    System.Logger logger = AppLog.getLogger(AppLog.class);
    logger.log(Level.INFO, "Class Hello {0}", "world");
  }

  @Test
  void getByNestedClass() {
    System.Logger logger = AppLog.getLogger(AppLog.Provider.class);
    logger.log(Level.INFO, "nested Hello {0}", "world");
  }

  @Test
  void getWithResourceBundle() {

    ResourceBundle loggerBundle = ResourceBundle.getBundle("io.bazz.bar");
    System.Logger logger = AppLog.getLogger("my.foo", loggerBundle);

    // use loggers bundle
    logger.log(Level.WARNING, "key0", "logger resource bunde");

    ResourceBundle baxBundle = ResourceBundle.getBundle("io.bazz.bax");
    // use explicitly supplied bundle
    logger.log(Level.WARNING, baxBundle, "key0", "method resource bundle");
  }

  @Test
  void withError() {
    System.Logger logger = AppLog.getLogger("my.foo");
    logger.log(Level.INFO, "Name Hello {0}", "world");
    try {
      methodThatThrows();
    } catch (Throwable e) {
      logger.log(Level.ERROR, MessageFormat.format("This error {0}", "MyParam"), e);
    }
  }

  private void methodThatThrows() {
    throw new RuntimeException("I like to throw");
  }
}
