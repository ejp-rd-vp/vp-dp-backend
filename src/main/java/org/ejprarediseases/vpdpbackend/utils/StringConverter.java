package org.ejprarediseases.vpdpbackend.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringConverter {

    static Logger logger = LoggerFactory.getLogger(StringConverter.class);

    /**
     * Converts a JSON-formatted string to a HashMap of key-value pairs.
     *
     * @param input The JSON-formatted string to be converted to a HashMap.
     * @return A HashMap containing key-value pairs parsed from the input string.
     * @throws IllegalArgumentException If the input string is not a valid JSON format.
     */
    public static Map<String, String> toHashMap(String input) {
        final ObjectMapper mapper = new ObjectMapper();
        Map<String, String> mapFromString = new HashMap<>();
        try {
            mapFromString = mapper.readValue(input, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            logger.error("Exception launched while trying to parse String to Map.",  e);
        }
        return mapFromString;
    }

    /**
     * Extracts all substrings from the input string that match the specified regular expression.
     *
     * @param input The input string from which substrings will be extracted.
     * @param regex The regular expression to match substrings against.
     * @return A list of substrings that match the given regular expression.
     */
    public static List<String> getAllSubstringsMatchingRegex(String input, String regex) {
        List<String> substrings = new ArrayList<>();
        if (input != null) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(input);
            while (m.find()) {
                substrings.add(m.group(1));
            }
        }
        return  substrings;
    }
}
