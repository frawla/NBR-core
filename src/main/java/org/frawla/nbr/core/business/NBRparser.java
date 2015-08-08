package org.frawla.nbr.core.business;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.frawla.nbr.core.common.Constants;
import org.frawla.nbr.core.common.Util;

/**
 * Network Backup-files Regex Parser A singleton class to parse a text file holding Network configuration and convert it into formatted text sperated
 * with tabs and newlines.
 * 
 * @author Sami A. Alfattani
 */
public class NBRparser
{
    private static NBRparser SingleToneObject = new NBRparser();

    private NBRparser()
    {
        super();
    }

    public static NBRparser getSingletoneObject()
    {
        return SingleToneObject;
    }

    public static String getResult(final String strFileContent)
    {
        StringBuilder str = new StringBuilder();
        String s = Constants.EMPTY_STRING;
        Pattern p;
        Matcher m;

        // Host Name
        p = Pattern.compile("hostname ([\\w-]+)\\n" , Pattern.MULTILINE);
        m = p.matcher(strFileContent);
        str.append(printResult(m, "Host Name:\t%s" + System.lineSeparator(), 1));

        // license
        p = Pattern.compile("(?sm)license ([\\w ]+)\\n");
        m = p.matcher(strFileContent);
        str.append(printResult(m, "License: \t%s\n", 1));

        // Users
        p = Pattern.compile("(?sm)username (\\w+) privilege (\\d{1,2})");
        m = p.matcher(strFileContent);
        str.append(printResult(m, "%s\t%s\tFULL\t Crypto\n", 1, 2));

        //make all info. of each interface in one line. 
        p = Pattern.compile("(?sm)^interface.*?!");
        m = p.matcher(strFileContent);

        StringBuilder sb = new StringBuilder();
        while (m.find())
        {
            sb.append(m.group(0).replace("\r", " ").replace("\n", " ")).append("\n");
        }

        //find and grouping interfaces
        p = Pattern
                .compile("(?m)^interface (GigabitEthernet[\\d\\./]+|Loopback\\d|Vlan\\d+)(?=\\s+?)(?:\\s+description (.*?)(?=\\s+?))?(?:.*?(no ip address|ip address [\\d.]+?\\s+?[\\d.]+?)(?=\\s+?))?(?:.*?(?:(?=!)|media-type (.*?))(?=\\s+?))?(?=.*?!)");
        m = p.matcher(sb);

        //Convert subnet Mask into ICDR.
        s = printResult(m, "Interface: \t%s\t%s\t%s\t%s\n", 1, 2, 3, 4).toString();
        //Looking for subnet mask only
        m = Pattern.compile("(?sm)ip address [\\d\\.]+ ([\\d\\.]+)").matcher(s);

        s = ChangeAll(m, s, (msk) -> "/" + Util.convertSubnetMaskToCIDR(msk) + "");
        str.append(s);

        str.append("Protocols:\n");
        // vrrrp
        p = Pattern.compile("^ (vrrp.*?) ip ([\\d\\. ]+)", Pattern.MULTILINE + Pattern.DOTALL);
        m = p.matcher(strFileContent);
        str.append(printResult(m, "vrrp:\t%s\t%s\n", 1, 2));

        // ospf
        p = Pattern.compile("ospf.*?(router-id [\\d\\.]+).*?\\r\\n.*?(( network ([\\d+.]+).*?\\r\\n){1,5})", Pattern.MULTILINE + Pattern.DOTALL);
        m = p.matcher(strFileContent);
        str.append(printResult(m, "ospf: \t%s\t%s\n", 1, 2));

        // Access-List
        p = Pattern.compile("(?sm)(ip access-list .*?)");
        m = p.matcher(strFileContent);
        str.append(printResult(m, "Access-List: \t%s\n", 1));

        p = Pattern.compile("(?sm)((permit) ((\\d+\\.){3}\\d+)) log");
        m = p.matcher(strFileContent);
        str.append(printResult(m, "\t%s\n", 1));

        // snmp-server
        p = Pattern.compile("(?sm)(snmp-server) host ((\\d+\\.){3}\\d+)");
        m = p.matcher(strFileContent);
        if (m.find())
            str.append("snmp-server: \t");
        p = Pattern.compile("(?sm)snmp-server host ((\\d+\\.){3}\\d+)");
        m = p.matcher(strFileContent);
        str.append("host: ");
        str.append(printResult(m, "%s, ", 1));

        return str.toString();
    }

    private static StringBuilder printResult(final Matcher matcher, final String format, final int... GroupNumbers)
    {
        assert matcher != null : "mater in PrintResult must not be null";
        StringBuilder str = new StringBuilder();
        while (matcher.find())
        {
            final String[] values = new String[GroupNumbers.length];
            for (int i = 0; i < GroupNumbers.length; i++)
            {
                values[i] = matcher.group(GroupNumbers[i]);
            }
            str.append(String.format(format, (Object[]) values));
        }
        return str;
    }

    /**
     * As an Example you can fill these parameters: String text = "no way oh my god it cannot be"; Matcher m =
     * Pattern.compile("\\b\\w{3,}\\b").matcher(text);
     * 
     * @param matcher
     * @param text
     * @param ireg
     * @return
     */
    private static String ChangeAll(final Matcher matcher, final String text, final IRegexFormatter ireg)
    {
        final StringBuilder sb = new StringBuilder();
        int last = 0;
        while (matcher.find())
        {
            String s = Constants.EMPTY_STRING;
            sb.append(text.substring(last, matcher.start()));
            s = matcher.group(0);
            s = s.replace(matcher.group(1), ireg.formatUsingRegex(matcher.group(1)));
            sb.append(s);
            last = matcher.end();
        }
        sb.append(text.substring(last));

        return sb.toString();
    }
}//end class