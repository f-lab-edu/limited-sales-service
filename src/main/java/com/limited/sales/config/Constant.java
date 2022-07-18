package com.limited.sales.config;

import com.google.gson.Gson;

public final class Constant {
    public static final String ADMIN_CODE = "limitedsale";
    public static Gson getGson() {
        return GsonLazyHolder.gson;
    }

    private static final class GsonLazyHolder {
        private static final Gson gson = new Gson();
    }
}
