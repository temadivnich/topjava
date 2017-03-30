package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by Vladimir on 27.03.2017.
 */
public interface MealRepository {
    void add(Meal meal);
    void delete(int mealId);
    void update(Meal meal);
    Meal getMealById(int id);
    List<Meal> getAll();

}
