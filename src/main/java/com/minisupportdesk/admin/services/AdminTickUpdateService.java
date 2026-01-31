package com.minisupportdesk.admin.services;

import com.minisupportdesk.Repository.TicketRepositary;
import com.minisupportdesk.Repository.UserRepositary;
import com.minisupportdesk.admin.DTO.AdminTicketUpdateReqDTO;
import com.minisupportdesk.common.dashboard.service.StatsService;
import com.minisupportdesk.entities.Status;
import com.minisupportdesk.entities.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminTickUpdateService {

    private final StatsService statsService;
    private final UserRepositary userRepositary;
    private final TicketRepositary ticketRepositary;

    @Transactional
    public void updateTicket(Long id, AdminTicketUpdateReqDTO req,String username){
        userRepositary.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        Ticket ticket = ticketRepositary.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Ticket Not found"));

        Status oldStatus = ticket.getStatus();
        Status newStatus = req.getStatus();

        Optional.ofNullable(req.getTitle()).ifPresent(ticket::setTitle);
        Optional.ofNullable(req.getDescription()).ifPresent(ticket::setDescription);
        Optional.ofNullable(req.getStatus()).ifPresent(ticket::setStatus);
        Optional.ofNullable(req.getPriority()).ifPresent(ticket::setPriority);


        if(newStatus != null){
            if(oldStatus == Status.OPEN && newStatus != Status.OPEN){
                statsService.decrement("tickets:open");
            } else if (oldStatus != Status.OPEN && newStatus == Status.OPEN) {
                statsService.increment("tickets:open");
            }

            if (oldStatus != Status.RESOLVED && newStatus == Status.RESOLVED) {
                statsService.increment("tickets:resolved");
            } else if (oldStatus == Status.RESOLVED && newStatus != Status.RESOLVED) {
                statsService.decrement("tickets:resolved");
            }
        }

        ticketRepositary.save(ticket);

        statsService.pushStats();

        log.info("Admin [{}] updated ticket [{}] from {} to {}",
                username, id, oldStatus, newStatus);
    }
}
