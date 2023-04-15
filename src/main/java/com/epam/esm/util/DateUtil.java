package com.epam.esm.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtil {

    public static String toIso8601Format(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String formatted = dateFormat.format(timestamp);
        return formatted;
    }
}
