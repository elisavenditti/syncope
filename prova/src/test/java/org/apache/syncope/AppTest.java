package org.apache.syncope;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        App a = new App();
        int c = a.diff(5,2);
        assertEquals(3, c);
    }
}
