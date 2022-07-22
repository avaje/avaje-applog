package io.avaje.applog;


import org.junit.jupiter.api.Test;

import java.lang.System.Logger.Level;
import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;

class AppLogTest {

  static JULCaptureHandler logCapture = JULCaptureHandler.install();

  @Test
  void getByName() {
    System.Logger logger = AppLog.get("my.foo");
    assertThat(logger.getName()).isEqualTo("my.foo");

    logger.log(Level.INFO, "Hello {0}", "world");

    assertThat(logCapture.lastRecord.getMessage()).isEqualTo("Hello {0}");
    assertThat(logCapture.lastRecord.getParameters()).hasSize(1);
    assertThat(logCapture.lastRecord.getLevel()).isEqualTo(java.util.logging.Level.INFO);
    assertThat(logCapture.lastRecord.getLoggerName()).isEqualTo("my.foo");
    assertThat(logCapture.lastRecord.getResourceBundle()).isNull();
    assertThat(logCapture.lastRecord.getResourceBundleName()).isNull();
  }

  @Test
  void getByClass() {
    System.Logger logger = AppLog.get(AppLog.class);
    assertThat(logger.getName()).isEqualTo("io.avaje.applog.AppLog");
  }

  @Test
  void getByNestedClass() {
    System.Logger logger = AppLog.get(AppLog.Provider.class);
    assertThat(logger.getName()).isEqualTo("io.avaje.applog.AppLog$Provider");
  }

  @Test
  void getWithResourceBundle() {

    ResourceBundle loggerBundle = ResourceBundle.getBundle("io.bazz.bar");
    System.Logger logger = AppLog.get("my.foo", loggerBundle);
    assertThat(logger.getName()).isEqualTo("my.foo");

    // use loggers bundle
    logger.log(Level.WARNING, "key0", "world");

    assertThat(logCapture.lastRecord.getMessage()).isEqualTo("key0");
    assertThat(logCapture.lastRecord.getParameters()).hasSize(1);
    assertThat(logCapture.lastRecord.getParameters()[0]).isEqualTo("world");
    assertThat(logCapture.lastRecord.getLevel()).isEqualTo(java.util.logging.Level.WARNING);
    assertThat(logCapture.lastRecord.getLoggerName()).isEqualTo("my.foo");
    assertThat(logCapture.lastRecord.getResourceBundle()).isSameAs(loggerBundle);
    assertThat(logCapture.lastRecord.getResourceBundleName()).isEqualTo("io.bazz.bar");

    ResourceBundle baxBundle = ResourceBundle.getBundle("io.bazz.bax");

    // use explicitly supplied bundle
    logger.log(Level.WARNING, baxBundle, "key0", "world");
    assertThat(logCapture.lastRecord.getResourceBundle()).isSameAs(baxBundle);
    assertThat(logCapture.lastRecord.getResourceBundleName()).isEqualTo("io.bazz.bax");
  }
}
