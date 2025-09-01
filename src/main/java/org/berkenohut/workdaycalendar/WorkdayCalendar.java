package org.berkenohut.workdaycalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.berkenohut.holidaycalendar.HolidayCalendar;

/**
 *
 * @author berke
 */
public class WorkdayCalendar {

    protected class WorkingHours {

        private static final int MINUTESINHOUR = 60;

        private final int startTime;
        private final int stopTime;
        private final int totalWorkingTime;

        public WorkingHours() {
            startTime = 8 * MINUTESINHOUR;
            stopTime = 16 * MINUTESINHOUR;
            totalWorkingTime = stopTime - startTime;
        }

        public WorkingHours(Calendar start, Calendar stop) {
            startTime = getTimeFromDate(start);
            stopTime = getTimeFromDate(stop);

            totalWorkingTime = stopTime - startTime;
        }

        public int getTimeFromDate(Calendar date) {
            int hours = date.get(Calendar.HOUR_OF_DAY);
            int minutes = date.get(Calendar.MINUTE);

            return hours * MINUTESINHOUR + minutes;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getStopTime() {
            return stopTime;
        }

        public int getWorkingDayTotal() {
            return totalWorkingTime;
        }
        
        public int getHoursFromWorkingTime(int time) {
            int hours = (int)time / 60;
            return hours;
        }

        public int getMinutesFromWorkingTime(int time) {
            int minutes = time % 60;
            return minutes;
        }
    }

    private final HolidayCalendar holidayCalendar;
    private WorkingHours workingHours;
    
    public WorkdayCalendar() {
        holidayCalendar = new HolidayCalendar();
        workingHours = new WorkingHours();
    }

    public WorkingHours getWorkingHours() {
        return workingHours;
    }

    private void setTime(Calendar date, int time) {

        int hours = workingHours.getHoursFromWorkingTime(time);
        int minutes = workingHours.getMinutesFromWorkingTime(time);

        date.set(Calendar.HOUR_OF_DAY, hours);
        date.set(Calendar.MINUTE, minutes);
    }

    private void adjustTimeToWorkingHours(Calendar date, int incrementDirection) {
        int time = workingHours.getTimeFromDate(date);
        
        if(incrementDirection >= 0) {
            if( time < workingHours.getStartTime() ) {
                setTime(date, workingHours.getStartTime());
            } else if( time > workingHours.getStopTime() ) {
                date.add(Calendar.DAY_OF_YEAR, 1);
                setTime(date, workingHours.getStartTime());
            }
        } else {
            if( time > workingHours.getStopTime() ) {
                setTime(date, workingHours.getStopTime());
            } else if( time < workingHours.getStartTime() ) {
                date.add(Calendar.DAY_OF_YEAR, -1);
                setTime(date, workingHours.getStopTime());
            }
        }

        while(holidayCalendar.isHoliday((Calendar)date.clone())) {
            date.add(Calendar.DAY_OF_YEAR, incrementDirection);
            if (incrementDirection > 0) {
                setTime(date, workingHours.getStartTime());
            } else {
                setTime(date, workingHours.getStopTime());
            }
        }
    }

    public void setHoliday(final Calendar date) {
        holidayCalendar.setHoliday(date);
    }
    
    public void setRecurringHoliday(final Calendar date) {
        holidayCalendar.setRecurringHoliday(date);
    }

    public void setWorkdayStartAndStop(Calendar start, Calendar stop) {
        workingHours = new WorkingHours(start, stop);
    }

    public Date getWorkdayIncrement(Date startDate, float incrementInWorkdays) {
        
        float incrementHours = incrementInWorkdays % 1;
        int incrementDays = (int) (incrementInWorkdays - incrementHours);
        incrementDays = Math.abs(incrementDays);

        int incrementDirection = incrementInWorkdays > 0 ? 1 : -1;

        int scaledIncrementTime = (int)(incrementHours * workingHours.getWorkingDayTotal()); // scaled working hours

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(startDate);

        adjustTimeToWorkingHours(currentDate, incrementDirection);

        int startTime = workingHours.getTimeFromDate(currentDate);
        int adjustedTime = startTime + scaledIncrementTime;
        
        // worked some minutes, but not all
        if( adjustedTime > workingHours.getStopTime() ) { 
            adjustedTime = workingHours.getStartTime() + (adjustedTime - workingHours.getStopTime());
            incrementDays += 1;
        } else if( adjustedTime < workingHours.getStartTime() ) {
            adjustedTime = workingHours.getStopTime() - (workingHours.getStartTime() - adjustedTime);
            incrementDays += 1;
        }

        setTime(currentDate, adjustedTime);

        int i = 0;
        while(i < incrementDays) {   
            currentDate.add(Calendar.DAY_OF_YEAR, incrementDirection);
            boolean isHoliday = holidayCalendar.isHoliday((Calendar)currentDate.clone()); // clone to ensure currentDate does not change, otherwise might yield unwanted results or inf. loop

            if(isHoliday)
                continue;
            i++;
        }
        return currentDate.getTime();
    }
    
    public static void main(String[] args) {
        WorkdayCalendar workdayCalendar = new WorkdayCalendar();
        workdayCalendar.setWorkdayStartAndStop(
            new GregorianCalendar(2004, Calendar.JANUARY, 1, 8, 0),
            new GregorianCalendar(2004, Calendar.JANUARY, 1, 16, 0));
        
        workdayCalendar.setRecurringHoliday(new GregorianCalendar(2004, Calendar.MAY, 17, 0, 0));
        workdayCalendar.setHoliday(new GregorianCalendar(2004, Calendar.MAY, 27, 0 ,0));

        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date start = new GregorianCalendar(2004, Calendar.MAY, 24, 18, 5).getTime();
        float increment = -5.5f;

        System.out.println(f.format(start) + " with the addition of " +
            increment + " working days is " +
            f.format(workdayCalendar.getWorkdayIncrement(start, increment) ));
    }
}
