package gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class SettingsMenu implements Initializable {
    public Text artistSel;
    public Text timeSel;
    public Text resolutionSel;

    private static Text artistSelStatic;
    private static Text timeSelStatic;
    private static Text resolutionSelStatic;
    public static String artist = "AA";
    public static int timeControl = 3;
    private static final LinkedHashMap<Integer, String> ARTISTS = new LinkedHashMap<>();
    private static final LinkedHashMap<Integer, Integer> TIME_CONTROLS = new LinkedHashMap<>();
    private static int artistsIndex;
    private static int timeIndex = 2;

    public static void settingsMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("settings.fxml"))));
        Parent oldRoot = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("main.fxml"))));
        MainMenu.updateScene(oldRoot, root, false);

        ARTISTS.putAll(Map.of(0, "All Artists,AA", 1, "AJR,AJ", 2, "Friday Pilots Club,FC", 3, "Sabrina Carpenter,SC", 4, "Taylor Swift,TS", 5, "Twenty One Pilots,TP"));
        TIME_CONTROLS.putAll(Map.of(0, 1, 1, 2, 2, 3, 3, 5, 4, 10));

        artistSelStatic.setText(ARTISTS.get(artistsIndex).substring(0, ARTISTS.get(artistsIndex).indexOf(",")));
        timeSelStatic.setText(TIME_CONTROLS.get(timeIndex) + ":00");
        resolutionSelStatic.setText(MainMenu.window.isFullScreen() ? "Fullscreen" : "Windowed");
    }

    public void cycleArtistLeft() {
        if(--artistsIndex < 0) artistsIndex = ARTISTS.size() - 1;
        String selection = ARTISTS.get(artistsIndex);
        artist = selection.substring(selection.indexOf(",") + 1);
        artistSelStatic.setText(selection.substring(0, selection.indexOf(",")));
    }

    public void cycleArtistRight() {
        if(++artistsIndex >= ARTISTS.size()) artistsIndex = 0;
        String selection = ARTISTS.get(artistsIndex);
        artist = selection.substring(selection.indexOf(",") + 1);
        artistSelStatic.setText(selection.substring(0, selection.indexOf(",")));
    }

    public void decrementTime() {
        if(--timeIndex < 0) timeIndex = TIME_CONTROLS.size() - 1;
        timeSelStatic.setText((timeControl = TIME_CONTROLS.get(timeIndex)) + ":00");
    }

    public void incrementTime() {
        if(++timeIndex >= TIME_CONTROLS.size()) timeIndex = 0;
        timeSelStatic.setText((timeControl = TIME_CONTROLS.get(timeIndex)) + ":00");
    }

    public void changeResolution() {
        MainMenu.window.setFullScreen(!MainMenu.window.isFullScreen());
        resolutionSelStatic.setText(MainMenu.window.isFullScreen() ? "Fullscreen" : "Windowed");
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        artistSelStatic = artistSel;
        timeSelStatic = timeSel;
        resolutionSelStatic = resolutionSel;
    }
}
