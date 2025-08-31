package org.berkenohut.workdaycalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author berke
 */
public class WorkdayCalendar {

    protected class WorkingHours
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

    public WorkingHours getWorkingHours()
    {
        return workingHours;
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

        int workingHoursStartTime = workingHours.getStartTime();
        int workingHoursStopTime = workingHours.getStopTime();

        // set start date 
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(startDate);
        int startTime = currentDate.get(Calendar.HOUR_OF_DAY) * 60 + currentDate.get(Calendar.MINUTE);
        incrementDays = Math.abs(incrementDays);

        if( increment == 1 ) // moving fw
        {
            if( startTime < workingHoursStartTime)
                startTime = workingHoursStartTime;

            int timeInNextDay = startTime + scaledIncrementTime;
            if( startTime + scaledIncrementTime > workingHoursStopTime)
            {
                if(startTime > workingHoursStopTime) // startTime is already ahead, move time to next day's beginning
                    timeInNextDay = workingHoursStartTime + scaledIncrementTime;

                else // subtract hours in the current day
                    timeInNextDay = workingHoursStartTime + scaledIncrementTime - (workingHoursStopTime - startTime);
                
                //currentDate.add(Calendar.DAY_OF_YEAR, 1);
                incrementDays++;
            }
            currentDate.set(Calendar.HOUR_OF_DAY, workingHours.getHoursFromWorkingTime(timeInNextDay));
            currentDate.set(Calendar.MINUTE, workingHours.getMinutesFromWorkingTime(timeInNextDay));

        }
        else // moving bw
        {
            if( startTime > workingHoursStopTime) // move to the day's end.
                startTime = workingHoursStopTime;

            int timeInNextDay = startTime + scaledIncrementTime;
            if( timeInNextDay < workingHoursStartTime) // scaledIncrementTime is negative
            {
                if(startTime < workingHoursStartTime) // startTime is already behind, move time to previous day's end
                    timeInNextDay = workingHoursStopTime + scaledIncrementTime;
                else // subtract hours in the current day 
                    timeInNextDay = (startTime - workingHoursStartTime) + workingHoursStopTime + scaledIncrementTime;

                //currentDate.add(Calendar.DAY_OF_YEAR, -1);
                incrementDays++;
            }
            currentDate.set(Calendar.HOUR_OF_DAY, workingHours.getHoursFromWorkingTime(timeInNextDay));
            currentDate.set(Calendar.MINUTE, workingHours.getMinutesFromWorkingTime(timeInNextDay));
        }

        int i = 0;
        while(i < incrementDays)
        {   
            currentDate.add(Calendar.DAY_OF_YEAR, increment);
            boolean isHoliday = holidayCalendar.isHoliday((Calendar)currentDate.clone()); // clone to ensure currentDate does not change, otherwise might yield unwanted results or inf. loop

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

        //for(int i = 0; i < increments.size(); ++i)
        //{
        //    Date s = (Date)starts.get(i);
        //    System.out.println(f.format(starts.get(i)) + " with the addition of " +
        //    increments.get(i) + " working days is " +
        //    f.format(workdayCalendar.getWorkdayIncrement(s, (float)increments.get(i) )));
        //}
    }
}
