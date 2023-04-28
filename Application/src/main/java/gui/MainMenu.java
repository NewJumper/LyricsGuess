package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainMenu extends Application {
    public static Stage window;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        stage.setTitle("TS Games");
        stage.getIcons().add(new Image("gui/textures/icon.png"));
        stage.setOnCloseRequest(event -> {
            event.consume();
            try {
                QuitMenu.display();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Font.loadFont(Objects.requireNonNull(MainMenu.class.getResource("fonts/norwester.otf")).toExternalForm(), 20);

        Parent root = FXMLLoader.load(Objects.requireNonNull(MainMenu.class.getResource("main.fxml")));
        window.setScene(new Scene(root));
        window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        showMenu();
        stage.show();
    }

    public static void showMenu() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainMenu.class.getResource("main.fxml")));
        updateScene(null, root, true);
    }

    public static void updateScene(Parent oldRoot, Parent newRoot, boolean isMain) throws IOException {
        Parent main = FXMLLoader.load(Objects.requireNonNull(MainMenu.class.getResource("main.fxml")));
        Scene scene = window.getScene();
        scene.setRoot(newRoot);

        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.F11) window.setFullScreen(!window.isFullScreen());
            if(event.getCode() == KeyCode.ESCAPE) {
                String rootId = window.getScene().getRoot().getId();
                if(isMain || oldRoot == main || (rootId != null && rootId.equals("guessingGame"))) return;

                try {
                    updateScene(main, oldRoot, false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void saveGame() throws IOException {
        if(GuessingMenu.trackCount > 1 && GuessingMenu.mode != 0) {
            String modeName;
            switch (GuessingMenu.mode) {
                default -> modeName = "NORMAL";
                case 2 -> modeName = "HARDCORE";
                case 3 -> modeName = "OPENING";
                case 4 -> modeName = "CLOSING";
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter("Application/src/main/resources/scores.txt", true));
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            writer.write(modeName + " - " + GuessingMenu.score + "  (" + (GuessingMenu.correct + GuessingMenu.incorrect) + ") " + dateFormat.format(new Date()) + "\n");
            writer.flush();
        }

        GuessingMenu.trackCount = 0;
        GuessingMenu.correct = 0;
        GuessingMenu.incorrect = 0;
        GuessingMenu.guesses = 0;
        GuessingMenu.strikes = 0;
    }

    public void play() throws IOException {
        PlayMenu.playMenu();
    }

    public void scores() throws IOException {
        ScoresMenu.scoresMenu();
    }

    public void settings() {
        System.out.println("settings");
    }

    public void quit() {
        window.close();
    }
}
