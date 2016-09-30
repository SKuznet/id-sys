package ru.pscb.uprid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.pscb.uprid.dao.SnilsDAO;
import ru.pscb.uprid.entity.Snils;

/**
 * Service for Snils
 */
@Component("SnilsService")
@Transactional
public class SnilsService {
    @Autowired
    SnilsDAO snilsDAO;

    public void addSnils(Snils snils) {
        if (snils.getSnils() == null || snils.getSnils().isEmpty()) {
            throw new IllegalArgumentException("Please, enter !");
        }
        snilsDAO.addSnils(snils);
    }

}