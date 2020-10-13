package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.data.ClippingsLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    ClippingsLoader clippingsLoader = ClippingsLoader.INSTANCE;
    @FXML
    BorderPane mainWindow;
    Stage stage;

    public void loadBooksToPanel() {
        stage = (Stage) mainWindow.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open clipping file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files", "*.txt"));
        fileChooser.showOpenDialog(stage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
