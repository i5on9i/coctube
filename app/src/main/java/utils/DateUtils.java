package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by namh on 2015-02-17.
 */
public class DateUtils {

    private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String ISO_DATE_RFC_822_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // "2001-07-04T12:08:56.235-0700"



    public static String getRelativeTimeSpanString(String isoTimeString) throws ParseException {
        // "2001-07-04T12:08:56.000Z" --> Date
        DateFormat df = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
        return (String) android.text.format.DateUtils.getRelativeTimeSpanString(
                df.parse(isoTimeString).getTime(),
                System.currentTimeMillis(),
                android.text.format.DateUtils.DAY_IN_MILLIS);
    }
}
