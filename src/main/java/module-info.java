module fr._14.pcd.codingweek15 {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.naming;
    requires java.sql;
    requires java.xml.bind;
    requires net.bytebuddy;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.hibernate.orm.core;
    requires static lombok;
  requires java.persistence;

  opens fr._14.pcd.codingweek15.controllers to javafx.fxml;
  opens fr._14.pcd.codingweek15 to javafx.fxml;
  opens fr._14.pcd.codingweek15.model to org.hibernate.orm.core;
    exports fr._14.pcd.codingweek15;
}