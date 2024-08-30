package com.example.oncallinvext.controllers;

import com.example.oncallinvext.domain.Category;
import com.example.oncallinvext.domain.Ticket;
import com.example.oncallinvext.repositories.TicketRepository;
import com.example.oncallinvext.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1")
public class TicketController {
    private static final Logger log = LoggerFactory.getLogger(TicketController.class);
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;

    public TicketController(TicketRepository ticketRepository, TicketService ticketService) {
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
    }

    @RequestMapping("/tickets")
    Iterable<Ticket> getById(){
        return ticketRepository.findAll();
    }

    @Async
    @Transactional
    @PostMapping("/tickets")
    CompletableFuture<String> createTicket(@RequestBody Ticket ticket){
        String queueStatus = ticketService.processRedisTicket(ticket);
        return CompletableFuture.completedFuture(queueStatus);
    }

    @Async
    @Transactional
    @PutMapping("/tickets/{id}")
    CompletableFuture<String> processClosedTicket(@PathVariable Long id){
        String queueStatus = ticketService.processClosedTicket(id);
        return CompletableFuture.completedFuture(queueStatus);
    }
}
