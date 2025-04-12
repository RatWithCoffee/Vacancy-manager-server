module vacancy.manager.server.main {
    requires org.postgresql.jdbc;
    requires java.rmi;
    requires java.sql;
    exports vacancy_manager.rmi_interfaces to java.rmi;
}