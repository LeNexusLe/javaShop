module project.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens project.project to javafx.fxml;
    exports project.project;
}