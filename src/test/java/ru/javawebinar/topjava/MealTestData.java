package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID_1 = START_SEQ + 2;
    public static final int USER_MEAL_ID_2 = START_SEQ + 3;
    public static final int USER_MEAL_ID_3 = START_SEQ + 4;
    public static final int USER_MEAL_ID_4 = START_SEQ + 5;
    public static final int USER_MEAL_ID_5 = START_SEQ + 6;
    public static final int USER_MEAL_ID_6 = START_SEQ + 7;
    public static final int ADMIN_MEAL_ID_1 = START_SEQ + 8;
    public static final int ADMIN_MEAL_ID_2 = START_SEQ + 9;

    public static final Meal USER_MEAL_1 = new Meal(USER_MEAL_ID_1, DateTimeUtil.parseLocalDateTime("2015-05-30 10:00"), "Завтрак", 500);
    public static final Meal USER_MEAL_2 = new Meal(USER_MEAL_ID_2, DateTimeUtil.parseLocalDateTime("2015-05-30 13:00"), "Обед", 1000);
    public static final Meal USER_MEAL_3 = new Meal(USER_MEAL_ID_3, DateTimeUtil.parseLocalDateTime("2015-05-30 20:00"), "Ужин", 500);
    public static final Meal USER_MEAL_4 = new Meal(USER_MEAL_ID_4, DateTimeUtil.parseLocalDateTime("2015-05-31 10:00"), "Завтрак", 1000);
    public static final Meal USER_MEAL_5 = new Meal(USER_MEAL_ID_5, DateTimeUtil.parseLocalDateTime("2015-05-31 13:00"), "Обед", 500);
    public static final Meal USER_MEAL_6 = new Meal(USER_MEAL_ID_6, DateTimeUtil.parseLocalDateTime("2015-05-31 20:00"), "Ужин", 510);
    public static final Meal ADMIN_MEAL_1 = new Meal(ADMIN_MEAL_ID_1, DateTimeUtil.parseLocalDateTime("2015-06-01 14:00"), "Админ ланч", 510);
    public static final Meal ADMIN_MEAL_2 = new Meal(ADMIN_MEAL_ID_2, DateTimeUtil.parseLocalDateTime("2015-06-01 21:00"), "Админ ланч", 1500);

    public static final ModelMatcher<Meal> MATCHER = new ModelMatcher<>();

}
