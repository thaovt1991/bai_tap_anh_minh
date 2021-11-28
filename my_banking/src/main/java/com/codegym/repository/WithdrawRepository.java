package com.codegym.repository;

import com.codegym.model.Withdraw;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
@Transactional
public class WithdrawRepository implements IWithdrawRepository{
    @PersistenceContext
    private EntityManager entityManager ;

    @Override
    public List findAll() {
        String hql ="select w from Withdraw w where w.isDelete = false " ;
        TypedQuery<Withdraw> query = entityManager.createQuery(hql,Withdraw.class) ;
        return query.getResultList();
    }

    @Override
    public Withdraw findById(Long id) {
        String hql ="select w from Withdraw w where w.isDelete = false and w.id =:id";
        TypedQuery<Withdraw> query = entityManager.createQuery(hql,Withdraw.class).setParameter("id",id) ;
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Withdraw withdraw) {
        if(withdraw.getId()!=null){
            entityManager.merge(withdraw);
        }else
            entityManager.persist(withdraw);
    }

    @Override
    public void remove(Long id) {
        Withdraw withdraw = findById(id) ;
        if(withdraw!=null){
            withdraw.setDelete(true);
            entityManager.merge(withdraw) ;
        }
    }


}
