package explorer.logic.utils;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    /**
     * Read date from a string, following the pattern: YYYY-MM-DD
     * @param str String to be parsed
     * @return Date object
     * @throws ParseException
     */
    public static Date parseDate(String str) throws ParseException {

        // Set expected date format. By default sets time to 00:00:00
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.parse(str);
    }

    /**
     * Converts Date object to Unix/Epoch Time in seconds
     * @param date
     * @return Unix Time as a BigInteger
     */
    public static BigInteger getUnixTimeFromDate(Date date) {
        return BigInteger.valueOf( date.getTime() / 1000 );     // .getTime returns in miliseconds
    }
}
