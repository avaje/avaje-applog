# avaje-applog
Application use of Java System.Logger logging facade

### Use

Use `AppLog.get()` rather than `System.getLogger()` to obtain `System.Logger` implementations.


```java
System.Logger logger = AppLog.get("org.foo.Bar");

```


### Defaults to System.getLogger()

Defaults to using JDK {@link System#getLogger(String)} but allows applications
to provide an alternative System.Logger implementation.


### Custom System.Logger implementation

Applications wanting control over the System.Logger implementation can firstly
provide a `System.LoggerFinder` and have that loaded via ServiceLoader.
This might not be possible for applications that for example don't have a
dedicated JVM.

Applications that can't use `System.LoggerFinder`}` can instead use
`AppLog.Provider` to provide System.Logger implementations. The reason
to use AppLog is that it provides this 1 extra level of indirection that
applications can use to control the System.Logger implementations.


### avaje-applog-slf4j

This is a dependency that we can include such that System.Logger implementations
provided by `AppLog.get()` are slf4j-api Logger.

```xml
<dependency>
  <groupId>io.avaje</groupId>
  <artifactId>avaje-applog-slf4j</artifactId>
  <version>1.0</version>
</dependency>
```
