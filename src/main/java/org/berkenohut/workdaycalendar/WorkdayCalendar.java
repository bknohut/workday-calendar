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
        private final int startTime;
        private final int stopTime;
 
        private final int totalWorkingTime;

        public WorkingHours(Calendar start, Calendar stop)
        {
            int startHour = start.get(Calendar.HOUR_OF_DAY);
            int startMinute = start.get(Calendar.MINUTE);
            startTime = startHour * 60 + startMinute;

            int stopHour = stop.get(Calendar.HOUR_OF_DAY);
            int stopMinute = stop.get(Calendar.MINUTE);
            stopTime = stopHour * 60 + stopMinute;

            totalWorkingTime = stopTime - startTime;
        }

        public int getStartTime() 
        {
            return startTime;
        }

        public int getStopTime() 
        {
            return stopTime;
        }

        public int getWorkingDayTotal()
        {
            return totalWorkingTime;
        }
        
        public int getHoursFromWorkingTime(int time)
        {
            int hours = (int)time / 60;
            return hours;
        }

        public int getMinutesFromWorkingTime(int time)
        {
            int minutes = time % 60;
            return minutes;
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
        workingHours = new WorkingHours(start, stop);
    }

    public Date getWorkdayIncrement(Date startDate, float incrementInWorkdays)
    {
        float incrementHours = incrementInWorkdays % 1;
        int incrementDays = (int) (incrementInWorkdays - incrementHours);
        int increment = incrementDays > 0 ? 1 : -1;

        int scaledIncrementTime = (int)(incrementHours * workingHours.getWorkingDayTotal()); // scaled working hours
        // TODO: increment Hours needs to be int maybe take the nearest int instead of casting

        int workingHoursStartTime = workingHours.getStartTime();
        int workingHoursStopTime = workingHours.getStopTime();

        // set start date 
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(startDate);
        //System.out.println("StartingDate: " + currentDate.getTime());

        int startTime = currentDate.get(Calendar.HOUR_OF_DAY) * 60 + currentDate.get(Calendar.MINUTE);

        if( increment == 1 ) // moving fw
        {
            if( startTime < workingHoursStartTime)
                startTime = workingHoursStartTime;

            int timeInNextDay = startTime + scaledIncrementTime;
            if( startTime + scaledIncrementTime > workingHoursStopTime) // move to next day's beginning - this can be an else totalIncrementTime <= workingDay
            {
                timeInNextDay = workingHoursStartTime + scaledIncrementTime;
                currentDate.add(Calendar.DAY_OF_YEAR, 1);
            }
            currentDate.set(Calendar.HOUR_OF_DAY, workingHours.getHoursFromWorkingTime(timeInNextDay));
            currentDate.set(Calendar.MINUTE, workingHours.getMinutesFromWorkingTime(timeInNextDay));

        }
        else // moving bw
        {
            if( startTime > workingHoursStopTime) // move to the day's end.
                startTime = workingHoursStopTime;

            int timeInNextDay = startTime + scaledIncrementTime;
            if( timeInNextDay < workingHoursStartTime) // total increment is negative here
            {
                timeInNextDay = workingHoursStopTime - scaledIncrementTime;
                currentDate.add(Calendar.DAY_OF_YEAR, -1);
            }
            currentDate.set(Calendar.HOUR_OF_DAY, workingHours.getHoursFromWorkingTime(timeInNextDay));
            currentDate.set(Calendar.MINUTE, workingHours.getMinutesFromWorkingTime(timeInNextDay));
        }

        int i = 0;
        incrementDays = Math.abs(incrementDays);
        while(i < incrementDays)
        {   
            currentDate.add(Calendar.DAY_OF_YEAR, increment);
            boolean isHoliday = holidayCalendar.isHoliday((Calendar)currentDate.clone()); // clone to ensure it does not change, otherwise might yield unwanted results or inf. loop

            if(isHoliday)
                continue;
            i++;
        }

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
        //Date start = new GregorianCalendar(2004, Calendar.MAY, 24, 18, 5).getTime();

        ArrayList starts = new ArrayList<Date>();
        starts.add(new GregorianCalendar(2004, Calendar.MAY, 24, 18, 5).getTime());
        starts.add(new GregorianCalendar(2004, Calendar.MAY, 24, 19, 3).getTime());
        starts.add(new GregorianCalendar(2004, Calendar.MAY, 24, 18, 3).getTime());
        starts.add(new GregorianCalendar(2004, Calendar.MAY, 24, 8, 3).getTime());
        starts.add(new GregorianCalendar(2004, Calendar.MAY, 24, 7, 3).getTime());

        ArrayList increments = new ArrayList<>();

        increments.add(-5.5f);
        increments.add(44.723656f);
        increments.add(-6.7470217f);
        increments.add(12.782709f);
        increments.add(8.276628f);

        for(int i = 0; i < increments.size(); ++i)
        {
            Date s = (Date)starts.get(i);
            System.out.println(f.format(starts.get(i)) + " with the addition of " +
            increments.get(i) + " working days is " +
            f.format(workdayCalendar.getWorkdayIncrement(s, (float)increments.get(i) )));
        }
    }
}
