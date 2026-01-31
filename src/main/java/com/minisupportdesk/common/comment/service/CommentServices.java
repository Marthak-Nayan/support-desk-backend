package com.minisupportdesk.common.comment.service;

import com.minisupportdesk.Repository.CommentRepository;
import com.minisupportdesk.Repository.TicketRepositary;
import com.minisupportdesk.Repository.UserRepositary;
import com.minisupportdesk.common.comment.DTO.AllChatRespDTO;
import com.minisupportdesk.common.comment.DTO.CommentDTO;
import com.minisupportdesk.common.comment.DTO.RoomEventDTO;
import com.minisupportdesk.entities.Comment;
import com.minisupportdesk.entities.Role;
import com.minisupportdesk.entities.Ticket;
import com.minisupportdesk.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServices {

    private final UserRepositary userRepositary;
    private final TicketRepositary ticketRepositary;
    private final CommentRepository commentRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public AllChatRespDTO getAllChatsFromTicketId(Long ticketId){

        //List<Comment> comments= commentRepository.findByTicket_Id(ticketId);
        List<Comment> comments = commentRepository.findByTicket_Id(ticketId);

        List<CommentDTO> commentDTOS = comments
                .stream()
                .map(comment -> CommentDTO.builder()
                        .message(comment.getMessage())
                        .senderId(comment.getSender().getId())
                        .build()).toList();

        return AllChatRespDTO.builder()
                .comment(commentDTOS)
                .tickedId(ticketId)
                .build();

    }

    public void sendComment(Long ticketId, RoomEventDTO message){
        User user = userRepositary.findById(message.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(user.getRole().equals(Role.USER)) {
            Ticket ticket = ticketRepositary.findByIdAndCreatedBy_Id(ticketId, message.getUserId());
            if (ticket == null) {
                throw new AccessDeniedException("You are not part of this room");
            }
            simpMessagingTemplate.convertAndSend("/topic/room/"+ticket,message);
        }else if (user.getRole().equals(Role.ADMIN)){
            Ticket ticket = ticketRepositary.findById(ticketId).orElseThrow(()-> new IllegalArgumentException("Ticket Not Found"));
            simpMessagingTemplate.convertAndSend("/topic/room/"+ticket.getId(),message);
        }
    }

    public void addUser(Long ticketId, RoomEventDTO message){
        User user = userRepositary.findById(message.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Ticket ticket = ticketRepositary.findById(ticketId).orElseThrow(()-> new IllegalArgumentException("Ticket Not Found"));
        message.setEventType("ONLINE");
        message.setRoomId(ticketId);
        simpMessagingTemplate.convertAndSend("/topic/room/"+ticket.getId(),message);
    }

    public void outUser(Long ticketId, RoomEventDTO message){
        User user = userRepositary.findById(message.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Ticket ticket = ticketRepositary.findById(ticketId).orElseThrow(()-> new IllegalArgumentException("Ticket Not Found"));
        message.setEventType("OFFLINE");
        message.setRoomId(ticketId);
        simpMessagingTemplate.convertAndSend("/topic/room/"+ticketId,message);
    }
}
