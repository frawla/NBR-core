package org.frawla.nbr.core.common;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import de.schlichtherle.truezip.file.TFile;

public class Test_Util
{
    @Test
    public void Test_getFileWithoutExtention()
    {
        File f = new File(".\\src\\res\\template.xlsx");

        String actual = Util.getFileWithoutExtention(f.getPath());
        String expected = "template";
        assertEquals(expected, actual);

    
        f = new File("./src/res/template.xlsx");
        actual = Util.getFileWithoutExtention(f.getPath());
        expected = "template";
        assertEquals(expected, actual);
    }

    @Test
    public void Test_convertNetmaskToCIDR()
    {
        int actual = Util.convertSubnetMaskToCIDR("255.255.128.0");
        int expected = 17;

        assertEquals(expected, actual);
    }

    @Test
    public void Test_readFileAsString()
    {
        final String expected = "line 1\nline 2\nline 3".replaceAll("\n", System.lineSeparator());
        
        final String fn = Util.getTestResourcesPath("/normal.txt");        
        String actual = Util.readFileAsString(new TFile(new File(fn)) );
        assertEquals(expected, actual);       
    }

    @Test
    public void getFileWithoutExtention()
    {
        final String filepath = Util.getTestResourcesPath("/normal.txt");
        final String actual = Util.getFileWithoutExtention(filepath);
        final String expected = "normal";
        assertEquals(expected, actual);
    }
}
