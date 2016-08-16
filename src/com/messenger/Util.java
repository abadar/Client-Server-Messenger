package com.messenger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author arsalan
 */
public class Util {

    public static final String NOT_CONNECTED = "\tNot Connected";

    public static final String CONNECTED = "\tConnected";

    public static final String WRONG_PORT = "Wrong Port Number!!!";

    public static final String INVALID_PORT_LENGTH = "Invalid Port Length!!!";

    /**
     * Function to get the current time
     *
     * @return
     */
    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }

}
