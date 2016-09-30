package ru.pscb.uprid.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.pscb.uprid.dao.SnilsDAO;
import ru.pscb.uprid.entity.Snils;

/**
 * Snils DAO, working with DB
 */
@Repository
public class SnilsDAOImpl implements SnilsDAO {
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public void addSnils(Snils snils) {
        sessionFactory.getCurrentSession().save(snils);
    }

}
