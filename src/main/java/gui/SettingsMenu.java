package gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SettingsMenu implements Initializable {
    public Text artistSel;

    private static Text artistSelStatic;
    public static String artist = "TS";

    /*
     * WHAT TO STORE:
     * WHAT ARTIST: AJR, SABRINA CARPENTER, TAYLOR SWIFT, TWENTY ONE PILOTS
     *      ARTIST CODES: AJ, SC, TS, TP
     * TIME FOR TIME ATTACK: 1 MIN, 2 MIN, 3 MIN, 5 MIN
     */
    public static void settingsMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("settings.fxml"))));
        Parent oldRoot = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("main.fxml"))));
        MainMenu.updateScene(oldRoot, root, false);

        artistSelStatic.setText(artist);
    }

    public void changeArtist() {
        if(artist.equals("TS")) artistSelStatic.setText(artist = "AJ");
        else if(artist.equals("AJ")) artistSelStatic.setText(artist = "TS");
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        artistSelStatic = artistSel;
    }
}
