package com.itmo.utils;


import com.itmo.exceptions.InputFormatException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

/**
 * класс для валидации данных, вводимых пользвателем с консоли или же из файла
 */
public class FieldsValidator {
    /**
     * проверка на парсинг строки в long
     *
     * @param field            - строка
     * @param messageIfInvalid - сообщение об ошибке
     */
    public static boolean checkStringParseToLong(String field, String messageIfInvalid) {
        try {
            Long.parseLong(field);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(messageIfInvalid);
            return false;
        }
    }

    public static boolean checkStringParseToInt(String field, String messageIfInvalid) {
        try {
            Integer.parseInt(field);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(messageIfInvalid);
            return false;
        }
    }

    /**
     * проверка на парсинг строки в double
     *
     * @param field            - строка
     * @param messageIfInvalid - сообщение об ошибке
     */
    public static boolean checkStringParseToDouble(String field, String messageIfInvalid) {
        try {
            Double.parseDouble(field);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(messageIfInvalid);
            return false;
        }
    }

    /**
     * метод проверяет входит ли число в интервал
     *
     * @param number           - число
     * @param min              - нижняя граница
     * @param max              - верхняя граница
     * @param messageIfInvalid - сообщение об ошибке
     * @param canBeNull        - может ли поле быть null
     */
    public static boolean checkNumber(Long number, long min, long max, String messageIfInvalid, boolean canBeNull) {
        try {
            if (number == null && canBeNull) return true;
            if (number == null) return false;
            if (number < min || number > max) throw new InputFormatException();
            return true;
        } catch (InputFormatException e) {
            System.out.println(messageIfInvalid);
            return false;
        }
    }

    public static boolean checkDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            formatter.parse(dateStr); // "7-01-2013"
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static LocalDate getDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(dateStr);
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (ParseException e) {
            return LocalDate.now();
        }
    }

    public static boolean checkChars(String field, boolean russian, boolean numbers) {
        for (char c : field.toCharArray()) {
            if (!(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z') && !russian && !numbers) return false;
            if (!(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z') && !(c >= 'а' && c <= 'я') && !(c >= 'А' && c <= 'Я') && !numbers)
                return false;
            if (!(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z') && !russian && !(c >= '0' && c <= '9')) return false;
            if (!(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z') && !(c >= 'а' && c <= 'я') && !(c >= 'А' && c <= 'Я') && !(c >= '0' && c <= '9'))
                return false;
        }
        return true;
    }
}
