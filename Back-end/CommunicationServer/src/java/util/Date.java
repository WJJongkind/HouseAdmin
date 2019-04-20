package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Objects of this class represent more usable Date objects that can be used
 * to calculate the difference in days, months and years between the object and
 * the current date.
 * @author Wessel Jongkind
 * @version 04-07-2017
 */
public class Date
{
    /**
     * The calendar-object that represents the date.
     */
    private final Calendar calendar;
    
    /**
     * The String representation of the date.
     */
    private final String date;
    
    /**
     * The average amount of days per month, used to calculate the age of patients.
     */
    private static final double AVERAGE_DAYS_PER_MONTH = 30.41666667;
    
    /**
     * The average amount of days per year, used to calculate the age of patients.
     */
    private static final double AVERAGE_DAYS_PER_YEAR = 365.25;
    
    /**
     * The amount of milliseconds that are in a day.
     */
    private static final int MILLIS_PER_DAY = 86400000;
    
    /**
     * Instantiates a new Date object by interpreting the given String date.
     * @param date The date that the Date-object should represent, formatted as yyyy-mm-dd.
     * @throws Exception When the String could not be converted.
     */
    public Date(String date) throws Exception
    {
        if(date == null)
        {
            calendar = null;
            this.date = null;
        }
        else
        {
            this.date = date;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            calendar = dateFormat.getCalendar();
            calendar.setTime(dateFormat.parse(date));
        }
    }
    
    /**
     * Obtains the current date (system time).
     * @return The current date in system-time.
     */
    public static String getCurrentDate()
    {
        DateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currentDate = currentDateFormat.getCalendar();
        currentDate.setTime(new java.util.Date());
        
        //Build the date-string.
        String date = currentDate.get(Calendar.YEAR) + "-";
        date += (currentDate.get(Calendar.MONTH) + 1) + "-";
        date += currentDate.get(Calendar.DAY_OF_MONTH);
        
        return date;
    }
    
    /**
     * Returns the number of the day represented by this object.
     * @return The number of the day represented by this object.
     */
    public int getDay()
    {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * Returns the number of the month represented by this object.
     * @return The number of the month represented by this object.
     */
    public int getMonth()
    {
        return calendar.get(Calendar.MONTH) + 1;
    }
    
    /**
     * Returns the number of the year represented by this object.
     * @return The number of the year represented by this object.
     */
    public int getYear()
    {
        return calendar.get(Calendar.YEAR);
    }
    
    /**
     * Calculates and returns the amount of days between the date-object and
     * the current system date.
     * @return The age of the patient
     */
    public double getDifferenceDays()
    {
        //Calculate the difference in days between the current date and the given date
        return (System.currentTimeMillis() - calendar.getTimeInMillis()) / MILLIS_PER_DAY;
    }
    
    /**
     * Calculates and returns the amount of years between the date-object and
     * the current system date. The amount of years may be a maximum of
     * 0.003 off from the actual amount of years due to the way the method
     * is implemented.
     * @return The difference between the date object and the current date in years.
     */
    public double getDifferenceYears()
    {
        return getDifferenceDays() / AVERAGE_DAYS_PER_YEAR;
    }
    
    /**
     * Checks whether the date object is after the given date object.
     * @param date The date object that should be compared.
     * @return True if the date is after the given date.
     */
    public boolean isAfter(Date date)
    {
        return getYear() > date.getYear() || 
               (getYear() == date.getYear() && (getMonth() > date.getMonth() ||
               (getMonth() == date.getMonth() && (getDay() > date.getDay()))));
    }
    
    @Override
    public String toString()
    {
        if(date == null)
            return "-";
        
        return date;
    }
}
