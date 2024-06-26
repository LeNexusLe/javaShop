package project.project;

import javafx.scene.control.*;

import java.util.Optional;

public class AlertStyleController {

    public void notificationError(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(text);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }
    public void notificationInfo(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(text);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.showAndWait();
    }
    public boolean confirmationBox(String title, String text) {
        boolean result = false;

        ButtonType foo = new ButtonType("Potwierdź", ButtonBar.ButtonData.OK_DONE);
        ButtonType bar = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "" ,foo, bar);
        alert.setHeaderText(title);
        alert.setContentText(text);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");

        Optional<ButtonType> option = alert.showAndWait();

        if (option.orElse(bar) == foo) {
            result = true;
        }

        return result;
    }
}
