package io.avaje.applog;

import java.util.ResourceBundle;
import java.util.ServiceLoader;

/**
 * Use of System.Logger for Application and Library logging.
 * <pre>{@code
 *
 *   System.Logger logger = AppLog.getLogger("org.foo");
 *   logger.log(Level.INFO, "Hello {0}", "world");
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
 *
 *
 * <hr/>
 *
 * <h2>Main differences to slf4j-api</h2>
 *
 * <h4>MessageFormat placeholders</h3>
 * <p>
 * System.Logger uses {@link java.text.MessageFormat} with placeholders like <code>{0}, {1}, {2} ...</code>
 * rather than slf4j which uses <code>{}</code>. This means parameters can be referenced multiple times
 * and the parameters can use formats like <code>{0,number,#.##} {0,time} {0,date}</code> etc.
 * See {@link java.text.MessageFormat} for more details.
 *
 * <h4>Default number format</h3>
 * <p>
 * By default, numbers are formatted based on locale so {@code 8080 } can be formatted as {@code 8,080}.
 * For this reason, we often prefer to format integers like <code>{0,number,#}</code> rather than
 * <code>{0}</code>.
 *
 * <pre>{@code
 *
 *   // use {1,number,integer} to format the int port so we get 8080 rather than 8,080
 *   logger.log(Level.INFO, "started with host {0} port {1,number,integer} ", host, port);
 *
 * }</pre>
 *
 *
 * <h4>Throwable and vararg parameters</h3>
 * <p>
 * When logging a message with vararg parameters, slf4j will automatically try to detect if the last
 * parameter is a Throwable and if so extract it and trim the parameters. System.Logger does not do this,
 * instead we need to be explicit when logging Throwable and formatting the message if needed.
 * <pre>{@code
 *
 *   System.Logger logger = AppLog.getLogger("org.foo");
 *
 *   // using varargs parameters all ok with no Throwable
 *   logger.log(Level.INFO, "My {0} Hello {1} message", "silly", "world");
 *   try {
 *       someMethodThatThrows();
 *   } catch (Throwable e) {
 *     // need to format message here and explicitly pass throwable
 *     logger.log(Level.ERROR, MessageFormat.format("Error using {0} and {1}", "MyParam0", "OtherParam1"), e);
 *   }
 * }</pre>
 *
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
