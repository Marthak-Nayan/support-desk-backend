package com.minisupportdesk.common.ticket.service;

import com.minisupportdesk.Repository.TicketRepositary;
import com.minisupportdesk.Repository.UserRepositary;
import com.minisupportdesk.common.dashboard.service.StatsService;
import com.minisupportdesk.common.ticket.DTO.CreateTicketReqDTO;
import com.minisupportdesk.common.ticket.DTO.CreateTicketRespDTO;
import com.minisupportdesk.entities.Status;
import com.minisupportdesk.entities.Ticket;
import com.minisupportdesk.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateTicketService {

    private final UserRepositary userRepositary;
    private final TicketRepositary ticketRepositary;
    private final StatsService statsService;

    @Transactional
    public CreateTicketRespDTO createTicket(CreateTicketReqDTO req,String username){
        User user = userRepositary.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Ticket ticket = Ticket.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .createdBy(user)
                .priority(req.getPriority())
                .createdAt(LocalDateTime.now())
                .status(Status.OPEN)
                .build();

        ticketRepositary.save(ticket);

        statsService.increment("tickets:today");
        statsService.increment("tickets:week");
        statsService.increment("tickets:open");

        statsService.pushStats();


        return CreateTicketRespDTO.builder()
                .createByName(username)
                .message("Ticket Created Successfully")
                .title(req.getTitle())
                .build();
    }

}
