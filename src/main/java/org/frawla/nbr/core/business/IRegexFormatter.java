package org.frawla.nbr.core.business;

public interface IRegexFormatter
{
    /**
     * @param rawValue
     *            : String Value to be processed by Regex.
     * @return : formatted String after Regex processing.
     */
    String formatUsingRegex(final String rawValue);
}