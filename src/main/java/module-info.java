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
    requires spring.security.crypto;
    requires org.apache.commons.logging;
    requires org.bouncycastle.provider;
    requires org.jfxtras.styles.jmetro;
    requires com.calendarfx.view;
    requires org.mnode.ical4j.core;

    opens fr._14.pcd.codingweek15.controllers to javafx.fxml;
    opens fr._14.pcd.codingweek15 to javafx.fxml;
    opens fr._14.pcd.codingweek15.model to org.hibernate.orm.core;
    exports fr._14.pcd.codingweek15;
    exports fr._14.pcd.codingweek15.controllers;
    exports fr._14.pcd.codingweek15.model;
    exports fr._14.pcd.codingweek15.controllers.auth;
    opens fr._14.pcd.codingweek15.controllers.auth to javafx.fxml;
    exports fr._14.pcd.codingweek15.controllers.borrow;
    opens fr._14.pcd.codingweek15.controllers.borrow to javafx.fxml;
    exports fr._14.pcd.codingweek15.controllers.loan;
    opens fr._14.pcd.codingweek15.controllers.loan to javafx.fxml;
}
