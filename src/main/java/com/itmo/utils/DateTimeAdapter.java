package com.itmo.utils;


import java.time.LocalDate;
import java.time.ZoneId;

public class DateTimeAdapter {

    public static LocalDate dateToLocalDate(java.sql.Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}
