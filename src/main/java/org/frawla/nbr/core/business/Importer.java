package org.frawla.nbr.core.business;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frawla.nbr.core.common.Util;

import de.schlichtherle.truezip.file.TFile;

public class Importer
{
    private List<String> fileNames = new ArrayList<>();
    private Map<String, String> fileContentMap = new HashMap<>();

    public Importer()
    {
        super();
    }

    /**
     * Adds the list of files into {@code Importer#fileContentMap}
     * 
     * @param listOfTextAndZipFiles
     */
    public Importer(List<String> listOfTextAndZipFiles)
    {
        fileNames = listOfTextAndZipFiles;
        for (String fn : fileNames)
        {
            TFile tf = new TFile(new File(fn));
            if (tf.exists())
            {
                System.out.println("exists");
            }
            else
            {
                System.err.println("File '" + tf + "' does not exist");
            }
            addFile(tf);
        }
    }

    /**
     * @return the map that maps a FileName (key) and its raw content as a string (value).
     */
    public Map<String, String> getFileContentMap()
    {
        return fileContentMap;
    }

    public void addFile(TFile tFile)
    {
        if (tFile.isDirectory())
        {
            TFile dir = new TFile(tFile.getPath());
            for (TFile member : dir.listFiles())
                addFile(member);
        }
        else if (tFile.isFile())
        {
            fileContentMap.put(tFile.getPath(), Util.readFileAsString(tFile));
        }
        // else is special file or non-existent
    }

}//end class