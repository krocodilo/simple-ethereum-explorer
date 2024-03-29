package explorer.logic.utils;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {

    /**
     * Read date from a string, following the pattern: YYYY-MM-DD
     * @param str String to be parsed
     * @return Date object
     * @throws ParseException - if unable to parse date from string
     */
    public static Date parseDate(String str) throws ParseException {

        // Set expected date format. By default sets time to 00:00:00
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.parse(str);
    }

    /**
     * Converts Date object to Unix/Epoch Time in seconds
     * @param date Date object to convert
     * @return Unix Time as a BigInteger
     */
    public static BigInteger getUnixTimeFromDate(Date date) {
        return BigInteger.valueOf( date.getTime() / 1000 );     // .getTime returns in miliseconds
    }

    /**
     * Converts Unix Time to date and time formatted as: "yyyy-MM-dd HH:mm:ss"
     * @param timestamp Long number
     * @return Formatted date
     */
    public static String getDateFromUnixTime(long timestamp) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format( new Date(timestamp * 1000) );    // to get in miliseconds
    }

    /**
     * Convert Wei units to ETH units
     * @param wei Wei amount
     * @return BigDecimal value of ETH
     */
    public static BigDecimal fromWeiToETH(String wei) {
        return Convert.fromWei(wei, Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_EVEN);
    }
}
