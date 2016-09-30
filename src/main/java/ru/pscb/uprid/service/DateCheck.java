package ru.pscb.uprid.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.pscb.uprid.controller.AppController;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Check date with ISO-8601 calendar system
 */
@Component
public class DateCheck {
    private final static Logger log = LoggerFactory.getLogger(AppController.class);
    private LocalDate dateNow = LocalDate.now();

    /**
     * Проверка даты рождения - должно быть не из будущего и человеку не может быть больше 123 лет (рекорд жизни 122 года)
     */
    public boolean checkBirthdayDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            return localDate.getYear() < (dateNow.getYear()) && localDate.getYear() > (dateNow.getYear() - 122);
        } catch (DateTimeException e) {
            log.error("Wrong check birthday date " + e);
            return false;
        }
    }

    /**
     * Проверка выдачи паспорта
     */
    public boolean checkIssuePassportDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            return localDate.getYear() <= (dateNow.getYear()) && localDate.getYear() > (dateNow.getYear() - 77);
        } catch (DateTimeException e) {
            log.error("Wrong check passport issue date " + e);
            return false;
        }
    }

    /**
     * Проверка пола
     */
    public boolean checkGender(String gender) {
        gender = gender.toLowerCase();
        if (gender.equals("м") || gender.equals("ж")) {
            return true;
        } else {
            log.error("Wrong gender from request.");
            return false;
        }
    }

}
