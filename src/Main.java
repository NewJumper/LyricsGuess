import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final Scanner INPUT = new Scanner(System.in);
    public static List<List<String>> albums = new ArrayList<>();
    public static List<String> scores = new ArrayList<>();
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[1;32m";
    public static final String CYAN = "\033[36m";
    public static final String YELLOW = "\033[1;33m";
    public static final String YELLOW_L = "\u001B[1;93m";
    public static final String BOLD = "\033[1;37m";
    public static final String ITALICS = "\033[3;37m";
    public static final String RESET = "\033[0m";
    public static final List<Integer> rows = new ArrayList<>();
    public static boolean hardcore;
    public static int maxRankings;
    public static int tracks;
    public static int correct;
    public static int incorrect;
    public static int lines;

    public static void main(String[] args) throws IOException {
        albums.add(Files.readAllLines(Paths.get("src/albums/taylor swift.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/fearless.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/speak now.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/red.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/1989.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/reputation.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/lover.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/folklore.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/evermore.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/midnights.txt")));

        scores = Files.readAllLines(Paths.get("src/scores.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/scores.txt", true));

        System.out.format(YELLOW_L + """
                \nWhich gamemode do you want to play?
                ALL %s-%s Guess all 188 songs to complete the game.
                %sHARDCORE %s-%s Guess all 188 songs from only one line and one guess.
                %sRANDOM %s-%s Set a high score by guessing songs that are randomly chosen.
                %sTIME ATTACK %s-%s Identify as many lines in two minutes.
                %sZEN %s-%s Guess all 188 songs from two neighboring lines with no penalties.
                %sSCORES - View the top scores
                """, RESET, YELLOW, YELLOW_L, RESET, YELLOW, YELLOW_L, RESET, YELLOW, YELLOW_L, RESET, YELLOW, YELLOW_L, RESET, YELLOW, BOLD);

        String choice = INPUT.nextLine();
        long start = System.nanoTime();

        switch(choice.toUpperCase()) {
            case "ALL" -> {
                System.out.println(BOLD + """
                    \nALL songs chosen
                    Try to get all 188 song names correct! You have infinite guesses!
                    If you want to end the game, type "quit" after the song's name is revealed.
                    """ + RESET);

                TitleGuessing.allSongsGuessing();
                System.out.println(YELLOW_L + "\nALL" + RESET + " songs");
                writer.write("ALL - ");
            }
            case "HARDCORE" -> {
                hardcore = true;
                System.out.println(RED + "\nHARDCORE challenge" + BOLD + """
                     chosen
                    Try to guess all 188 song names correct with only\s""" + RED + "ONE" + BOLD + " line and " + RED + "ONE" + BOLD + " guess!" + """
                    \nThe game will end if you guess incorrectly!
                    If you want to end the game, type "quit" after the song's name is revealed.
                    """ + RESET);

                TitleGuessing.allSongsGuessing();
                System.out.println(RED + "\nHARDCORE CHALLENGE" + RESET);
                writer.write("HARDCORE - ");
            }
            case "RANDOM" -> {
                System.out.println(BOLD + """
                    \nRANDOM songs chosen
                    Try to get the song's name, but you only have three guesses!
                    If you want to end the game, type "quit" after the song's name is revealed.
                    Typing "idk" as a guess will immediately end the game.
                    """ + RESET);

                while(TitleGuessing.titleGuessing(0, 0, true) && !INPUT.nextLine().equalsIgnoreCase("quit")) tracks++;
                tracks++;
                System.out.println(YELLOW_L + "\nRANDOM" + RESET + " songs");
                writer.write("RANDOM - ");
            }
            case "TIME ATTACK", "ZEN" -> {
                System.out.println(ITALICS + choice + RESET + " is not a game, yet!");
                writer.flush();
                return;
            }
            default -> {
                if(choice.toUpperCase().contains("SCORES")) {
                    if(choice.length() == 6) maxRankings = 5;
                    else maxRankings = Math.max(3, Math.min(Integer.parseInt(choice.substring(7)), 15));
                    organizeScores();
                    writer.flush();
                    return;
                }
                System.out.println(ITALICS + choice + RESET + " is not a game!");
                writer.flush();
                return;
            }
        }

        long end = System.nanoTime();
        double minutes = (end - start) / 60000000000d;
        long timeFormat = minutes == 0 ? 1000000000L : 60000000000L;
        int score;
        if(hardcore) {
            System.out.println("Completed: " + tracks + " tracks");
            score = (int) (11.1d * tracks - minutes + 1);
        } else {
            System.out.format("""
                    Tracks: %s
                    Correct: %s
                    Incorrect: %s
                    Lines Given: %s""", tracks, correct, incorrect, lines);
            score = (int) (10 * correct - 2 * incorrect - lines - minutes);
        }
        System.out.println("\nTime: " + (end - start) / timeFormat + " " + (timeFormat == 1000000000L ? "second" : "minute") + ((end - start) / timeFormat != 1 ? "s" : ""));
        System.out.println(GREEN + "Score: " + score);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        writer.write(score + " (" + tracks + ") " + dateFormat.format(new Date()) + "\n");
        writer.flush();
    }

    public static void organizeScores() {
        List<String> leaderboard = new ArrayList<>();
        leaderboard.add(scores.get(0));

        for(int i = 1; i < scores.size(); i++) {
            int score = getScoringInfo(scores.get(i), true);

            for(int j = i - 1; j >= 0; j--) {
                int exScore = getScoringInfo(leaderboard.get(j), true);
                if(score < exScore) {
                    leaderboard.add(j + 1, scores.get(i));
                    break;
                } else if(score == exScore) {
                    for(int k = j; k >= 0; k--) {
                        exScore = getScoringInfo(leaderboard.get(k), true);
                        if(score < exScore || getScoringInfo(scores.get(i), false) >= getScoringInfo(leaderboard.get(j), false)) {
                            leaderboard.add(k + 1, scores.get(i));
                            break;
                        } else if (k == 0) leaderboard.add(0, scores.get(i));
                    }

                    break;
                } else if(j == 0) leaderboard.add(0, scores.get(i));
            }
        }

        while(leaderboard.size() > maxRankings) leaderboard.remove(maxRankings);

        for(int i = 0; i < leaderboard.size(); i++) {
            String scoring = leaderboard.get(i);
            String spacedScoring = scoring.substring(0, scoring.indexOf(")") + 2);
            spacedScoring += " " + "-".repeat(23 - spacedScoring.length()) + "  " + scoring.substring(scoring.indexOf(")") + 2);
            if(spacedScoring.contains("HARDCORE")) spacedScoring = RED + "HARDCORE" + RESET + spacedScoring.substring(spacedScoring.indexOf(" - "));

            leaderboard.set(i, spacedScoring);
        }

        System.out.println(GREEN + "\nTOP " + maxRankings + " SCORES" + RESET + ":" + BOLD + "\n    MODE - score (tracks)     Date" + RESET);
        for(int i = 0; i < leaderboard.size(); i++) System.out.println((i + 1) + (i > 8 ? ". " : ".  ") + leaderboard.get(i));
    }

    public static int getScoringInfo(String text, boolean score) {
        return Integer.parseInt(score ? text.substring(text.indexOf("- ") + 2, text.indexOf("(") - 1) : text.substring(text.indexOf("(") + 1, text.indexOf(")")));
    }
}
