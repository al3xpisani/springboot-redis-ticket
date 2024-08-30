package com.example.oncallinvext.service;

import com.example.oncallinvext.domain.Ticket;

public interface TicketService {
    String processRedisTicket(Ticket ticket);
    String processClosedTicket(Long id);
}
