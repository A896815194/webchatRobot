package com.web.webchat.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {

    public static Date toadyAfterMillions(long afterMillions) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime todayafter = today.plus(afterMillions, ChronoUnit.MILLIS);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = todayafter.atZone(zoneId);
        Date date = Date.from(zonedDateTime.toInstant());
        return date;
    }

}
