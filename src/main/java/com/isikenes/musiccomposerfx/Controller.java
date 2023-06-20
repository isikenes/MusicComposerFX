package com.isikenes.musiccomposerfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Initializable {

    @FXML
    private ChoiceBox<String> instrumentCB;

    @FXML
    private Slider durationSlider;

    @FXML
    private Slider tempoSlider;

    @FXML
    Button saveButton;

    static Player player;
    String[] notes = {"A", "B", "C", "D", "E", "F", "G", "Ah2", "Bh2", "Ch2", "Dh2", "Eh2", "Fh2", "Gh2", "A4q", "B4q", "C4q", "D4q", "E4q", "F4q", "G4q"};
    Pattern pattern;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instrumentCB.setValue("Piano");
        instrumentCB.getItems().addAll("Piano", "Violin", "Cello", "Guitar", "Trumpet", "Flute", "Xylophone");
        player = new Player();
        saveButton.setVisible(false);
    }

    @FXML
    public void Compose() {
        StopMusic();
        int length = (int) durationSlider.getValue();
        int speed = (int) tempoSlider.getValue();
        String instrument = instrumentCB.getValue().toUpperCase(Locale.ROOT);

        StringBuilder music = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            music.append(notes[random.nextInt(notes.length)]).append(" ");
        }

        pattern = new Pattern("V0 T" + speed + " I[" + instrument + "] ");
        pattern.add(music.toString());

        //this code prevents app freeze while music is playing in main thread
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> player.play(pattern));
        executorService.shutdown();
        saveButton.setVisible(true);
    }

    @FXML
    public void Save() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Audio Files", "*.midi"));
        File selectedFile = fileChooser.showSaveDialog(durationSlider.getScene().getWindow());

        try {
            MidiFileManager.savePatternToMidi(pattern, selectedFile);
        } catch (IOException | RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Check the file information!");
            alert.showAndWait();
        }
    }

    public static void StopMusic() {
        player.getManagedPlayer().finish();
    }

}