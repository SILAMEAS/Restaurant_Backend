package com.sila.utlis.context;

import com.sila.exception.BadRequestException;
import com.sila.model.User;

import java.util.Objects;

public class UserContext {

    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void setUser(User user) {
        currentUser.set(user);
    }

    public static User getUser() {
        if (Objects.isNull(currentUser.get())) {
            throw new BadRequestException("You are not logged in can't get user; note : UserContext");
        }
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}