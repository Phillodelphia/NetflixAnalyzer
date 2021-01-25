package se.nackademin;

public class Episode {
    String title;
    String date;
    String episode;
    String season;

    // constructs a new episode with date
    public static Episode constructEpisode(String[] parsedTitle) throws Exception {

        // needed to trim the double citation marks from the reader
        parsedTitle[0] = parsedTitle[0].replaceAll("\"", "");
        parsedTitle[1] = parsedTitle[1].replaceAll("\"", "");

        String[] title = parsedTitle[0].split(":");
        if (title.length > 3) {
            title = cleanUpTitle(title);
        }
        switch (title.length) {
            case 1:
                return new Episode(title[0], parsedTitle[1]);

            case 2:
                return new Episode(title[0], title[1], parsedTitle[1]);

            case 3:
                return new Episode(title[0], title[1], title[2], parsedTitle[1]);

            default:
                System.out.println("Not a netflix entry");
                throw new Exception();
        }
    }

    Episode(String title, String season, String episode, String date) {
        this.title = title;
        this.season = season;
        this.episode = episode;
        this.date = date;
    }

    Episode(String title, String season, String date) {
        this.title = title;
        this.season = season;
        this.date = date;
    }

    Episode(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDate() {
        return this.date;
    }

    public String getSeason() {
        return this.season;
    }

    public String getEpisode() {
        return this.episode;
    }

    // if the title doesn't match the usual netflix CSV format
    public static String[] cleanUpTitle(String[] title) {
        String cTitle = title[0];
        String cSeason = title[1];
        String cEpisode = "";
        int titleLength = title.length - 1;

        for (int i = 2; i <= titleLength; i++) {
            cEpisode += title[i];
        }
        String[] newTitle = new String[] { cTitle, cSeason, cEpisode };

        return newTitle;
    }
}
