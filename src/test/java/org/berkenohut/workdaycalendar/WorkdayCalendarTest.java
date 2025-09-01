package org.berkenohut.workdaycalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.junit.jupiter.api.Test;

public class WorkdayCalendarTest
{
    private class DateIncrementResultQuadruple
    {
        DateIncrementResultQuadruple(int idx, Date date, float increment, Date result)
        {
            this.idx = idx;
            this.date = date;
            this.increment = increment;
            this.result = result;
        }

        public int idx;
        public Date date;
        public float increment;
        public Date result;
    }

    @Test
    public void testSetWorkdayStartAndStop()
    {
        Random random = new Random();
        int min = 0;
        int max = 23;

        int startHour = random.nextInt(max - min) + min;
        min = startHour;
        int stopHour = random.nextInt(max - min) + min;
        
        min = 0;
        max = 59;

        int startMinute = random.nextInt(max - min) + min;
        min = startMinute;
        int stopMinute = random.nextInt(max - min) + min;

        WorkdayCalendar workdayCalendar = new WorkdayCalendar();
        workdayCalendar.setWorkdayStartAndStop(
            new GregorianCalendar(2004, Calendar.JANUARY, 1, startHour, startMinute),
            new GregorianCalendar(2004, Calendar.JANUARY, 1, stopHour, stopMinute));
        
        int storedTime = workdayCalendar.getWorkingHours().getWorkingDayTotal();

        assert storedTime == (stopHour - startHour) * 60 + (stopMinute - startMinute);

        assert (stopHour - startHour) == workdayCalendar.getWorkingHours().getHoursFromWorkingTime(storedTime);
        assert (stopMinute - startMinute) == workdayCalendar.getWorkingHours().getMinutesFromWorkingTime(storedTime);
    }

