module io.avaje.applog {

  exports io.avaje.applog;
  requires static java.logging; // testing purposes only

  uses io.avaje.applog.AppLog.Provider;
}
