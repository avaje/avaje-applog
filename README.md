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

Use `AppLog.getLogger()` rather than `System.getLogger()` to obtain `System.Logger` implementations.

```java
System.Logger logger = AppLog.getLogger("org.foo.Bar");

```

Using `AppLog.getLogger()` rather than using `System.getLogger()` directly gives applications an extra
option for controlling the `System.Logger` implementation. Typically, this is useful in the
case where application does not have a dedicated JVM where service loading `System.LoggerFinder`
is not sufficient.


### Defaults to System.getLogger()

Defaults to using JDK `System.getLogger()` but allows applications to provide an alternative
System.Logger implementation by service loading a `AppLog.Provider`.


### Custom System.Logger implementation

Applications wanting control over the System.Logger implementation can firstly provide
a `System.LoggerFinder` and have that loaded via ServiceLoader. This might not be possible
for applications that for example don't have a dedicated JVM.

Applications that can't use `System.LoggerFinder` can instead use`AppLog.Provider` to
provide System.Logger implementations. The reason to use AppLog is that it provides this
1 extra level of indirection that applications can use to control the System.Logger
implementations.


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
