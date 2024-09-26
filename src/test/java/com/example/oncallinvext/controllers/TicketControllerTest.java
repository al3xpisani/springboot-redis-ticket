package com.example.oncallinvext.controllers;

import com.example.oncallinvext.domain.Ticket;
import com.example.oncallinvext.repositories.TicketRepository;
import com.example.oncallinvext.service.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(TicketController.class)

public class TicketControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TicketRepository targetRepo;

    @MockBean
    private TicketService ticketService;

    @Test
    @DisplayName("GET /api/v1/tickets - Success")
    void testGetAllTargetsSuccess() throws Exception {
        Ticket mockTicket = new Ticket();
        mockTicket.setId(1L);
        mockTicket.setQueueName("Sample Ticket");
        when(targetRepo.findAll()).thenReturn(Arrays.asList(mockTicket));

        MvcResult result = mockMvc.perform(get("/api/v1/tickets"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println("Response ******: " + jsonResponse);

        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tickets")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        response.andDo(print())
                .andExpect(jsonPath("$[0].queueName", is(mockTicket.getQueueName())))
                .andExpect(jsonPath("$[0].id", is(mockTicket.getId().intValue())));
    }

    @Test
    @DisplayName("GET /api/v1/tickets - No Content")
    void testGetAllTargetsEmpty() throws Exception {
        Iterable<Ticket> noTargets = Arrays.asList();

        given(targetRepo.findAll()).willReturn(noTargets);

        mockMvc.perform(get("/api/v1/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
