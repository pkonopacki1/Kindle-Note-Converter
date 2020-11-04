package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.data.ClippingsLoader;
import org.example.data.KindleNote;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    ClippingsLoader clippingsLoader = ClippingsLoader.INSTANCE;
    @FXML
    BorderPane mainWindow;
    @FXML
    ListView<String> bookList;
    @FXML
    VBox notesVBox;
    @FXML
    Button loadNotesBtn;
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
                showAlertDialog("Loaded file is not kindle clippings, try again.");
            }
        }
    }
    private void showBookList() {
        if(clippingsLoader.getBooksTitles() != null) {
            bookList.getItems().clear();
            clippingsLoader.getBooksTitles().forEach(book -> bookList.getItems().add(book));
        }
    }

    private void showBookNotes(String title) {
        List<KindleNote> notes = clippingsLoader.getBookNotes(title);

        for (KindleNote note: notes) {
            notesVBox.getChildren().add(createNoteCard(note));
        }
    }
    private void removeBookNotes() {
        notesVBox.getChildren().clear();
    }
    private TitledPane createNoteCard(KindleNote note) {
        TextArea noteArea = new TextArea(note.getNote());
        noteArea.setWrapText(true);
        TitledPane titledPane = new TitledPane(note.getInfo(), noteArea);
        titledPane.setMaxHeight(100);
        titledPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        return titledPane;
    }

    public void exportAllNotes() {
        File file = saveTxtFile("All notes");
        if(file != null) clippingsLoader.saveAllNotes(file);
    }

    public void exportBookNotes() {
        String selectedTile = bookList.getSelectionModel().getSelectedItem();
        if(selectedTile == null) return;
        File file = saveTxtFile(selectedTile);
        if(file != null) clippingsLoader.saveOneBookNotes(selectedTile, file);
    }

    private File saveTxtFile(String fileTitle) {
        stage = (Stage) mainWindow.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files", "*.txt"));
        fileChooser.setInitialFileName(fileTitle.concat(".txt"));
        return fileChooser.showSaveDialog(stage);
    }

    private void showAlertDialog(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText(info);
        alert.setHeaderText(null);

        alert.showAndWait();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadNotesBtn.setOnAction(event -> {
            if(bookList.getSelectionModel().getSelectedItem() != null) {
                removeBookNotes();
                showBookNotes(bookList.getSelectionModel().getSelectedItem());
            }
        });
    }
}
