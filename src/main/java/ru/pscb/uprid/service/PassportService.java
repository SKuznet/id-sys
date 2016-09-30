package ru.pscb.uprid.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.pscb.uprid.controller.AppController;
import ru.pscb.uprid.dao.PassportDAO;
import ru.pscb.uprid.entity.Passport;

/**
 * Service for passport
 */
@Component("PassportService")
@Transactional
public class PassportService {
    private final static Logger log = LoggerFactory.getLogger(AppController.class);

    @Autowired
    PassportDAO passportDAO;

    public void addPassport(Passport passport) {
        if (passport.getNumber() == null || passport.getNumber().isEmpty()) {
            log.error("Не введен номер паспорта");
            throw new IllegalArgumentException("Please, enter number!");
        }
        if (passport.getSeries() == null || passport.getSeries().isEmpty()) {
            log.error("Не введена серия паспорта");
            throw new IllegalArgumentException("Please, enter series!");
        }
        if (passport.getIssueDate() == null || passport.getIssueDate().isEmpty()) {
            log.error("Не введена дата выдачи паспорта");
            throw new IllegalArgumentException("Please, enter issue date!");
        }
        passportDAO.addPassport(passport);
    }

}
