package org.frawla.nbr.core.business;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.frawla.nbr.core.business.Importer;
import org.frawla.nbr.core.business.NBRparser;
import org.frawla.nbr.core.common.Constants;
import org.frawla.nbr.core.common.Util;
import org.junit.BeforeClass;
import org.junit.Test;

import de.schlichtherle.truezip.file.TFile;

public class TestNBRparser
{
    private static String strFileContent;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        Map<String, String> backupFileContentList;
        List<String> l = new ArrayList<>();
        String tstfile = (Constants.TEST_RESOUCES_PATH + "/R3-Rotuer_3800_WAN-RTR-1-Win.txt").replace("/", java.io.File.separator);
        l.add(tstfile);
        Importer im = new Importer(l);
        backupFileContentList = im.getFileContentMap();
        strFileContent = backupFileContentList.get(tstfile);
    }

    @Test
    public void TestgetResult()
    {
        final String fPath = Util.getTestResourcesPath("/expected_output.txt");
        final String actual = NBRparser.getResult(strFileContent);
        final String expected = Util.readFileAsString(new TFile(fPath));

        assertEquals("expecte '" + expected + "' should be equal to actual '" + actual + "'",
                expected.trim().replaceAll("\n", "").replaceAll("\r", ""), actual.trim().replaceAll("\n", "").replaceAll("\r", ""));
    }

    @Test
    public void TestPrintResult()
    {
        final Pattern ptrn = Pattern.compile("(k.*d)", Pattern.MULTILINE + Pattern.DOTALL);
        final Matcher mchr = ptrn.matcher("this text is to match a keyword in this text");

        try
        {
            final String expected = "Hello keyword";
            final Method method = NBRparser.class.getDeclaredMethod(
                    /* Private Mehtod Name */       "printResult",
                    /* List of Parameter Types */   Matcher.class, String.class, int[].class);
            method.setAccessible(true);
            final StringBuilder actual = (StringBuilder) method.invoke(
                    /* an object to invoke the method */    NBRparser.getSingletoneObject(),
                    /* List of Arguments */                 mchr, "Hello %s", new int[] { 1 });
            assertEquals(expected, actual.toString());
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
}
