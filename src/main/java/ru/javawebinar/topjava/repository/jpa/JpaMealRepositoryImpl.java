package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {

        meal.setUser(em.getReference(User.class, userId));

        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            Query query = em.createQuery("UPDATE Meal SET description=:description, calories=:calories, dateTime=:date_time WHERE id=:id AND user.id=:user_id")
                    .setParameter("description", meal.getDescription())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("date_time", meal.getDateTime())
                    .setParameter("id", meal.getId())
                    .setParameter("user_id", userId);
            return query.executeUpdate() != 0 ? meal : null;
        }
    }

    @Transactional
    @Override
    public boolean delete(int id, int userId) {

//        Query query = em.createQuery("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId");
//        return query
//                .setParameter("id", id)
//                .setParameter("userId", userId)
//                .executeUpdate() != 0;

        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;

    }

    @Override
    public Meal get(int id, int userId) {
//        TypedQuery<Meal> query =
//                em.createQuery("SELECT m FROM Meal m WHERE m.id=?1 AND m.user.id=?2", Meal.class)
//                        .setParameter(1, id)
//                        .setParameter(2, userId);
//        List<Meal> result = query.getResultList();
//        return DataAccessUtils.singleResult(result);

        return DataAccessUtils.singleResult(
                em.createNamedQuery(Meal.GET, Meal.class)
                        .setParameter(1, id)
                        .setParameter(2, userId).getResultList()
        );

    }

    @Override
    public List<Meal> getAll(int userId) {
//        TypedQuery<Meal> query =
//                em.createQuery("SELECT m FROM Meal m WHERE m.user.id=?1 ORDER BY m.dateTime DESC", Meal.class)
//                        .setParameter(1, userId);
//        List<Meal> result = query.getResultList();
//        return result;
        return em.createNamedQuery(Meal.GET_ALL, Meal.class).setParameter(1, userId).getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
//        TypedQuery<Meal> query =
//                em.createQuery("SELECT m FROM Meal m WHERE m.user.id=?1 AND m.dateTime BETWEEN ?2 AND ?3 ORDER BY dateTime DESC", Meal.class)
//                        .setParameter(1, userId)
//                        .setParameter(2, startDate)
//                        .setParameter(3, endDate);
//        List<Meal> result = query.getResultList();
//        return result;

        return em.createNamedQuery(Meal.GET_BETWEEN, Meal.class)
                .setParameter(1, userId)
                .setParameter(2, startDate)
                .setParameter(3, endDate)
                .getResultList();

    }
}