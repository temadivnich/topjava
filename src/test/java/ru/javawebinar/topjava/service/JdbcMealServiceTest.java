package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * Created by Vladimir on 30.04.2017.
 */
@ActiveProfiles({Profiles.ACTIVE_DB, Profiles.JDBC})
public class JdbcMealServiceTest extends MealServiceTest{
}
