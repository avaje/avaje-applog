[![Build](https://github.com/avaje/avaje-applog/actions/workflows/build.yml/badge.svg)](https://github.com/avaje/avaje-applog/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.avaje/avaje-applog.svg?label=Maven%20Central)](https://mvnrepository.com/artifact/io.avaje/avaje-applog)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/avaje/avaje-applog/blob/master/LICENSE)
[![JDK EA](https://github.com/avaje/avaje-applog/actions/workflows/jdk-ea.yml/badge.svg)](https://github.com/avaje/avaje-applog/actions/workflows/jdk-ea.yml)

# avaje-applog

Application and library use of Java `System.Logger` logging facade.

### Dependency

```xml
<dependency>
  <groupId>io.avaje</groupId>
  <artifactId>avaje-applog</artifactId>
  <version>1.0</version>
</dependency>
```

### How to use

Use `AppLog.getLogger()` rather than `System.getLogger()` to obtain `System.Logger`.

```java
System.Logger logger = AppLog.getLogger("org.foo");

logger.log(Level.INFO, "Hello {0}", "world");
```

Using `AppLog.getLogger()` rather than using `System.getLogger()` gives applications an extra
option for controlling the `System.Logger` implementation returned by AppLog. Typically, this
is useful in the case where application does not have a dedicated JVM where service loading
`System.LoggerFinder` is not sufficient. One such environment currently is AWS Lambda Java runtime.


### Defaults to System.getLogger()

Defaults to using JDK `System.getLogger()` but allows applications to provide an alternative
System.Logger implementation returned by AppLog.getLogger() by service loading a `AppLog.Provider`.


### Custom System.Logger implementation

Applications wanting control over the System.Logger implementation can firstly provide
a `System.LoggerFinder` and have that loaded via ServiceLoader. This might not be possible
for applications that for example don't have a dedicated JVM.

Applications that can't use `System.LoggerFinder` can instead use `AppLog.Provider` to
control the System.Logger implementations returned by AppLog. The reason to use AppLog is
that it provides this 1 extra level of indirection that applications can use to control
the System.Logger implementation returned by AppLog.


### SLF4J-API - avaje-applog-slf4j

We can add `avaje-applog-slf4j` as a dependency such that System.Logger implementations
provided by `AppLog.getLogger()` are slf4j-api Logger.

```xml
<dependency>
  <groupId>io.avaje</groupId>
  <artifactId>avaje-applog-slf4j</artifactId>
  <version>1.0</version>
</dependency>
```


---------------

## Main differences to slf4j-api

#### MessageFormat placeholders

System.Logger uses {@link java.text.MessageFormat} with placeholders like <code> {0}, {1}, {2} ... </code>
rather than slf4j which uses <code> {} </code>. This means parameters can be referenced multiple times
and the parameters can use formats like <code>{0,number,#.##} {0,time} {0,date}</code> etc.
See {@link java.text.MessageFormat} for more details.


#### Number format

By default, numbers are formatted based on locale so {@code 8080 } can be formatted as {@code 8,080}.
We often prefer to format integers like <code> {0,number,#} </code> rather than <code> {0} </code>.
```java
// use {1,number,integer} to format the int port so we get 8080 rather than 8,080
logger.log(Level.INFO, "started with host {0} port {1,number,integer} ", host, port);
```


#### Throwable and vararg parameters

When logging a message with vararg parameters, slf4j will automatically try to detect if the last
parameter is a Throwable and if so extract it and trim the parameters. System.Logger does not do this,
instead we need to be explicit when logging Throwable and formatting the message if needed.

```java
System.Logger logger = AppLog.getLogger("org.foo");

// using varargs parameters all ok with no Throwable
logger.log(Level.INFO, "My {0} Hello {1} message", "silly", "world");
try {
    someMethodThatThrows();
} catch (Throwable e) {
  // need to format message here and explicitly pass throwable
  logger.log(Level.ERROR, MessageFormat.format("Error using {0} and {1}", "MyParam0", "OtherParam1"), e);
}
```

#### No slf4j `MDC`, No slf4j `Markers`, No fluid API
