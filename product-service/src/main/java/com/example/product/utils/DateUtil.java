package com.example.product.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.util.StringUtils;

public class DateUtil {

  public static Long getOnlyDateFromTimeStamp(Long timeStamp) throws ParseException {
//    Long creationTimeStamp = Long.valueOf(timeStamp);
    Date dateCreated = new Date(timeStamp);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date dateFormat = new SimpleDateFormat("yyyy-MM-dd")
        .parse(simpleDateFormat.format(dateCreated));
    return dateFormat.getTime();
  }
  public static long getDateFromStringWithPattern(String date, String pattern) throws ParseException {
    if (StringUtils.isEmpty(date))
      return 0;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    return simpleDateFormat.parse(date).getTime();
  }

  public static String getStringFromDateLongWithPattern(long date, String pattern) throws ParseException {
    if (date == 0)
      return "";
    Date _date = new Date(date);
    DateFormat formatter = new SimpleDateFormat(pattern);
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    return formatter.format(date);
  }

  public static String convertPattern(String inDate, String pattern1, String pattern2)
      throws ParseException {
    DateFormat formatter1 = new SimpleDateFormat(pattern1);
    DateFormat formatter2 = new SimpleDateFormat(pattern2);
    Date dateFound = formatter1.parse(inDate);
    String result = formatter2.format(dateFound);
    return result;
  }
  public static Date addDays(Date date, int days)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, days); //minus number would decrement the days
    return cal.getTime();
  }

}
