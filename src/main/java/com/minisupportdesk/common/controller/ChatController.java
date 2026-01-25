package com.minisupportdesk.common.controller;


import com.minisupportdesk.Repository.CommentRepository;
import com.minisupportdesk.common.DTO.AllChatRespDTO;
import com.minisupportdesk.common.services.ChatServices;
import com.minisupportdesk.entities.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatServices chatServices;

    @GetMapping("/getChats/{ticketId}")
    public AllChatRespDTO getOldChats(@PathVariable Long roomId){
        return chatServices.getAllChatsFromTicketId(roomId);
    }

}
