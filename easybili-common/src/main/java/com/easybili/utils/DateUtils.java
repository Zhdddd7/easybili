package com.easybili.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.String.format;

public class DateUtils {
    public static String formatNow(String pattern) {
        // Retrieve current time
        LocalDateTime now = LocalDateTime.now();

        // format builder
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // format current time
        return now.format(formatter);
    }

    public static String getBeforeDayDate(Integer day){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(calendar.getTime());
    }

    public static List<String> getBeforeDays(Integer beforeDays) {
        LocalDate endDate = LocalDate.now();
        List<String> dateList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        for(int i = beforeDays; i >0; i--){
            dateList.add(endDate.minusDays(i).format(formatter));
        }
        return dateList;
    }


}


