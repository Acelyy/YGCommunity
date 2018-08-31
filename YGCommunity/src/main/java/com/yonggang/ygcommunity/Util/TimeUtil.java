package com.yonggang.ygcommunity.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String FormatDate(String time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(time)));
    }
}
