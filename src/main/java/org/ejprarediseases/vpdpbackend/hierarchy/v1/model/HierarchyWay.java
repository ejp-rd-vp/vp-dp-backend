package org.ejprarediseases.vpdpbackend.hierarchy.v1.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
@Getter
public enum HierarchyWay {
    UP("up"),
    DOWN("down");
    private final String value;

    public static HierarchyWay getNameFromValue(String value) {
        switch (value.toLowerCase()) {
            case "up":
                return UP;
            case "down":
                return DOWN;
            default:
                throw new NoSuchElementException(
                        value + " is not found. Acceptable values: " + names());

        }
    }
    public static List<String> names() {
        String valuesStr = Arrays.toString(HierarchyWay.values());
        return Arrays.stream(
                valuesStr.substring(1, valuesStr.length()-1)
                        .replace(" ", "").split(",")).toList();
    }

    public static String pattern() {
        String valuesStr = Arrays.toString(HierarchyWay.values());
        return valuesStr.substring(1, valuesStr.length()-1)
                .replace(" ", "|").replace(",", "");
    }
}
