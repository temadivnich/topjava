package ru.javawebinar.topjava.web;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    ConfigurableApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if ("filter".equals(action)) {
            doGet(request, response);
        } else {

            String id = request.getParameter("id");

            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")), AuthorizedUser.DefaultUser);

            LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            if (id.isEmpty()) {
                mealRestController.create(meal);
            } else {
                mealRestController.update(meal, Integer.valueOf(id));
            }

            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        MealRestController mealRestController = appCtx.getBean(MealRestController.class);
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                LOG.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = action.equals("create") ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, AuthorizedUser.DefaultUser) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                break;
            case "all":
            default:
                LOG.info("getByFilter");
                request.setAttribute("meals", mealRestController.getByFilter(
                        putAndGetLocalDateParam("startDate", LocalDate.MIN, request),
                        putAndGetLocalDateParam("endDate", LocalDate.MAX, request),
                        putAndGetLocalTimeParam("startTime", LocalTime.MIN, request),
                        putAndGetLocalTimeParam("endTime", LocalTime.MAX, request)));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }
    private static LocalDate putAndGetLocalDateParam(String param, LocalDate ld, HttpServletRequest request) {
        String value = request.getParameter(param);
        if (!Strings.isNullOrEmpty(value)) {
            ld = DateTimeUtil.parseLocalDate(value);
            request.setAttribute(param, ld);
        }
        return ld;
    }

    private static LocalTime putAndGetLocalTimeParam(String param, LocalTime lt, HttpServletRequest request) {
        String value = request.getParameter(param);
        if (!Strings.isNullOrEmpty(value)) {
            lt = DateTimeUtil.parseLocalTime(value);
            request.setAttribute(param, lt);
        }
        return lt;
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
