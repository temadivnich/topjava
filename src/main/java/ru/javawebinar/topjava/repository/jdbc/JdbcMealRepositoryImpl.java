package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final SimpleJdbcInsert insertMeal;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public JdbcMealRepositoryImpl(DataSource dataSource) {
        this.insertMeal = new SimpleJdbcInsert(dataSource)
                .usingGeneratedKeyColumns("id")
                .withTableName("meals");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Objects.requireNonNull(meal);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("description", meal.getDescription())
                .addValue("datetime", meal.getDateTime())
                .addValue("calories", meal.getCalories())
                .addValue("userid", userId);

        if (meal.isNew()) {
            Number id = insertMeal.executeAndReturnKey(params);
            meal.setId(id.intValue());
            return meal;
        } else {
            return 1 == namedParameterJdbcTemplate.update(
                    "UPDATE meals SET description=:description, datetime=:datetime, calories=:calories WHERE userid=:userid AND id=" + meal.getId(), params)
                    ? meal : null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
//        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id).addValue("userid", userId);
//        return 1 == namedParameterJdbcTemplate.update("DELETE FROM meals WHERE userid=:userid AND id=:userid", params);
        return 1 == jdbcTemplate.update("DELETE FROM meals WHERE id = ? AND userid= ? ", id, userId);
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND userid=?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE userid = ? ORDER BY datetime DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE userid = ? AND datetime BETWEEN ? AND ? ORDER BY datetime DESC", ROW_MAPPER, userId, startDate, endDate);
    }
}
