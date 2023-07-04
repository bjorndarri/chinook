/**
 * Domain API.
 */
module is.codion.framework.demos.chinook.domain.api {
  requires is.codion.common.db;
  requires is.codion.framework.db.core;
  requires transitive is.codion.plugin.jasperreports;
  requires java.desktop;

  exports is.codion.framework.demos.chinook.domain.api;

  //for accessing default methods in EntityType interfaces and resources
  opens is.codion.framework.demos.chinook.domain.api;
}