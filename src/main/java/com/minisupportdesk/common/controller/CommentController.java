package com.minisupportdesk.common.controller;

import com.minisupportdesk.common.DTO.AllChatRespDTO;
import com.minisupportdesk.common.services.CommentServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/getComments")
public class CommentController {

    private final CommentServices commentServices;


    @PutMapping("/{ticketId}")
    public AllChatRespDTO getOldComment(@PathVariable Long ticketId){
        return commentServices.getAllChatsFromTicketId(ticketId);
    }

}
