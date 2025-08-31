package org.berkenohut.workdaycalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

public class HolidayCalendarTest 
{
    @Test
    public void setHolidayTest()
    {
        HolidayCalendar holidayCalendar = new HolidayCalendar();

        ArrayList<GregorianCalendar> holidays = new ArrayList<>();
        holidays.add(new GregorianCalendar(2004, Calendar.MAY, 27, 0 ,0));
        holidays.add(new GregorianCalendar(2025, Calendar.AUGUST, 29, 0 ,0));
        holidays.add(new GregorianCalendar(2025, Calendar.NOVEMBER, 17, 0 ,0));
        holidays.add(new GregorianCalendar(2137, Calendar.APRIL, 12, 0 ,0));
        holidays.add(new GregorianCalendar(1995, Calendar.JULY, 30, 0 ,0));

        for (Object date : holidays)
        {
            holidayCalendar.setHoliday((GregorianCalendar)date);
        }

        for (Object date : holidays) 
        {
            assert holidayCalendar.isHoliday((GregorianCalendar)date) == true;
        }
    }

    @Test
    public void setRecurringHoliday()
    {
        HolidayCalendar holidayCalendar = new HolidayCalendar();

        ArrayList<GregorianCalendar> holidays = new ArrayList<>();
        holidays.add(new GregorianCalendar(2004, Calendar.MAY, 27, 0 ,0));
        holidays.add(new GregorianCalendar(2025, Calendar.AUGUST, 29, 0 ,0));
        holidays.add(new GregorianCalendar(2025, Calendar.NOVEMBER, 17, 0 ,0));
        holidays.add(new GregorianCalendar(2137, Calendar.APRIL, 12, 0 ,0));
        holidays.add(new GregorianCalendar(1995, Calendar.JULY, 30, 0 ,0));

        for (Object date : holidays)
        {
            holidayCalendar.setRecurringHoliday((GregorianCalendar)date);
        }

        holidays.add(new GregorianCalendar(2005, Calendar.MAY, 27, 0 ,0));
        holidays.add(new GregorianCalendar(2026, Calendar.AUGUST, 29, 0 ,0));
        holidays.add(new GregorianCalendar(2026, Calendar.NOVEMBER, 17, 0 ,0));
        holidays.add(new GregorianCalendar(2138, Calendar.APRIL, 12, 0 ,0));
        holidays.add(new GregorianCalendar(1996, Calendar.JULY, 30, 0 ,0));
        
        holidays.add(new GregorianCalendar(2006, Calendar.MAY, 27, 0 ,0));
        holidays.add(new GregorianCalendar(2027, Calendar.AUGUST, 29, 0 ,0));
        holidays.add(new GregorianCalendar(2027, Calendar.NOVEMBER, 17, 0 ,0));
        holidays.add(new GregorianCalendar(2139, Calendar.APRIL, 12, 0 ,0));
        holidays.add(new GregorianCalendar(1997, Calendar.JULY, 30, 0 ,0));

        holidays.add(new GregorianCalendar(2007, Calendar.MAY, 27, 0 ,0));
        holidays.add(new GregorianCalendar(2028, Calendar.AUGUST, 29, 0 ,0));
        holidays.add(new GregorianCalendar(2028, Calendar.NOVEMBER, 17, 0 ,0));
        holidays.add(new GregorianCalendar(2140, Calendar.APRIL, 12, 0 ,0));
        holidays.add(new GregorianCalendar(1998, Calendar.JULY, 30, 0 ,0));
        for (Object date : holidays) 
        {
            assert holidayCalendar.isHoliday((GregorianCalendar)date) == true;
        }
    }
}
