package com.example.oncallinvext.repositories;

import com.example.oncallinvext.domain.Ticket;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    @Modifying
    @Transactional
    @Query("SELECT t FROM Ticket t WHERE t.queueName = :queueName AND t.status = 'CREATED'")
    List<Ticket> findCreatedByQueueName(String queueName);
    Ticket findOpenedTicketById(Long id);
    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.status = 'CLOSED' WHERE t.id = :id AND t.status = 'CREATED'")
    int closeOpenedTicketById(Long id);
}
