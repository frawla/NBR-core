package org.frawla.nbr.core.exporting;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.frawla.nbr.core.common.Util;
import org.junit.Before;
import org.junit.Test;

import de.schlichtherle.truezip.file.TFile;

public class Test_SheetExporter
{
    @Before
    public void setup()
    {
        //TODO please remove if this is not necessary
    }

    @Test
    public void Test_Export()
    {
        Map<String, String> resultMap = new HashMap<>();
        String str = Util.readFileAsString(new TFile(Util.getTestResourcesPath("/expected_output.txt")));
        resultMap.put(Util.getMainResourcesPath("/this file name.txt"), str);
        final SheetExporter sh = new SheetExporter();
        sh.export(resultMap);
        assertNotNull(sh.getWorkBook());
    }
}
