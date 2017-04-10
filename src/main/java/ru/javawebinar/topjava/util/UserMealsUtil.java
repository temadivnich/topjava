package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static final User ADMIN = new User(null, "admin", "admin@gmail.com", "password1", Role.ROLE_ADMIN);
    public static final User USER = new User(null, "user", "user@gmail.com", "password2", Role.ROLE_USER);

    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2017, Month.APRIL, 29, 10, 0), "Завтрак", 500, null),
            new Meal(LocalDateTime.of(2017, Month.APRIL, 29, 13, 0), "Обед", 1000, null),
            new Meal(LocalDateTime.of(2017, Month.APRIL, 29, 20, 0), "Ужин", 500, null),
            new Meal(LocalDateTime.of(2017, Month.APRIL, 30, 10, 0), "Завтрак", 1000, null),
            new Meal(LocalDateTime.of(2017, Month.APRIL, 30, 13, 0), "Обед", 500, null),
            new Meal(LocalDateTime.of(2017, Month.APRIL, 30, 20, 0), "Ужин", 510, null)
    );

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static void main(String[] args) {
        List<MealWithExceed> mealsWithExceeded = getFilteredWithExceeded(MEALS, LocalDate.MIN, LocalDate.MAX, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsWithExceeded.forEach(System.out::println);

        System.out.println(getFilteredWithExceededByCycle(MEALS, LocalTime.of(7, 0), LocalTime.of(12, 0), DEFAULT_CALORIES_PER_DAY));
    }

    //Deprecated
//    public static List<MealWithExceed> getWithExceeded(Collection<Meal> meals, int caloriesPerDay) {
//        return getFilteredWithExceeded(meals, LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
//    }

    public static List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals,
                                                               LocalDate startDate,
                                                               LocalDate endDate,
                                                               LocalTime startTime,
                                                               LocalTime endTime,
                                                               int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                //.filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(), LocalDateTime.of(startDate,startTime),LocalDateTime.of(endDate,endTime)))

                .map(meal -> createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<MealWithExceed> getFilteredWithExceededByCycle(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        meals.forEach(meal -> caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum));

        final List<MealWithExceed> mealsWithExceeded = new ArrayList<>();
        meals.forEach(meal -> {
            if (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
                mealsWithExceeded.add(createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
            }
        });
        return mealsWithExceeded;
    }

    public static MealWithExceed createWithExceed(Meal meal, boolean exceeded) {
        return new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }
}