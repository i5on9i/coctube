package utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 10. 21
 * Time: 오후 3:26
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {

    public static String convertStreamToString(InputStream stream, String encoding) throws IOException {
        // convert to the String
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, encoding));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        return responseStrBuilder.toString();
    }

    public static InputStream convertToInputStream(String input){

        return new ByteArrayInputStream(input.getBytes());

    }

    public static Date convertToDate(SimpleDateFormat format, String pubDateString) throws ParseException {

        Date pubDate;

        pubDate = format.parse(pubDateString.trim());
        return pubDate;
    }


    public static Date convertToDateKST(SimpleDateFormat format, String pubDateString) throws ParseException {

        Calendar KST = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
        format.setCalendar(KST);

        Date pubDate;

        pubDate = format.parse(pubDateString.trim());
        return pubDate;
    }

    public static String getYouTubeIdFromUrl(String url){

        return url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('?'));

    }

    public static String getNumberWithComma(String viewCount){

        try{
            int count = Integer.parseInt(viewCount);
            return NumberFormat.getInstance().format(count);
        }catch(NumberFormatException e){
            Log.i("StringUtils", "getNumberWithComma, number format exception");
        }

        return viewCount;

    }

    public static String convertSecToDurationString(String duration) {

        try {
            int sec = Integer.parseInt(duration);

            long rHour = TimeUnit.SECONDS.toHours(sec);
            long rMin = TimeUnit.SECONDS.toMinutes(sec) - TimeUnit.HOURS.toMinutes(rHour);
            long rSec = sec - TimeUnit.MINUTES.toSeconds(rMin) - TimeUnit.HOURS.toSeconds(rHour);


            String result;
            if(rHour < 1){
                result = String.format("%d:%02d", rMin, rSec);
            }else{
                result = String.format("%d:%02d:%02d", rHour, rMin, rSec);
            }

            return result;


        } catch (NumberFormatException e) {
            Log.i("StringUtils", "convertSecToDurationString, number format exception");
        }
        return "";

    }




}
