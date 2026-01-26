package com.minisupportdesk.common.controller;


import com.minisupportdesk.common.DTO.RoomEventDTO;
import com.minisupportdesk.common.services.CommentServices;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CommentServices commentServices;

    @MessageMapping("/sendComment/{ticketId}")
    public void sendComment(@DestinationVariable Long ticketId, @Payload RoomEventDTO message){
        commentServices.sendComment(ticketId,message);
    }

    @MessageMapping("/addUser/{ticketId}")
    public void addUser(@DestinationVariable Long ticketId, @Payload RoomEventDTO message){
        commentServices.addUser(ticketId,message);
    }

    @MessageMapping("/outUser/{ticketId}")
    public void outUser(@DestinationVariable Long ticketId, @Payload RoomEventDTO message){
       commentServices.outUser(ticketId,message);
    }

}
