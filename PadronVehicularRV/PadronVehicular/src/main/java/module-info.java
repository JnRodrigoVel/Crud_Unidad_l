module pe.edu.upeu.padronvehicular {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires static lombok;
    requires jakarta.validation;

    opens pe.edu.upeu.padronvehicular to javafx.fxml;
    opens pe.edu.upeu.padronvehicular.controller to javafx.fxml;
    opens pe.edu.upeu.padronvehicular.model;
    exports pe.edu.upeu.padronvehicular;
}
