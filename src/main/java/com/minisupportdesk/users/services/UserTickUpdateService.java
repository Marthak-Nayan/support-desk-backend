package com.minisupportdesk.users.services;

import com.minisupportdesk.Repository.TicketRepositary;
import com.minisupportdesk.Repository.UserRepositary;
import com.minisupportdesk.common.dashboard.service.StatsService;
import com.minisupportdesk.entities.Status;
import com.minisupportdesk.entities.Ticket;
import com.minisupportdesk.entities.User;
import com.minisupportdesk.error.ForbiddenException;
import com.minisupportdesk.users.DTO.UserTicketUpdateReqDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserTickUpdateService {

    private final StatsService statsService;
    private final UserRepositary userRepositary;
    private final TicketRepositary ticketRepositary;

    public void updateTicket(Long id, UserTicketUpdateReqDTO req, String username){
        User user = userRepositary.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("User Not found"));
        Ticket ticket = ticketRepositary.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Ticket Not found"));

        Status oldStatus = ticket.getStatus();
        Status newStatus = req.getStatus();

        if(ticket.getStatus()!= Status.OPEN && ticket.getStatus()!= Status.IN_PROGRESS){
            throw new ForbiddenException("You can edit ticket only when status is OPEN or IN_PROGRESS");
        }
        if(!ticket.getCreatedBy().getId().equals(user.getId())){
            throw new ForbiddenException("You can edit only your own tickets");
        }

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
