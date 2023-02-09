package com.example.business.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

  public static Long getOnlyDateFromTimeStamp(Long timeStamp) throws ParseException {
//    Long creationTimeStamp = Long.valueOf(timeStamp);
    Date dateCreated = new Date(timeStamp);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date dateFormat = new SimpleDateFormat("yyyy-MM-dd")
        .parse(simpleDateFormat.format(dateCreated));
    return dateFormat.getTime();
  }

}
