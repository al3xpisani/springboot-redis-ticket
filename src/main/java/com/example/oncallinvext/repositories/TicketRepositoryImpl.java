package com.example.oncallinvext.repositories;

import com.example.oncallinvext.domain.Ticket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public abstract class TicketRepositoryImpl implements TicketRepository {
    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public List<Ticket> findCreatedByQueueName(String queueName) {
//        String jpql = "SELECT t FROM Ticket t WHERE t.queueName = :queueName AND t.status = 'CREATED'";
//        TypedQuery<Ticket> query = entityManager.createQuery(jpql, Ticket.class);
//        query.setParameter("queueName", queueName);
//        return query.getResultList();
//    }
    @Override
    public Ticket findOpenedTicketById(Long id) {
        String jpql = "SELECT t FROM Ticket t WHERE t.id = :id AND t.status = 'CREATED'";
        TypedQuery<Ticket> query = entityManager.createQuery(jpql, Ticket.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
}
