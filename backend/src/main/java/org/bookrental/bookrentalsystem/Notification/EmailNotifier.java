package org.bookrental.bookrentalsystem.Notification;

import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class EmailNotifier implements Notifier {
    private final MessageSource messageSource;

    public EmailNotifier(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void notify(User user, String messageKey, Object... args) {
        String message = messageSource.getMessage(
                messageKey,
                args,
                Locale.ENGLISH
        );
        //Email Sending Simulation
        System.out.printf("[EMAIL] To: %s | Message: %s", user.getEmail(), message);
    }
}
