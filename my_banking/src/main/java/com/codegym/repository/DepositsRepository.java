package com.codegym.repository;

import com.codegym.model.Deposits;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
@Transactional
public class DepositsRepository implements IDepositsRepository{

    @PersistenceContext
    private EntityManager entityManager ;

    @Override
    public List<Deposits> findAll() {
        String sql = "select d from Deposits d where d.isDelete =false " ;
        TypedQuery<Deposits> query = entityManager.createQuery(sql, Deposits.class);
        return query.getResultList() ;
    }

    @Override
    public Deposits findById(Long id) {
       String sql ="select d from Deposits d where d.isDelete =false and d.id =:id";
      TypedQuery<Deposits> query = entityManager.createQuery(sql,Deposits.class).setParameter("id",id) ;
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Deposits deposits) {
        if (deposits.getId() != null) {
            entityManager.merge(deposits);
        } else {
            entityManager.persist(deposits);
        }
    }

    @Override
    public void remove(Long id) {
        Deposits deposits = findById(id);
        if (deposits != null) {
            deposits.setDelete(true);
            entityManager.merge(deposits);
        }
    }
}
