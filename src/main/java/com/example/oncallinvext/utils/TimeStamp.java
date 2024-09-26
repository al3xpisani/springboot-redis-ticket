package com.example.oncallinvext.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeStamp {

    public static String getTimeStampBRNow() {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        format.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        Date now = new Date();
        return format.format(now);
    }
}
