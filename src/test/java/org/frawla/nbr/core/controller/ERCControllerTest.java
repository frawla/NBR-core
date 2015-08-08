package org.frawla.nbr.core.controller;

import java.util.ArrayList;
import java.util.List;

import org.frawla.nbr.core.controller.ERCController;
import org.frawla.nbr.core.exporting.SheetExporter;
import org.junit.Test;

public class ERCControllerTest
{
    @Test
    public void exportTest()
    {
        String fzip = "./test/res/R3-Rotuer 3800 WAN-RTR-1-Win.txt";
        List<String> lst = new ArrayList<>();
        lst.add(fzip);
        ERCController er  = new ERCController(lst);
        er.export(new SheetExporter());
    }
}
