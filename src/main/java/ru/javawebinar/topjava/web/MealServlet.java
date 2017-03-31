package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Created by Vladimir on 26.03.2017.
 */
public class MealServlet extends HttpServlet {

    private static final Logger LOG = getLogger(MealServlet.class);
    private MealRepository repository;

    public MealServlet() {
        repository = new MemoryMealRepositoryImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        LOG.debug("doGet to meals");


        String action = Optional.ofNullable(request.getParameter("action")).orElse("");

        if (action.equals("delete")) {
            repository.delete(Integer.parseInt(request.getParameter("id")));
            response.sendRedirect("meals");
        } else if (action.equals("update")) {
            Meal meal = repository.getMealById(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/meal.jsp").forward(request, response);
        } else if (action.equals("insert")) {
            request.getRequestDispatcher("/meal.jsp").forward(request, response);
        } else{
            List<MealWithExceed> result = MealsUtil.getFilteredWithExceeded(repository.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", result);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("doPost to meals");

        request.setCharacterEncoding("UTF-8");

        Meal meal = new Meal();
        LocalDateTime dateTime = LocalDateTime.now().withSecond(0).withNano(0);
        try {
            dateTime = TimeUtil.parseLocalDateTime(request.getParameter("dateTime"));
        } catch (DateTimeParseException e) {
        }
        meal.setDateTime(dateTime);
        int calories = 0;
        try {
            calories = Integer.parseInt(request.getParameter("calories"));
        } catch (NumberFormatException e) {
        }
        meal.setCalories(calories);
        meal.setDescription(request.getParameter("description"));

        String mealId = request.getParameter("id");
        if (Objects.nonNull(mealId) && !mealId.equals("")) {
            meal.setId(Integer.valueOf(mealId));
            repository.update(meal);
        } else {
            repository.add(meal);
        }


        response.sendRedirect("meals");
    }

}
