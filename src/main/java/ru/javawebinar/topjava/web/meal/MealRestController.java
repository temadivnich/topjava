package ru.javawebinar.topjava.web.meal;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.ValidationUtil.checkIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger LOG = getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public Meal create(Meal meal) {;
        checkNew(meal);
        return service.save(meal, AuthorizedUser.id());
    }

    public void update(Meal meal, int id) {
        LOG.info("update meal " + id + " for user " + AuthorizedUser.id());
        checkIdConsistent(meal, id);
        service.save(meal, AuthorizedUser.id());
    }

    public void delete(int id) {
        service.delete(id, AuthorizedUser.id());
    }

    public Meal get(int id) {
        return service.get(id, AuthorizedUser.id());
    }

    public List<Meal> getAll() {
        return service.getAll(AuthorizedUser.id());
    }

    public List<MealWithExceed> getByFilter(HttpServletRequest request) {
        LOG.info("getByFilter for userId=" + AuthorizedUser.id());
        return UserMealsUtil.getFilteredWithExceeded(
                    service.getAll(AuthorizedUser.id()),
                    putAndGetLocalDateParam("startDate", LocalDate.MIN, request),
                    putAndGetLocalDateParam("endDate", LocalDate.MAX, request),
                    putAndGetLocalTimeParam("startTime", LocalTime.MIN, request),
                    putAndGetLocalTimeParam("endTime", LocalTime.MAX, request),
                    UserMealsUtil.DEFAULT_CALORIES_PER_DAY);
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
}