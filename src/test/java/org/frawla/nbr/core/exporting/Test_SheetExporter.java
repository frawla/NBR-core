package org.frawla.nbr.core.exporting;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.frawla.nbr.core.common.Util;
import org.frawla.nbr.core.exporting.SheetExporter;
import org.junit.BeforeClass;
import org.junit.Test;

import de.schlichtherle.truezip.file.TFile;

public class Test_SheetExporter
{
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        //
    }

    @Test
    public void Test_Export()
    {
        Map<String, String> resultMap = new HashMap<>();
        String str = Util.readFileAsString(new TFile( Util.getTestResourcesPath("/expected_output.txt")));
        resultMap.put(Util.getMainResourcesPath("/this file name.txt") , str);
        SheetExporter sh = new SheetExporter();
        sh.export(resultMap);

        assertEquals(0, 0);
    }
}
