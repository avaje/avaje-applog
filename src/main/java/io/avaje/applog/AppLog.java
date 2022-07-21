package io.avaje.applog;

import java.util.ResourceBundle;
import java.util.ServiceLoader;

/**
 * Use of System.Logger for Application logging.
 * <p>
 * Defaults to using JDK {@link System#getLogger(String)} but allows applications
 * to provide an alternative System.Logger implementation.
 */
public class AppLog {

  private static final Provider provider = ServiceLoader.load(Provider.class)
    .findFirst()
    .orElseGet(DefaultProvider::new);

  /**
   * Return the logger for the given class name.
   *
   * @param cls The logger name provided by the class name.
   * @return The logger to use
   */
  public static System.Logger get(Class<?> cls) {
    return provider.getLogger(cls.getName());
  }

  /**
   * Return the logger for the given name.
   *
   * @param name The logger name
   * @return The logger to use
   */
  public static System.Logger get(String name) {
    return provider.getLogger(name);
  }

  /**
   * Return the logger for the given name and resource bundle.
   *
   * @param name The logger name
   * @return The logger to use
   */
  public static System.Logger get(String name, ResourceBundle bundle) {
    return provider.getLogger(name, bundle);
  }

  private static final class DefaultProvider implements Provider {

    @Override
    public System.Logger getLogger(String name) {
      return System.getLogger(name);
    }

    @Override
    public System.Logger getLogger(String name, ResourceBundle bundle) {
      return System.getLogger(name, bundle);
    }
  }

  /**
   * Provide System.Logger implementations that otherwise default to {@link System#getLogger(String)}.
   */
  public interface Provider {

    /**
     * Return the logger implementation given the logger name.
     *
     * @param name The logger name
     */
    System.Logger getLogger(String name);

    /**
     * Return the logger implementation given the logger name and resource bundle.
     *
     * @param name   The logger name
     * @param bundle The resource bundle containing localised messages
     */
    System.Logger getLogger(String name, ResourceBundle bundle);
  }
}