    @Test
    public void testGetWorkdayIncrement()
    {
        ArrayList<DateIncrementResultQuadruple> testData = new ArrayList<>();

        // negative increment
        // start time after workday 
        {
            testData.add( new DateIncrementResultQuadruple(
                0,
                new GregorianCalendar(2004, Calendar.MAY, 24, 18, 5).getTime(), 
                -5.5f,
                new GregorianCalendar(2004, Calendar.MAY, 14, 12, 0).getTime())
            );

            testData.add(new DateIncrementResultQuadruple(
                1,
                new GregorianCalendar(2004, Calendar.MAY, 24, 18, 3).getTime(), 
                -6.7470217f,
                new GregorianCalendar(2004, Calendar.MAY, 13, 10, 2).getTime())
            );
        }
        
        // start time before workday
        {
            testData.add(new DateIncrementResultQuadruple(
                2,
                new GregorianCalendar(2025, Calendar.AUGUST, 22, 3, 33).getTime(), 
                -8.312f,
                new GregorianCalendar(2025, Calendar.AUGUST, 8, 13, 31).getTime())
            );
        }
        
        // start time in workday
        // increment does not move time out of workday
        {
            testData.add(new DateIncrementResultQuadruple(
                3,
                new GregorianCalendar(2025, Calendar.AUGUST, 22, 15, 21).getTime(), 
                -0.25f,
                new GregorianCalendar(2025, Calendar.AUGUST, 22, 13, 21).getTime())
            );
        }
        
        // increment moves time before workday
        {
            testData.add(new DateIncrementResultQuadruple(
                4,
                new GregorianCalendar(2025, Calendar.AUGUST, 22, 13, 0).getTime(), 
                -0.75f,
                new GregorianCalendar(2025, Calendar.AUGUST, 21, 15, 0).getTime())
            );
        }
        
        // no day increment, time changes the day to a holiday
        {
            testData.add(new DateIncrementResultQuadruple(
                5,
                new GregorianCalendar(2025, Calendar.AUGUST, 15, 13, 0).getTime(), 
                -0.75f,
                new GregorianCalendar(2025, Calendar.AUGUST, 13, 15, 0).getTime())
            );
        }
        
        // holiday start increment hits holiday
        {
            testData.add(new DateIncrementResultQuadruple(
                6,
                new GregorianCalendar(2025, Calendar.AUGUST, 31, 15, 0).getTime(), 
                -2.0f,
                new GregorianCalendar(2025, Calendar.AUGUST, 26, 16, 0).getTime())
            );
        }

        // positive increment
        // start time after workday
        {
            testData.add(new DateIncrementResultQuadruple(
                7,
                new GregorianCalendar(2004, Calendar.MAY, 24, 19, 3).getTime(), 
                44.723656f,
                new GregorianCalendar(2004, Calendar.JULY, 27, 13, 47).getTime())
            );
        }

        // start time before workday
        {
            testData.add(new DateIncrementResultQuadruple(
                8,
                new GregorianCalendar(2004, Calendar.MAY, 24, 7, 3).getTime(), 
                8.276628f,
                new GregorianCalendar(2004, Calendar.JUNE, 4, 10, 12).getTime())
            );
        }
        
        // start time in workday
        // increment does not move time out of workday
        {
            testData.add(new DateIncrementResultQuadruple(
                9,
                new GregorianCalendar(2004, Calendar.MAY, 24, 8, 3).getTime(), 
                12.782709f,
                new GregorianCalendar(2004, Calendar.JUNE, 10, 14, 18).getTime())
            );
        }

        // increment moves time after workday
        {
            testData.add(new DateIncrementResultQuadruple(
                10,
                new GregorianCalendar(2025, Calendar.AUGUST, 22, 14, 42).getTime(), 
                5.625f,
                new GregorianCalendar(2025, Calendar.SEPTEMBER, 2, 11, 42).getTime())
            );
        }

        // holiday start increment hits holiday
        {
            testData.add(new DateIncrementResultQuadruple(
                11,
                new GregorianCalendar(2025, Calendar.AUGUST, 23, 9, 0).getTime(), 
                4.0f,
                new GregorianCalendar(2025, Calendar.SEPTEMBER, 1, 8, 0).getTime())
            );
        }

        // no increment with correct start date
        {
            testData.add(new DateIncrementResultQuadruple(
                12,
                new GregorianCalendar(2025, Calendar.AUGUST, 22, 10, 0).getTime(), 
                0.0f,
                new GregorianCalendar(2025, Calendar.AUGUST, 22, 10, 0).getTime())
            );
        }

        // no increment with wrong start date
        {
            testData.add(new DateIncrementResultQuadruple(
                13,
                new GregorianCalendar(2025, Calendar.AUGUST, 23, 18, 0).getTime(), 
                0.0f,
                new GregorianCalendar(2025, Calendar.AUGUST, 22, 16, 0).getTime())
            );
        }

        // large increment 
        {
            testData.add(new DateIncrementResultQuadruple(
                14,
                new GregorianCalendar(2025, Calendar.SEPTEMBER, 1, 8, 0).getTime(), 
                100000.0f,
                new GregorianCalendar(2411, Calendar.JANUARY, 28, 8, 0).getTime())
            );
        }

        WorkdayCalendar workdayCalender = new WorkdayCalendar();
        workdayCalender.setWorkdayStartAndStop(
            new GregorianCalendar(2004, Calendar.JANUARY, 1, 8, 0),
            new GregorianCalendar(2004, Calendar.JANUARY, 1, 16, 0));
        
        workdayCalender.setRecurringHoliday(new GregorianCalendar(2004, Calendar.MAY, 17, 0, 0));
        workdayCalender.setHoliday(new GregorianCalendar(2004, Calendar.MAY, 27, 0 ,0));

        workdayCalender.setHoliday(new GregorianCalendar(2025, Calendar.AUGUST, 14, 0 ,0));
        workdayCalender.setRecurringHoliday(new GregorianCalendar(2025, Calendar.AUGUST, 28, 0, 0));


        for (int i = 0; i < testData.size(); ++i) 
        {
            DateIncrementResultQuadruple testDataItem = testData.get(i);

            int idx = testDataItem.idx;
            Date date = testDataItem.date;
            float increment = testDataItem.increment;
            Date expectedResult = testDataItem.result;

            Date receivedResult = workdayCalender.getWorkdayIncrement(date, increment);

            boolean isCorrect = receivedResult.equals(expectedResult);

            System.out.println("isCorrect: " + isCorrect + " index: " + idx);
            System.out.println("receivedResult: " + receivedResult + " expectedResult: " + expectedResult + " and increment is: " + increment);

            assert isCorrect;
        }

    }
}