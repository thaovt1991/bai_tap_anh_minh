package com.codegym.repository;

import com.codegym.model.Transfer;
import com.codegym.model.Withdraw;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
@Transactional
public class TransferRepository implements ITransferRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Transfer> findAll() {
        String hql ="select t from Transfer t where t.isDelete = false";
        TypedQuery<Transfer> query = entityManager.createQuery(hql, Transfer.class) ;
        return query.getResultList();
    }

    @Override
    public Transfer findById(Long id) {
        String hql ="select t from Transfer t where t.isDelete = false and t.id =:id";
        TypedQuery<Transfer> query = entityManager.createQuery(hql,Transfer.class).setParameter("id",id) ;
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Transfer transfer) {
      if(transfer.getId()!= null){
          entityManager.merge(transfer) ;
      }else
          entityManager.persist(transfer);
    }

    @Override
    public void remove(Long id) {
        Transfer transfer = findById(id) ;
        if(transfer!=null){
            transfer.setDelete(true);
            entityManager.merge(transfer) ;
        }
    }
}
