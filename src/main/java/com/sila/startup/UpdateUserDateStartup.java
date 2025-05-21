package com.sila.startup;

import com.sila.model.User;
import com.sila.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateUserDateStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    public UpdateUserDateStartup(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<User> users = userRepository.findAllByCreatedAtIsNullOrUpdatedAtIsNull();
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        List<User> toUpdateUsers = new ArrayList<>();
        for (User user : users) {
            boolean hasNullValue = false;
            if (user.getCreatedAt() == null) {
                user.setCreatedAt(getCurrentDateTime());
                hasNullValue = true;
            }
            if (user.getUpdatedAt() == null) {
                user.setUpdatedAt(getCurrentDateTime());
                hasNullValue = true;
            }
            if (hasNullValue) {
                toUpdateUsers.add(user);
            }
        }
        if (!CollectionUtils.isEmpty(toUpdateUsers)) {
            userRepository.saveAll(toUpdateUsers);
        }
    }

    private static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
