package org.frawla.nbr.core.exporting;

import org.frawla.nbr.core.controller.IExporter;

/**
 * @author sami
 */
public abstract class AbstractExporter implements IExporter
{
    private String formattedString;

    protected AbstractExporter()
    {
        super();
    }

    public String getFormattedString()
    {
        return formattedString;
    }

    public void setFormattedString(final String formattedText)
    {
        this.formattedString = formattedText;
    }
}
