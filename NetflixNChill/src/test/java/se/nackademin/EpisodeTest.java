package se.nackademin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class EpisodeTest {
    Episode ep;

    @BeforeEach
    public void init() throws Exception {
        String[] testTitle = new String[] {"Test: Season 1: Episode 1", "2020-01-20"};
        this.ep = Episode.constructEpisode(testTitle);
    }

    @Test
    public void testConstructing() {
        String[] testTitle = new String[] {"Test: Season 1: Episode 1", "2020-01-20"};
        try {
        Episode testEp = Episode.constructEpisode(testTitle);
        
        assertEquals("Test", testEp.getTitle());
        } 
        catch (Exception e) {fail();}
    }

    @Test 
    public void testCleanUpTitle() {
        String[] testTitle = new String[] {"Test: Season 1: Episode 1: test"};
        String[] testParsed = testTitle[0].split(":");
        testTitle = Episode.cleanUpTitle(testParsed);
        assertEquals(" Episode 1 test", testTitle[2]);
    }

    @Test
    public void testGetTitle() {
        assertEquals("Test", ep.getTitle());
    }

    @Test
    public void testGetDate() {
        assertEquals("2020-01-20", ep.getDate());
    }

    @Test
    public void testGetSeason() {
        assertEquals(" Season 1", ep.getSeason());
    }

    @Test
    public void testGetEpisode() {
        assertEquals(" Episode 1", ep.getEpisode());
    }

    @Test
    public void testInvalidConstruct() {
        String[] testTitle = new String[] {null};
        try { Episode.constructEpisode(testTitle); fail();}
        catch (Exception e) {}
    }
}
