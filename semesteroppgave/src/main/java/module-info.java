module Semesteroppgave {
    requires javafx.controls;
    requires javafx.fxml;

    opens Semesteroppgave to javafx.fxml;
    exports Semesteroppgave;
    exports Components;
}