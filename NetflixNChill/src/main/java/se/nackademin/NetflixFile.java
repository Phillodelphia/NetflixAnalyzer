package se.nackademin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static java.util.Comparator.comparingInt;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class NetflixFile {

    HashMap<String, List<Episode>> titles = new HashMap<>();
    List<Episode> latestWatched;

    // load the CSV netflix file and create a list
    public void loadFile(Reader reader) throws Exception {

        try (BufferedReader br = new BufferedReader(reader)) {
            String row;
            br.readLine();
            while ((row = br.readLine()) != null) {
                String[] data = row.split(",");
                data = checkTitle(data);

                Episode newEp = Episode.constructEpisode(data);
                addEpisode(newEp);
            }
            if (titles.size() < 1) {
                System.out.println("Error file doesn't contain any series or this is an invalid file");
                throw new Exception("Invalid file");
            } else {
                titles.forEach((K, V) -> {
                    Collections.reverse(titles.get(K));
                });
            }
        } catch (Exception e) {e.getStackTrace();}
    }

    public void addStatistics() {

        File directory = new File("./Nfiles/Statistic");
        if (!directory.exists()) {
            directory.mkdir();
        }

        try (FileWriter myWriter = new FileWriter(directory + "/Statistics.txt")) {
            Episode latest = getFirstHistory();
            List<Episode> latestList = getAmountHistory(10);
            myWriter.write("Total amount of Episodes/Movies: " + getTotalViews() + "\nTotal amount of Shows watched: "
                    + getTotalShows() + "\n\n----Latest watched----\n" + latest.title + " " + latest.season + " "
                    + latest.episode + "\n\n----Latest 10 watched----\n");
            latestList.forEach(x -> {
                try {
                    myWriter.write(x.title + " " + x.season + " " + x.episode + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add an Episode instance to hashmap if the key exist add it to the same key
    public void addEpisode(Episode ep) {

        String title = ep.title.toLowerCase();

        if (this.titles.containsKey(title)) {
            List<Episode> episodeList = this.titles.get(title);
            episodeList.add(ep);
            this.titles.put(title, episodeList);
        } else {
            List<Episode> episodeList = new ArrayList<Episode>();
            episodeList.add(ep);
            this.titles.put(title, episodeList);
        }
    }

    // Check if title has any uneccessary commas
    public String[] checkTitle(String[] title) {

        if (title.length > 2) {
            String date = title[title.length - 1];
            String content = "";

            for (int i = 0; i < title.length - 1; i++) {
                content += title[i];
            }

            String[] newarray = { content, date };
            return newarray;
        }
        return title;
    }

    // get every series/movies in the list
    public void getHistory() {

        for (String val : this.titles.keySet()) {
            System.out.println(val);
        }
    }

    // gets a series/movie based on user input
    public void getSpecificTitle(String input) {

        //check if input exists in the titles dictionary
        List<String> filteredList = this.titles.keySet().stream().filter(x -> x.toLowerCase().contains(input))
                .collect(Collectors.toList());
        
        if (filteredList.size() <= 1) {

            List<Episode> list = this.titles.get(input);
            if (list != null) {
                System.out.println("\n----" + list.get(0).title + "----");
                list.forEach(val -> {
                    System.out.println(val.season + ": " + val.episode + " - " + val.date);
                });
            } 
            else {
                System.out.println("Couldn't find a movie or series by that name.");
                if (filteredList.size() > 0) {
                    System.out.println("The closest match we found was: [" + filteredList.get(0)
                            + "]. Did you perhaps make a typo?");
                }
            }
        } 
        else {
            System.out.println("----Relevant searches----");
            filteredList.forEach(val -> System.out.println(val));
        }

    }

    public void getSpecificDate(String input) {

        List<Episode> temp = latestWatched.stream().filter(x -> x.date.startsWith(input)).collect(Collectors.toList());

        temp.forEach(x -> System.out.println(x.title + " " + x.date));

    }

    // Loads the latest movie
    public void loadHistory() {

        List<List<Episode>> tempList = new ArrayList<>(this.titles.values());
        this.latestWatched = tempList.stream()
                .collect(ArrayList<Episode>::new, List<Episode>::addAll, List<Episode>::addAll).stream()
                .sorted(Comparator.comparing(Episode::getDate)).collect(Collectors.toList());

        Collections.reverse(this.latestWatched);
    }

    // Print sorted by amount of seasons watched
    public void printSortedSeason() {

        HashMap<String, List<String>> seasonList = new HashMap<>();

        latestWatched.forEach(x -> {
            if (!seasonList.containsKey(x.title)) {
                List<String> tempList = new ArrayList<>();
                tempList.add(x.season);
                seasonList.put(x.title, tempList);
            } else if (!seasonList.get(x.title).contains(x.season)) {
                List<String> tempList = seasonList.get(x.title);
                tempList.add(x.season);
                seasonList.put(x.title, tempList);
            }
        });

        Map<String, List<String>> sorted = seasonList.entrySet().stream()
                .sorted(comparingByValue(comparingInt(List::size)))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
                    throw new AssertionError();
                }, LinkedHashMap::new));

        List<String> reversed = new ArrayList<String>(sorted.keySet());
        Collections.reverse(reversed);

        System.out.println("----Seasons watched per show----");
        reversed.forEach(x -> System.out.println(x + " - " + seasonList.get(x).size()));
    }

    // get a list of history sorted by views
    public void printSortedEpisodes() {

        Map<String, List<Episode>> sorted = this.titles.entrySet().stream()
                .sorted(comparingByValue(comparingInt(List::size)))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
                    throw new AssertionError();
                }, LinkedHashMap::new));
        List<String> reversed = new ArrayList<String>(sorted.keySet());
        Collections.reverse(reversed);
        System.out.println("----Episodes Watched per show----");
        reversed.forEach(x -> System.out.println(titles.get(x).get(0).title + " - " + titles.get(x).size()));

    }

    // get total amount of episodes/movies watched
    public int getTotalViews() {
        return latestWatched.size();
    }

    // get the total amound of series
    public int getTotalShows() {
        return titles.size();
    }

    // get a random episode
    public Episode getRandom() {
        Random random = new Random();
        int ranNum = random.nextInt(latestWatched.size());

        return latestWatched.get(ranNum);
    }

    // get the first movie in the history
    public Episode getFirstHistory() {
        Episode ep = this.latestWatched.get(0);

        return ep;
    }

    // get the x latest series in your list
    public List<Episode> getAmountHistory(int amount) {
        List<Episode> list = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            list.add(this.latestWatched.get(i));
        }

        return list;
    }
}
