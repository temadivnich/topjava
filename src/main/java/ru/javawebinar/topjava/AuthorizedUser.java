package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.util.UserMealsUtil.DEFAULT_CALORIES_PER_DAY;

public class AuthorizedUser {

    public static User DefaultUser = new User(1, "default", "email@email.com", "password", Role.ROLE_USER);

    public static int id() {
        return 1;
    }

    public static int getCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}