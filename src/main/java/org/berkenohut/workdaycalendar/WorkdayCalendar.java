package org.berkenohut.workdaycalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author berke
 */
public class WorkdayCalendar {

    private class WorkingHours
    {
        private final int startHour;
        private final int startMinute;

        private final int stopHour;
        private final int stopMinute;
 
        private final int totalHours;
        private final int totalMinutes;

        public WorkingHours(int startHour, int startMinute, int stopHour, int stopMinute)
        {
            this.startHour = startHour;
            this.startMinute = startMinute;

            this.stopHour = stopHour;
            this.stopMinute = stopMinute;

            int minuteDifference = stopMinute - startMinute;

            if( minuteDifference < 0)
            {
                this.totalHours = stopHour - startHour - 1;
                this.totalMinutes = minuteDifference + 60;
            }
            else
            {
                this.totalHours = stopHour - startHour;
                this.totalMinutes = minuteDifference;
            }

            /*
            System.out.println("Received working hours: Start: " + startHour + ":" + startMinute + " and Stop: " + stopHour + ":" + stopMinute + " with a time difference of: "
            + totalHours + ":" + totalMinutes);
            */
        }

        public int getStartHour() 
        {
            return startHour;
        }

        public int getStartMinute()
        {
            return startMinute;
        }

        public int getStopHour() 
        {
            return stopHour;
        }

        public int getStopMinute()
        {
            return stopMinute;
        }

        public int getWorkingDayTotalHours()
        {
            return totalHours;
        }

        public int getWorkingDayTotalMinutes()
        {
            return totalMinutes;
        }
        
    }

    private WorkingHours workingHours;
    private final HolidayCalendar holidayCalendar;
    
    public WorkdayCalendar()
    {
        holidayCalendar = new HolidayCalendar();
    }

    public void setHoliday(final Calendar date)
    {
        holidayCalendar.setHoliday(date);
    }
    
    public void setRecurringHoliday(final Calendar date)
    {
        holidayCalendar.setRecurringHoliday(date);
    }

    public void setWorkdayStartAndStop(Calendar start, Calendar stop)
    {
        int startHour = start.get(Calendar.HOUR_OF_DAY);
        int startMinute = start.get(Calendar.MINUTE);

        int endHour = stop.get(Calendar.HOUR_OF_DAY);
        int endMinute = stop.get(Calendar.MINUTE);

        if(workingHours == null)
            workingHours = new WorkingHours(startHour, startMinute, endHour, endMinute);
    }

    public Date getWorkdayIncrement(Date startDate, float incrementInWorkdays)
    {
        float incrementHours = incrementInWorkdays % 1;
        int incrementDays = (int) (incrementInWorkdays - incrementHours);

        incrementHours = incrementHours * workingHours.getWorkingDayTotalHours(); // working hours in float

        int totalHours = (int) incrementHours; // just the hours
        float remainder = incrementHours - totalHours;
        int totalMinutes = (int) (remainder * 60); // just the minutes

        int i = 0;
        int increment = incrementDays > 0 ? 1 : -1;

        incrementDays = Math.abs(incrementDays);

        Calendar currentDate = Calendar.getInstance();

        currentDate.setTime(startDate);
        System.out.println("StartingDate: " + currentDate.getTime());

        while(i < incrementDays)
        {   
            currentDate.add(Calendar.DAY_OF_YEAR, increment);
            boolean isHoliday = holidayCalendar.isHoliday((Calendar)currentDate.clone()); // clone to ensure it does not change, otherwise might yield unwanted results or inf. loop

            if(isHoliday)
                continue;
            i++;
        }

        int hourOfTheDay = currentDate.get(Calendar.HOUR_OF_DAY);
        int minuteOfTheHour = currentDate.get(Calendar.MINUTE);

        

        return currentDate.getTime();
    }
    
    public static void main(String[] args) 
    {
        WorkdayCalendar workdayCalendar = new WorkdayCalendar();
        workdayCalendar.setWorkdayStartAndStop(
            new GregorianCalendar(2004, Calendar.JANUARY, 1, 8, 0),
            new GregorianCalendar(2004, Calendar.JANUARY, 1, 16, 0));
        
        workdayCalendar.setRecurringHoliday(new GregorianCalendar(2004, Calendar.MAY, 17, 0, 0));
        workdayCalendar.setHoliday(new GregorianCalendar(2004, Calendar.MAY, 27, 0 ,0));

        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date start = new GregorianCalendar(2004, Calendar.MAY, 24, 18, 5).getTime();

        ArrayList increments = new ArrayList<>();

        increments.add(-5.5f);
        increments.add(44.723656f);
        increments.add(-6.7470217f);
        increments.add(12.782709f);
        increments.add(8.276628f);

        for(int i = 0; i < increments.size(); ++i)
        {
            System.out.println(f.format(start) + " with the addition of " +
            increments.get(i) + " working days is " +
            f.format(workdayCalendar.getWorkdayIncrement(start, (float)increments.get(i) )));

            System.err.println("");
        }

        
    }
}
