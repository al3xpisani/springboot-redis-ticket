package com.example.oncallinvext.service;

import com.example.oncallinvext.domain.Ticket;
import com.example.oncallinvext.repositories.TicketRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
        ticket.setCreatedAt(getTimeStampBRNow());
        ticket.setStatus("CREATED");
        String ticketDetails = formatTicketDetails(ticket);
        String isTicketsPerAttendanceReached = checkMaximumTicketsPerAttendance(ticket, tickets, ticketDetails);
        if (isTicketsPerAttendanceReached != null) return isTicketsPerAttendanceReached;
        ticketRepository.save(ticket);
        log.info("Ticket Id {} dropped to the attendance start working : {}",ticket.getId(), ticketDetails);
        return ticketDetails;
    }

    private static String formatTicketDetails(Ticket ticket) {
        return "{" +
                "\"queueName\":\"" + ticket.getQueueName() + "\"," +
                "\"createdAt\":\"" + ticket.getCreatedAt() + "\"," +
                "\"issueDescription\":\"" + ticket.getIssueDescription() + "\"," +
                "\"status\":\"" + ticket.getStatus() + "\"" +
                "}";
    }

    private static String getTimeStampBRNow() {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        format.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        Date now = new Date();
        return format.format(now);
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
        Ticket desirealizedTicket = null;
        String lastRedisTicket = redisService.getLatestRecord(ticket.getQueueName());
        if(lastRedisTicket == null) return "The queue is empty. No more tickets to be assigned at this moment for " + ticket.getQueueName();
        try {
            JSONObject jsonObject =  new JSONObject(lastRedisTicket);
            desirealizedTicket = objectMapper.readValue(jsonObject.toString(), Ticket.class);
        } catch (JsonProcessingException e) {
            log.info("ERROR ====>>>> {}",e.getOriginalMessage());
            return null;
        }
        Ticket savedTicket = ticketRepository.save(desirealizedTicket);
        if(savedTicket == null) return "Ticket could not be saved";

        redisService.dequeue(ticket.getQueueName());
        return "Tickets were processed with success! New tickets were pushed from Queue and dropped into Attendance todo list";
    }

}
