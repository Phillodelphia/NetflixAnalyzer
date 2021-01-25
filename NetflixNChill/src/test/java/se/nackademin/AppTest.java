package se.nackademin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void testInputMenu()
    {
        String input = "_back_";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        assertEquals(false, App.menu("1"));
    }
    
    @Test
    public void testInvalidInputMenu()
    {
        assertEquals(false, App.menu("t"));
    }

    @Test
    public void testExitMenu()
    {
        assertEquals(true, App.menu("quit"));
    }
}
