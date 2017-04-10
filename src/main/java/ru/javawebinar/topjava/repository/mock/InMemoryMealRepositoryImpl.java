package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private static final Logger LOG = getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    //1st approach
//    private UserRepository userRepository;

//    @Autowired
//    public InMemoryMealRepositoryImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//        UserMealsUtil.MEALS.forEach(meal -> this.save(meal, AuthorizedUser.id()));
//    }

    //2nd approach
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    void after() {
        UserMealsUtil.MEALS.forEach(meal -> this.save(meal, AuthorizedUser.id()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        LOG.info("save " + meal + " with userId " + userId);
        User u = userRepository.get(userId);
        if (u != null) {
            if (meal.isNew()) {//new meal
                meal.setId(counter.incrementAndGet());
                meal.setUser(u);
                repository.put(meal.getId(), meal);
            } else if (meal.getUser().getId() == userId) {//meal belongs to user
                repository.put(meal.getId(), meal);
            } else {
                meal = null;
            }

        } else {
            meal = null;
        }

        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        LOG.info("delete meal " + id + " with userId " + userId);
        Meal meal = repository.get(id);
        if (meal != null && meal.getUser().getId() == userId) {
            return repository.remove(id) != null;
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        LOG.info("get meal " + id + " with userId " + userId);
        Meal meal = repository.get(id);
        if (meal != null && meal.getUser().getId() == userId) {
            return meal;
        } else {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        LOG.info("getAll for userId=" + userId);
        return repository.values()
                .stream()
                .filter(m -> m.getUser().getId() == userId)
//                .sorted((o1, o2) -> {
//                    int i = o2.getDateTime().compareTo(o1.getDateTime());
//                    if(i!=0){
//                        return i;
//                    } else{
//                        return o2.getId().compareTo(o1.getId());
//                    }
//                })
                //correct
                .sorted(Comparator.comparing(Meal::getDateTime).reversed().thenComparing(Comparator.comparing(Meal::getId).reversed()))
                //incorrect
                //.sorted(Comparator.comparing(Meal::getDateTime).reversed().thenComparing(Meal::getId).reversed())
                .collect(Collectors.toList());
    }
}

