package com.example.oncallinvext.service;

import com.example.oncallinvext.domain.Ticket;
import com.example.oncallinvext.repositories.TicketRepository;
import com.example.oncallinvext.utils.TimeStamp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);
    private final RedisService redisService;
    private final TicketRepository ticketRepository;
    private final ObjectMapper objectMapper;
    @Autowired
    public TicketServiceImpl(RedisService redisService, TicketRepository ticketRepository, ObjectMapper objectMapper) {
        this.redisService = redisService;
        this.ticketRepository = ticketRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public String processRedisTicket(Ticket ticket) {
        List<Ticket> tickets = ticketRepository.findCreatedByQueueName(ticket.getQueueName());
        ticket.setCreatedAt(TimeStamp.getTimeStampBRNow());
        ticket.setStatus("CREATED");
        String isTicketsPerAttendanceReached = checkMaximumTicketsPerAttendance(ticket, tickets, ticket.toString());
        if (isTicketsPerAttendanceReached != null) return isTicketsPerAttendanceReached;
        ticketRepository.save(ticket);
        log.info("Ticket Id {} dropped to the attendance start working : {}",ticket.getId(), ticket);
        return ticket.toString();
    }

    private String checkMaximumTicketsPerAttendance(Ticket ticket, List<Ticket> tickets, String ticketDetails) {
        int realTicketsLength = tickets.size()+1;
        if(realTicketsLength > 3){
            redisService.enqueue(ticket.getQueueName(), ticketDetails);
            log.info("Ticket queued to be processed later : {}", ticketDetails);
            return "Ticket queued to be processed later : Queue name : " + ticket.getQueueName() + " Description : " + ticket.getIssueDescription();
        }
        return null;
    }

    @Override
    @Transactional
    public String processClosedTicket(Long id) {
        Ticket ticket = ticketRepository.findOpenedTicketById(id);
        if(ticket == null) return "Ticket does not exist";
        int closedTicketDB = ticketRepository.closeOpenedTicketById(id);
        if(closedTicketDB == 0) return "The existing ticket was not closed";
        Ticket desirealizedTicket;
        String lastRedisTicket = redisService.getLatestRecord(ticket.getQueueName());
        if(lastRedisTicket == null) return "The queue is empty. No more tickets to be assigned at this moment for " + ticket.getQueueName();
        try {
            JSONObject jsonObject =  new JSONObject(lastRedisTicket);
            desirealizedTicket = objectMapper.readValue(jsonObject.toString(), Ticket.class);
        } catch (JsonProcessingException e) {
            log.info("ERROR ====>>>> {}",e.getOriginalMessage());
            return null;
        }
        ticketRepository.save(desirealizedTicket);
        redisService.dequeue(ticket.getQueueName());
        return "Tickets were processed with success! New tickets were pushed from Queue and dropped into Attendance todo list";
    }

}
