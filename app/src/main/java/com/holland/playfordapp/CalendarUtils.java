package com.holland.playfordapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarUtils {
    private final Calendar cal = Calendar.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private List<Date> holidays;
    public CalendarUtils(List<Date> holidays) {
        this.holidays = holidays;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isWeekday(Date dateOfMonth) {
        Calendar cal=Calendar.getInstance();
        //LocalDate localDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), dateOfMonth);
        //Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        cal.setTime(dateOfMonth);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1 || dayOfWeek == 7) {
            return false;
        }
        return true;
    }

    public String getDayType(Date targetDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.set(2024, Calendar.FEBRUARY, 21); //Starting point known A day
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        Calendar targetCal = Calendar.getInstance();
        targetCal.setTime(targetDate);
        targetCal.set(Calendar.HOUR_OF_DAY, 0);
        targetCal.set(Calendar.MINUTE, 0);
        targetCal.set(Calendar.SECOND, 0);
        targetCal.set(Calendar.MILLISECOND, 0);

        //Check if it is on a weekend
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isWeekday(targetCal.getTime()) || isHoliday(targetDate)) {
                return "No School";
            }
        }

        int count = 0;
        //Set startCalendar to known date of an A day
        startCal.set(2024, Calendar.FEBRUARY, 21);
        while (startCal.before(targetCal)) {
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (isWeekday(startCal.getTime()) && !isHoliday(startCal.getTime())) {
                    count++;
                }
            }
        }

        return count % 2 == 0 ? "A" : "B";
    }
    public static boolean isDateHoliday(String title, String catTitle) { //Returns if a specific date is a holiday based on the title
        if (catTitle.toLowerCase().contains("holiday") || catTitle.toLowerCase().contains("no classes") || title.toLowerCase().contains("holiday") || title.toLowerCase().contains("no classes")) {
            return true;
        }
        return false;
    }
    public boolean isHoliday(Date date) { //Returns if a date is a holiday given just the Date object
        for (Date holiday : holidays) {
            if (sdf.format(date).equals(sdf.format(holiday))) {
                return true;
            }
        }
        return false;
    }
}
