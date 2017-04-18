package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by Vladimir on 17.04.2017.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {

    @Autowired
    DbPopulator dbPopulator;

    @Autowired
    MealService service;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void get() throws Exception {
        Meal meal = service.get(USER_MEAL_ID_1, USER_ID);
        MATCHER.assertEquals(USER_MEAL_1, meal);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        service.get(USER_MEAL_ID_1, ADMIN_ID);
    }

    @Test
    public void delete() throws Exception {
        service.delete(USER_MEAL_ID_3, USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_2, USER_MEAL_1), service.getAll(USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(USER_MEAL_ID_3, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() throws Exception {
        MATCHER.assertCollectionEquals(Arrays.asList(USER_MEAL_6, USER_MEAL_5, USER_MEAL_4), service.getBetweenDates(LocalDate.of(2015, Month.MAY, 31), LocalDate.of(2015, Month.MAY, 31), USER_ID));
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        MATCHER.assertCollectionEquals(Arrays.asList(USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2), service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 12, 0), LocalDateTime.of(2015, Month.MAY, 31, 15, 0), USER_ID));
    }

    @Test
    public void getAll() throws Exception {
        MATCHER.assertCollectionEquals(Arrays.asList(USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1), service.getAll(USER_ID));
    }

    @Test
    public void update() throws Exception {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setDescription("UpdatedName");
        updated.setCalories(400);
        service.update(updated, USER_ID);
        MATCHER.assertEquals(updated, service.get(USER_MEAL_ID_1, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setDescription("UpdatedName");
        updated.setCalories(400);
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void save() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.now(), "MyDescription", 600);
        Meal created = service.save(newMeal, USER_ID);
        newMeal.setId(created.getId());
        MATCHER.assertCollectionEquals(Arrays.asList(newMeal, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1), service.getAll(USER_ID));

    }

}