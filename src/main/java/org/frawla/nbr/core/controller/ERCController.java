package org.frawla.nbr.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frawla.nbr.core.business.Importer;
import org.frawla.nbr.core.business.NBRparser;

public class ERCController
{
    private final Map<String, String> resultsMap;

    public ERCController(final List<String> selectedFileList)
    {
        assert null != selectedFileList : "Parameter 'selectedFileList' of method 'ERCController' must not be null";

        final Importer importer = new Importer(selectedFileList);
        /** key: File Name. */
        Map<String, String> rawFileContentMap = importer.getFileContentMap();
        resultsMap = new HashMap<String, String>(rawFileContentMap.size());
        rawFileContentMap.forEach((k, v) -> resultsMap.put(k, NBRparser.getResult(rawFileContentMap.get(k))));
    }

    public void export(final IExporter exporter)
    {
        exporter.export(resultsMap);
    }
}