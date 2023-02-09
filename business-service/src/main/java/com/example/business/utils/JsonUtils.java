package com.example.business.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class JsonUtils {

  private static final Gson gson = new Gson();

  public JsonUtils() {
  }

  public static String toJson(Object obj) {
    return gson.toJson(obj);
  }

  public static HashMap<?, ?> fromJson(String jsonstr) {
    return (HashMap) gson.fromJson(jsonstr, HashMap.class);
  }

  public static JsonObject toJsonObject(String jsonstr) {
    return (JsonObject) gson.fromJson(jsonstr, JsonObject.class);
  }

  public static Gson gson() {
    return gson;
  }
}
