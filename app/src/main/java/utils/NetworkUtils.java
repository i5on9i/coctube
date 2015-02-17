package utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {


    /**
     *
     * This methods, so far, supports only GET, please do not use "PUT" on requestMethod.
     *
     * @param requestMethod  should be a "GET" or "PUT".
     * @param feedUrlString
     * @return
     * @throws java.io.IOException
     */

    public static InputStream getInputStream(String requestMethod, String feedUrlString) throws IOException {
        URL feedUrl = new URL(feedUrlString);
        HttpURLConnection conn = (HttpURLConnection) feedUrl.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod(requestMethod);
        conn.setDoInput(true);

        return conn.getInputStream();

    }



}
