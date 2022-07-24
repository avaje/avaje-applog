package io.avaje.applog;

import java.util.ResourceBundle;
import java.util.ServiceLoader;

/**
 * Use of System.Logger for Application logging.
 *
 * <pre>{@code
 *
 *  System.Logger logger = AppLog.getLogger("org.foo");
 *  logger.log(Level.INFO, "Hello {0}", "world");
 *
 * }</pre>
 *
 * <p>
 * Defaults to using JDK {@link System#getLogger(String)} but allows applications
 * to provide an alternative System.Logger implementation.
 * <p>
 * Applications wanting control over the System.Logger implementation can firstly
 * provide a {@code System.LoggerFinder} and have that loaded via ServiceLoader.
 * This might not be possible for applications for example don't have a dedicated JVM.
 * <p>
 * Applications that can't use {@code System.LoggerFinder} can instead use
 * {@link AppLog.Provider} to provide System.Logger implementations. The reason
 * to use AppLog is that it provides this 1 extra level of indirection that applications
 * can use to control the System.Logger implementations.
 */
public final class AppLog {

  private static final Provider provider = ServiceLoader.load(Provider.class)
    .findFirst()
    .orElseGet(DefaultProvider::new);

  /**
   * Return the logger for the given class name.
   * <pre>{@code
   *
   *  System.Logger logger = AppLog.getLogger(Foo.class);
   *  logger.log(Level.INFO, "Hello {0}", "world");
   *
   * }</pre>
   *
   * @param cls The logger name provided by the class name.
   * @return The logger to use
   */
  public static System.Logger getLogger(Class<?> cls) {
    return provider.getLogger(cls.getName());
  }

  /**
   * Return the logger for the given name.
   * <pre>{@code
   *
   *  System.Logger logger = AppLog.getLogger("org.foo");
   *  logger.log(Level.INFO, "Hello {0}", "world");
   *
   * }</pre>
   *
   * @param name The logger name
   * @return The logger to use
   */
  public static System.Logger getLogger(String name) {
    return provider.getLogger(name);
  }

  /**
   * Return the logger for the given name and resource bundle.
   *
   * <pre>{@code
   *
   *  ResourceBundle bundle = ResourceBundle.getBundle("io.bazz.bar");
   *  System.Logger logger = AppLog.getLogger("io.bazz.Foo", bundle);
   *
   *  logger.log(Level.WARNING, "HELLO_KEY", "world");
   *
   * }</pre>
   *
   * @param name The logger name
   * @return The logger to use
   */
  public static System.Logger getLogger(String name, ResourceBundle bundle) {
    return provider.getLogger(name, bundle);
  }

  /**
   * The default implementation that just uses System.getLogger().
   */
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
   * Provide System.Logger implementations that would otherwise default to {@link System#getLogger(String)}.
   * <p>
   * Create an implementation of Provider and declare that to be service loaded by AppLog. We do this either
   * via module-info provides clause or via {@code META-INF/services/io.avaje.applog.AppLog$Provider} (or both).
   * <p>
   * Note that the {@code io.avaje:avaje-applog-slf4j} dependency provides an adapter for {@code slf4j-api}.
   * We can add that to our classpath/module-path and then System.Logger is implemented using slf4j-api.
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
