package se.nackademin;

import java.io.Reader;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

public class App {
    static Scanner sc = new Scanner(System.in);
    static NetflixFile nf;

    public static void main(String[] args) {

        nf = new NetflixFile();

        try {
            Reader fr = new FileReader("./Nfiles/NetflixViewingHistory.csv");

            // loads and parses the CSV file
            nf.loadFile(fr);

            // gets the loaded data and sort them by date
            nf.loadHistory();
            
        } catch (Exception e) {
            System.out.println(e);
        }

        welcomeMessage();

        while (!menu(sc.nextLine())) {
            welcomeMessage();
        }

    }

    //main menu method
    public static boolean menu(String command) {
        
        if ("search".equalsIgnoreCase(command) || "1".equals(command)) {
            while (true) {
                System.out.println("Search in your history file. type [_back_] to go back");
                String input = sc.nextLine();
                if (!"_back_".equalsIgnoreCase(input)) {
                    nf.getSpecificTitle(input.toLowerCase());
                    continue;
                }
                break;
            }
        } 
        else if ("search date".equalsIgnoreCase(command) || "2".equals(command)) {
            while (true) {
                System.out.println("Search in your history file. type [_back_] to go back");
                String input = sc.nextLine();
                if (!"_back_".equalsIgnoreCase(input)) {
                    nf.getSpecificDate(input);
                    continue;
                }
                break;
            }
        } 
        else if ("statistics".equalsIgnoreCase(command) || "3".equals(command)) {
            Episode ep = nf.getFirstHistory();
            System.out.println("----The latest movie/Episode you watched was----");
            System.out.println(ep.title + " " + ep.season + " " + ep.episode + " - " + ep.date + "\n");

            List<Episode> topList = nf.getAmountHistory(10);
            System.out.println("----The latest 10 movies/Episodes you watched was----");
            topList.forEach(x -> System.out.println(x.title + ": " + x.episode));

            System.out.println("Adding data to file [Statistics.txt]");
            nf.addStatistics();
        } 
        else if ("all".equalsIgnoreCase(command) || "4".equals(command)) {
            nf.getHistory();
            System.out.println("----Printed everything from history----");
        } 
        else if ("random".equalsIgnoreCase(command) || "5".equals(command)) {
            Episode ep = nf.getRandom();
            System.out.println(ep.title + ": " + ep.season + ": \"" + ep.episode + "\" - " + ep.date);
        } 
        else if ("all_views".equalsIgnoreCase(command) || "6".equals(command)) {
            System.out.println("Do you want to sort by Episodes watched or Seasons watched? [E] or [S]");
            String input = sc.nextLine();

            if ("E".equalsIgnoreCase(input)) {
                nf.printSortedEpisodes();
            } else if ("S".equalsIgnoreCase(input)) {
                nf.printSortedSeason();
            }
        } 
        else if ("help".equalsIgnoreCase(command)) {
            welcomeMessage();
        } 
        else if ("quit".equalsIgnoreCase(command)) {
            System.out.println("Quitting program...");
            return true;
        } 
        else {
            System.out.println("Unknown command");
        }
        return false;
    }

    public static void welcomeMessage() {
        System.out.println("----Welcome to the Netflix Analyzer----\n"
                + "type [1] or [search] to search for a series in the list\n"
                + "type [2] or [search date] to search for a series in the list based on date\n"
                + "type [3] or [statistics] to see your latest watches\n"
                + "type [4] or [all] to list all series/movies you've watched\n"
                + "type [5] or [random] to get a random title\n" + "type [6] or [totalviews] to list watched per show\n"
                + "type [help] if you want to show the commands again\n" + "type [quit] to exit the program.");
    }
}
