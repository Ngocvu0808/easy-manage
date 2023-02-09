package com.example.business.utils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
public class SortingUtils {

  public SortingUtils() {
  }

  public static boolean validateSort(String sort, Class<?> aclass) {
    Field[] fields = aclass.getDeclaredFields();
    Map<String, String> fieldSort = detectSortType(sort);
    if (fieldSort.isEmpty()) {
      return true;
    } else {
      Set<String> keys = fieldSort.keySet();
      Iterator var5 = keys.iterator();

      String key;
      do {
        if (!var5.hasNext()) {
          return true;
        }

        key = (String) var5.next();
        String finalKey = key;
        boolean isExists = Arrays.stream(fields).anyMatch((f) -> {
          return f.getName().equals(finalKey);
        });
        if (!isExists) {
          return false;
        }
      } while (((String) fieldSort.get(key)).equals("asc") || ((String) fieldSort.get(key)).equals(
          "desc"));

      return false;
    }
  }

  public static Map<String, String> detectSortType(String sort) {
    LinkedHashMap<String, String> map = new LinkedHashMap();
    if (sort != null && !sort.isEmpty()) {
      String[] arr = sort.trim().split(",");
      String[] var3 = arr;
      int var4 = arr.length;

      for (int var5 = 0; var5 < var4; ++var5) {
        String s = var3[var5];
        String[] order = s.trim().split("_");
        map.put(order[0].trim(), order[1].trim());
      }
    }

    return map;
  }
}
