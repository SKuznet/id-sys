package ru.pscb.uprid.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.pscb.uprid.dao.PassportDAO;
import ru.pscb.uprid.entity.Passport;

/**
 * DAO passport, working with DB
 */
@Repository
public class PassportDAOImpl implements PassportDAO {
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public void addPassport(Passport passport) {
        sessionFactory.getCurrentSession().save(passport);
    }

}
