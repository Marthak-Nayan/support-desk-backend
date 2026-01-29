package com.minisupportdesk.common.controller;

import com.minisupportdesk.common.DTO.TicketRespDTO;
import com.minisupportdesk.common.services.GetTicketServices;
import com.minisupportdesk.entities.Priority;
import com.minisupportdesk.entities.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class GetTicketController {

    private final GetTicketServices getTicketServices;

    @GetMapping("/")
    public TicketRespDTO getAllOrFilterTickets(Pageable pageable,
                                       @RequestParam(required = false) List<Status> statuses,
                                       @RequestParam(required = false) List<Priority> priorities,
                                       Authentication authentication){
        String username = authentication.getName();
        return  getTicketServices.getFilterTicket(pageable,statuses,priorities,username);
    }
}
