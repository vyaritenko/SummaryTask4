package ua.nure.yaritenko.SummaryTask4.db;

import ua.nure.yaritenko.SummaryTask4.db.entity.User;
/**
 * UserLock entity.
 *
 * @author V.Yaritenko
 *
 */
public enum UserLock {
    LOCK, UNLOCKED;

    public static UserLock getUserLock(User user) {
        int userLockId = user.getUserLockId();
        return UserLock.values()[userLockId - 1];
    }

    public String getName() {
        return name().toLowerCase();
    }
}


