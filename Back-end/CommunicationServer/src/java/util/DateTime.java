package util;

import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to represent dates and times and it can be used to compare two dates
 * (i.e find out if one date is after another, calculate the difference in time etc.).
 * @author Wessel Jongkind
 * 27-07-2017
 */
public class DateTime extends Date
{
    /**
     * The amount of hours and minutes that the datetime object represents.
     */
    private final int hour, minute;
    
    /**
     * The groups associated with the regex-string.
     */
    private static final int DATE_GROUP = 1, HOURS_GROUP = 2, MINUTES_GROUP = 3;
    
    /**
     * If numbers are equal or larger than this number, then they're double-digit.
     */
    private static final int DOUBLE_DIGIT = 10;
    
    /**
     * The regex-string used to obtain the datetime values.
     */
    private static final String DATETIME_PATTERN = "(\\d{4}-\\d\\d?-\\d\\d?)? ?(\\d\\d?):(\\d\\d?).*";

    /**
     * Instantiates a new DateTime object by parsing the string to usable values.
     * @param datetime The String that represents the datetime.
     * @throws Exception When the String could not be converted.
     */
    public DateTime(String datetime) throws Exception
    {
        super(getDateTimePart(datetime, DATE_GROUP));
        
        if(datetime == null)
            hour = minute = Integer.MIN_VALUE;
        else
        {
            hour = Integer.parseInt(getDateTimePart(datetime, HOURS_GROUP));
            minute = Integer.parseInt(getDateTimePart(datetime, MINUTES_GROUP));
        }
    }
    
    /**
     * Obtains a part of the regex-code used to parse Strings to datetime values.
     * @param datetime The String that should be parsed.
     * @param part The part of the regex-code that should be obtained.
     * @return The String containing the value of the requested regex-part.
     * @throws Exception When no matches have been found.
     */
    private static String getDateTimePart(String datetime, int part) throws Exception
    {
        if(datetime == null)
            return null;
        
        Pattern pattern = Pattern.compile(DATETIME_PATTERN);
        Matcher matcher = pattern.matcher(datetime);
        if(!matcher.matches())
            throw new Exception("Invalid datetime entered.");
        
        return matcher.group(part);
    }
    
    @Override
    public boolean isAfter(Date other)
    {
        if(other instanceof DateTime)
        {
            DateTime otherDateTime = (DateTime) other;
            
            return super.isAfter(other) || (getDate().equals(otherDateTime.getDate())) && 
                   (getHour() > otherDateTime.getHour() || 
                   (getHour() == otherDateTime.getHour() && getMinute() > otherDateTime.getMinute()));
        }
        else
            return super.isAfter(other);
    }
    
    /**
     * Returns the current date and time
     * @return The current datetime
     * @throws Exception When the current datetime could not be obtained.
     */
    public static DateTime getCurrentDateTime() throws Exception
    {
        ZonedDateTime datetime = ZonedDateTime.now();
        return new DateTime(Date.getCurrentDate().toString() + " " + datetime.getHour() + ":" + datetime.getMinute());
    }
    
    /**
     * Returns the date represented by the datetime object represented as a String.
     * @return The date represented by the datetime object represented as a String.
     */
    public String getDate()
    {
        return super.toString();
    }
    
    /**
     * Returns the time represented by the datetime object represented as a String.
     * @return The time represented by the datetime object represented as a String.
     */
    public String getTime()
    {
        return makeUniformString(hour) + ":" + makeUniformString(minute);
    }
    
    /**
     * Returns the amount of hours that the datetime object represents.
     * @return The amount of hours that the datetime object represents.
     */
    public int getHour()
    {
        return hour;
    }
    
    /**
     * Returns the amount of minutes that the datetime object represents.
     * @return The amount of minutes that the datetime object represents.
     */
    public int getMinute()
    {
        return minute;
    }
    
    @Override
    public String toString()
    {
        String date = super.toString();
        
        if(hour == Integer.MIN_VALUE)
            return date;
        
        return date + " " + makeUniformString(hour) + ":" + makeUniformString(minute);
    }
    
    /**
     * Makes a uniform datetime String so that all numbers lower than 10 will be displayed
     * with a 0 before them.
     * @param digit The number to be converted.
     * @return The converted String.
     */
    private String makeUniformString(int digit)
    {
        if(digit < DOUBLE_DIGIT)
            return "0" + digit;
        else
            return "" + digit;
    }
}
