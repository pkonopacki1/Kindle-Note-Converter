package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.data.ClippingsLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    ClippingsLoader clippingsLoader = ClippingsLoader.INSTANCE;
    @FXML
    BorderPane mainWindow;
    @FXML
    ListView<String> bookList;

    Stage stage;

    public void loadNotes() {
        stage = (Stage) mainWindow.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open clipping file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                clippingsLoader.loadClippingsFromFile(file);
                showBookList();
            } catch (FileNotFoundException e) {
                // TODO: 20.10.2020 Zamień stackTrace na okno z informacją
                e.printStackTrace();
            }
        }
    }

    private void showBookList() {
        if(clippingsLoader.getBooksTitles() != null) {
            clippingsLoader.getBooksTitles().forEach(book -> bookList.getItems().add(book));
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
