package com.itmo.data;

import java.io.Serializable;
import java.util.Arrays;

public enum Climate implements Serializable {
    TROPICAL_SAVANNA("TROPICAL_SAVANNA"),
    OCEANIC("OCEANIC"),
    SUBARCTIC("SUBARCTIC");

    private String climateStr;

    Climate(String russian) {
        this.climateStr = russian;
    }

    /**
     * аналог valueOf только еще и сообщением об ошибке
     *
     * @param value         - строка, которую ищем
     * @param messageIfNull - сообщение, если не нашли
     */
    public static Climate getValue(String value, String messageIfNull) {
        Climate semester = Arrays.stream(Climate.values()).filter(s -> s.climateStr.equals(value)).findAny().orElse(null);
        if (semester == null) System.out.println(messageIfNull);
        return semester;
    }
}
