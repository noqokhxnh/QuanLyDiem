package com.example.qld.utils;

import com.example.qld.models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Lớp quản lý bộ nhớ đệm cho người dùng
 * Dùng để lưu trữ tạm thời thông tin người dùng trong bộ nhớ
 */
public class CacheManager {
    private static CacheManager instance;
    private Map<Integer, User> userCache;

    /**
     * Constructor riêng tư để đảm bảo chỉ có một thể hiện duy nhất
     */
    private CacheManager() {
        userCache = new HashMap<>();
    }

    /**
     * Lấy thể hiện duy nhất của CacheManager (Singleton pattern)
     * @return Thể hiện duy nhất của CacheManager
     */
    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    /**
     * Lưu người dùng vào bộ nhớ đệm
     * @param user Đối tượng User cần lưu vào cache
     */
    public void cacheUser(User user) {
        if (user != null) {
            userCache.put(user.getId(), user);
        }
    }

    /**
     * Lấy người dùng từ bộ nhớ đệm dựa trên ID
     * @param userId ID của người dùng cần lấy
     * @return Đối tượng User nếu tìm thấy trong cache, ngược lại trả về null
     */
    public User getCachedUser(int userId) {
        return userCache.get(userId);
    }

    /**
     * Xóa tất cả người dùng khỏi bộ nhớ đệm
     */
    public void clearUserCache() {
        userCache.clear();
    }

    /**
     * Xóa người dùng khỏi bộ nhớ đệm dựa trên ID
     * @param userId ID của người dùng cần xóa
     */
    public void removeUserFromCache(int userId) {
        userCache.remove(userId);
    }
}