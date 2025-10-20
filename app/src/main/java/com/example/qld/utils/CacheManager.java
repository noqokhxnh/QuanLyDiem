package com.example.qld.utils;

import com.example.qld.models.User;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {
    private static CacheManager instance;
    private Map<Integer, User> userCache;

    private CacheManager() {
        userCache = new HashMap<>();
    }

    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    public void cacheUser(User user) {
        if (user != null) {
            userCache.put(user.getId(), user);
        }
    }

    public User getCachedUser(int userId) {
        return userCache.get(userId);
    }

    public void clearUserCache() {
        userCache.clear();
    }

    public void removeUserFromCache(int userId) {
        userCache.remove(userId);
    }
}