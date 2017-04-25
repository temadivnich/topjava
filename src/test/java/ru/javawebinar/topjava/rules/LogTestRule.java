package ru.javawebinar.topjava.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Vladimir on 25.04.2017.
 */
public class LogTestRule implements TestRule {


    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                LocalDateTime start = LocalDateTime.now();
                base.evaluate();
                LocalDateTime end = LocalDateTime.now();

                String message = "Test " + description + " lasted in milliseconds: " + ChronoUnit.MILLIS.between(start, end);
                System.out.println(message);
            }
        };
    }
}
