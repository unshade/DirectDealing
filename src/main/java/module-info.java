module fr._14.pcd.codingweek15 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;

    opens fr._14.pcd.codingweek15 to javafx.fxml;
    exports fr._14.pcd.codingweek15;
}