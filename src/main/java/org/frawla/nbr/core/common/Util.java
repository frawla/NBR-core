package org.frawla.nbr.core.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import de.schlichtherle.truezip.file.TFile;

public final class Util
{
    private Util()
    {
        super();
    }

    
    public static String getTestResourcesPath(final String filePath){
        return (Constants.TEST_RESOUCES_PATH + filePath).replace("/", java.io.File.separator);
    }
    
    public static String getMainResourcesPath(final String filePath){
        return (Constants.MAIN_RESOUCES_PATH + filePath).replace("/", java.io.File.separator);
    }
    public static boolean openFileInDefalutApplication(final String appPath)
    {
        boolean failed = false;
        try
        {
            //TODO switch on OS type
            /**
             * default implementation -> case: Windows
             */
            execute1(appPath);
        }
        catch (IOException | SecurityException | NullPointerException | IllegalArgumentException e)
        {
            //log
            failed = true;
        }

        if (failed)
        {
            failed = execute2(appPath);
        }
        
        return failed;
    }

    private static void execute1(final String appPath) throws IOException, SecurityException, NullPointerException, IllegalArgumentException
    {
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("rundll32 url.dll,FileProtocolHandler \"" + appPath + "\"");
    }

    private static boolean execute2(final String appPath)
    {
        try
        {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(appPath);

        }
        catch (IOException | SecurityException | NullPointerException | IllegalArgumentException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getFileWithoutExtention(final String path)
    {
        String fn = path.replaceAll("^.*\\\\(.*)\\.([^.]+)$", "$1");
        return fn;
    }

    public static int convertSubnetMaskToCIDR(final String netmask)
    {
        InetAddress m = null;
        try
        {
            m = InetAddress.getByName(netmask);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        final byte[] netmaskBytes = m.getAddress();
        int cidr = 0;
        boolean zero = false;
        for (byte b : netmaskBytes)
        {
            int mask = 0x80;

            for (int i = 0; i < 8; i++)
            {
                int result = b & mask;
                if (result == 0)
                {
                    zero = true;
                }
                else if (zero)
                {
                    throw new IllegalArgumentException("Invalid netmask.");
                }
                else
                {
                    cidr++;
                }
                mask >>>= 1;
            }
        }
        return cidr;
    }

    /**
     * @param tFile
     *            : a text file
     * @return: the file content as a String.
     * @throws IOException
     */
    public static String readFileAsString(final TFile tFile)
    {
        return readFileAsString(tFile, System.lineSeparator());
    }

    public static String readFileAsString(final TFile tFile, final String LineSeperator)
    {
        File tmp = null;
        try
        {
            //Extract the file into temporary Dir.
            tmp = TFile.createTempFile("Samitemp", null, new File(System.getProperty("java.io.tmpdir")));
            tmp.deleteOnExit();
            TFile.cp(tFile, tmp);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //final close all open objects
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(tmp)));)
        {
            //read the extraxcted file normally.
            String line = null;
            StringBuilder str = new StringBuilder();

            if ((line = in.readLine()) != null)
                str.append(line.replace("\n", "").replace("\r", ""));

            while ((line = in.readLine()) != null)
            {
                str.append(LineSeperator + line.replace("\n", "").replace("\r", ""));
            }
            return str.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return Constants.EMPTY_STRING;
    }

}// end class
