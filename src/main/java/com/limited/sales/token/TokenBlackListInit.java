package com.limited.sales.token;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenBlackListInit {
    public static ConcurrentMap<String, String> getInstance() { return LazyHolder.TOKEN_BLACK_LIST; }

    private static final class LazyHolder {
        private static final ConcurrentMap<String, String> TOKEN_BLACK_LIST = new ConcurrentHashMap<>();
    }
}
