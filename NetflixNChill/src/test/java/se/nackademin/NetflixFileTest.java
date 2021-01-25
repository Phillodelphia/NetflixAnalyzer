package se.nackademin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.io.*;

public class NetflixFileTest {
    NetflixFile nf;
    Reader reader;

    @BeforeEach
    public void init() throws Exception {
        nf = new NetflixFile(); 
        this.reader = new StringReader("Title: Date\nTest: Season 1: Episode 1, \"2021-01-20\"\nTest: Season 2: Episode 2, \"2021-01-20\"\nTesto: Season 1: Episode 1, \"2021-01-21\"");
        nf.loadFile(reader);
        nf.loadHistory();
    }

    @Test
    public void testLoadFile() {
        Reader reader = new StringReader("Title: Date\nTest: Season 1: Episode 1, \"2021-01-20\"");
        try {
        nf.loadFile(reader);
        assertEquals("Test", nf.titles.get("test").get(0).getTitle());
        } catch(Exception e) {}
    }

    @Test
    public void testAddEpisode() {
        String[] parse = new String[] {"Test: Season 1: Episode 1", "2020-01-20"};
        try {
        Episode ep = Episode.constructEpisode(parse);
        nf.addEpisode(ep);
        assertEquals(true, nf.titles.containsKey("test"));
        } catch(Exception e) {}
    }

    @Test
    public void testCheckTitle() {
        String[] parse = new String[] {"Test: Season 1: Episode 1", "2020-01-20"};
        
        assertEquals(parse, nf.checkTitle(parse));
    }

    @Test
    public void testCheckTitleWith3Indexes() {
        String[] parse = new String[] {"Test", " death & robots: Season 1: Episode 1", "2020-01-20"};
        
        assertEquals("Test death & robots: Season 1: Episode 1", nf.checkTitle(parse)[0]);
    }

    @Test
    public void testLoadHistory() {
        assertEquals("Testo", nf.latestWatched.get(0).title);
    }

    @Test
    public void testGetFirst() {
        assertEquals("Testo", nf.getFirstHistory().title);
    }

    @Test
    public void testGetAmount() {
        List<Episode> testData = nf.getAmountHistory(3);
        assertEquals(true, testData.size() == 3);
    }

    @Test
    public void testGetTotalViews() {
        
        assertEquals(3, nf.getTotalViews());
    }

    @Test
    public void testGetTotalSeasons() {
        
        assertEquals(2, nf.getTotalShows());
    }

    @Test 
    public void testAddEpisodeToExisting() {
        NetflixFile nf2 = new NetflixFile();
        String[] parse = new String[] {"Test: Season 1: Episode 1", "2020-01-20"};
        String[] parse2 = new String[] {"Test: Season 2: Episode 1", "2020-01-21"};
        try {
        Episode ep = Episode.constructEpisode(parse);
        nf2.addEpisode(ep);
        Episode ep2 = Episode.constructEpisode(parse2);
        nf2.addEpisode(ep2);
        assertEquals(true, nf2.titles.get("test").size() == 2);
        } catch(Exception e) {}
    }

    @Test
    public void testInvalidFile() {
        NetflixFile nf2 = new NetflixFile();
        Reader reader = new StringReader("Title: Date");
        try {
            nf2.loadFile(reader);
            fail();
        }catch (Exception e) {}

    }
}
