package org.ejprarediseases.vpdpbackend.notification.v1.handler;

import java.util.List;

public class HtmlHandler {

    /**
     * Converts a list of values into an HTML table row.
     *
     * This method takes a list of string values and converts each value into a table cell
     * (&lt;td&gt; element). The resulting cells are concatenated to form an HTML table row
     * (&lt;tr&gt; element).
     *
     * @param values The list of string values to be converted into table cells.
     * @return A string containing an HTML table row with the converted values as table cells.
     */
    public static String listToTableRow(List<String> values) {
        values = values.stream().map(value -> "<td>" + value + "</td>").toList();
        return "<tr>" + String.join("", values) + "</tr>";
    }
}
