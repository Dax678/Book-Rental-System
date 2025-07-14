package org.bookrental.bookrentalsystem.Notification;

import org.bookrental.bookrentalsystem.Data.Entity.User;

public interface Notifier {
    void notify (User user, String messageKey, Object... args);
}
