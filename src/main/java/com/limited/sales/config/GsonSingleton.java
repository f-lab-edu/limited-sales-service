package com.limited.sales.config;

import com.google.gson.Gson;

public class GsonSingleton {
  public static Gson getGson() {
    return GsonLazyHolder.gson;
  }

  private static final class GsonLazyHolder {
    private static final Gson gson = new Gson();
  }
}
