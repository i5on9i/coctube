package utils;

import android.content.Intent;

public class IntentUtils {


    private static String TAG = "IntentUtils";

    public static Intent getIntentComposeEmail(String recipient, String popupIndication) {

//        String recipient = getString(R.string.report_email_address);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});

        //let the user choose what email client to use
        return Intent.createChooser(emailIntent, popupIndication);

    }


}
