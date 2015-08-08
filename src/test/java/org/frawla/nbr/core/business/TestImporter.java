/**
 * 
 */
package org.frawla.nbr.core.business;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.frawla.nbr.core.common.Constants;
import org.frawla.nbr.core.common.Util;
import org.junit.Before;
import org.junit.Test;


/**
 * @author sami
 */
public class TestImporter
{
    /**
     * @throws java.lang.Exception
     */
    private Importer im;
    private String fn = Constants.EMPTY_STRING;
    private String fzip = Constants.EMPTY_STRING;

    @Before
    public void setUpBeforeClass()
    {        
        fn =   Util.getTestResourcesPath("/normal.txt"); 
        fzip = Util.getTestResourcesPath("/pack.zip/Zipped.txt");
    }

    @Test
    public void Test_getStringList()
    {

        //Expected
        Map<String, String> mp = new HashMap<>();        
        mp.put(fn, "line 1\r\nline 2\r\nline 3");
        mp.put(fzip, "zipline 1\r\nzipline 2\r\nzipline 3");

        //Actual
        ArrayList<String> al = new ArrayList<>();
        al.add(fn);
        al.add(fzip);       
        im = new Importer(al);
        
        assertEquals(mp, im.getFileContentMap());
    }
}
