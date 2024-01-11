module fr.quatorze.pcd.codingweekquinze {
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

    opens fr.quatorze.pcd.codingweekquinze.controllers to javafx.fxml;
    opens fr.quatorze.pcd.codingweekquinze.controllers.components to javafx.fxml;
    opens fr.quatorze.pcd.codingweekquinze to javafx.fxml;
    opens fr.quatorze.pcd.codingweekquinze.model to org.hibernate.orm.core;
    exports fr.quatorze.pcd.codingweekquinze;
    exports fr.quatorze.pcd.codingweekquinze.controllers;
    exports fr.quatorze.pcd.codingweekquinze.model;
    exports fr.quatorze.pcd.codingweekquinze.controllers.auth;
    opens fr.quatorze.pcd.codingweekquinze.controllers.auth to javafx.fxml;
    exports fr.quatorze.pcd.codingweekquinze.controllers.borrow;
    opens fr.quatorze.pcd.codingweekquinze.controllers.borrow to javafx.fxml;
    exports fr.quatorze.pcd.codingweekquinze.controllers.loan;
    opens fr.quatorze.pcd.codingweekquinze.controllers.loan to javafx.fxml;
    opens fr.quatorze.pcd.codingweekquinze.layout.component to javafx.fxml;
}
