package com.example.product.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

}
