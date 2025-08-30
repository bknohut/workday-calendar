package org.berkenohut.workdaycalendar;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class HolidayCalendar
{

    private Set<Calendar> holidays;

    public HolidayCalendar() 
    {
        holidays = new HashSet<>();
    }

    private void removeTimeFromDate(Calendar date)
    {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
    }

    public void setHoliday(final Calendar date)
    {
        Calendar d = date;
        removeTimeFromDate(d);
        holidays.add(d);

        System.out.println("Added holiday: " + d.getTime());
    }

    public void setRecurringHoliday(final Calendar date)
    {
        Calendar d = date;
        removeTimeFromDate(d);
        d.set(Calendar.YEAR, 0);
        holidays.add(d);

        //System.out.println("Added holiday: " + d.getTime());
    }

    public boolean isHoliday(final Calendar date)
    {
        // check if it is weekend
        int day = date.get(Calendar.DAY_OF_WEEK);
        if(day == Calendar.SUNDAY || day == Calendar.SATURDAY)
            return true;

        Calendar d = date;
        removeTimeFromDate(d);

        // check if it is holiday
        boolean isHoliday = holidays.contains(date);
        if(isHoliday) 
            return true;

        // recurring holidays have their year value set to 0
        d.set(Calendar.YEAR, 0);
        isHoliday = holidays.contains(d);
        //System.err.println("Checking recurring: " + d.getTime());
        return isHoliday;
    }
}