package com.minisupportdesk.admin.services;

import com.minisupportdesk.Repository.TicketRepositary;
import com.minisupportdesk.Repository.UserRepositary;
import com.minisupportdesk.admin.DTO.AdminTicketUpdateReqDTO;
import com.minisupportdesk.entities.Ticket;
import com.minisupportdesk.entities.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminTickUpdateService {

    private final UserRepositary userRepositary;
    private final TicketRepositary ticketRepositary;

    @Transactional
    public void updateTicket(Long id, AdminTicketUpdateReqDTO req,String username){
        Ticket ticket = ticketRepositary.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Ticket Not found"));

        Optional.ofNullable(req.getTitle()).ifPresent(ticket::setTitle);
        Optional.ofNullable(req.getDescription()).ifPresent(ticket::setDescription);
        Optional.ofNullable(req.getStatus()).ifPresent(ticket::setStatus);
        Optional.ofNullable(req.getPriority()).ifPresent(ticket::setPriority);
    }
}
