package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Vladimir on 27.03.2017.
 */
public class MemoryMealRepositoryImpl implements MealRepository {

    Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void add(Meal meal) {

        if(meal.isNew()){
           meal.setId(counter.getAndIncrement());
        }
        repository.put(meal.getId(),meal);
    }

    @Override
    public void delete(int mealId) {
        repository.remove(mealId);
    }

    @Override
    public void update(Meal meal) {
        if(!meal.isNew()){
            repository.put(meal.getId(),meal);
        }
    }

    @Override
    public Meal getMealById(int id) {
        return repository.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return repository.values().stream().collect(Collectors.toList());
    }
}
