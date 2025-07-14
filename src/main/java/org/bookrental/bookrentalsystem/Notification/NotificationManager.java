package org.bookrental.bookrentalsystem.Notification;

import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationManager {
    private final List<Notifier> notifiers;


    public NotificationManager(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    public void notifyAll(User user, String messageKey, Object... args) {
        for(Notifier notifier : notifiers) {
            notifier.notify(user, messageKey, args);
        }
    }
}
