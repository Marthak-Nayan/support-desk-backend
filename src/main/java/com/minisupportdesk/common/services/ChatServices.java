package com.minisupportdesk.common.services;

import com.minisupportdesk.Repository.CommentRepository;
import com.minisupportdesk.common.DTO.AllChatRespDTO;
import com.minisupportdesk.common.DTO.CommentDTO;
import com.minisupportdesk.entities.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServices {

    private final CommentRepository commentRepository;

    public AllChatRespDTO getAllChatsFromTicketId(Long roomId){

        //List<Comment> comments= commentRepository.findByTicket_Id(ticketId);
        List<Comment> comments = commentRepository.findByRoom_Id(roomId);

        List<CommentDTO> commentDTOS = comments
                .stream()
                .map(comment -> CommentDTO.builder()
                        .message(comment.getMessage())
                        .senderId(comment.getSender().getId())
                        .build()).toList();

        return AllChatRespDTO.builder()
                .comment(commentDTOS)
                .roomId(roomId)
                .build();

//        return comments.stream()
//                .map(comment -> AllChatRespDTO.builder()
//                        .comment(commentDTOS)
//                        .tickedId(comment.getTicket().getId())
//                        .roomId(comment.getRoom().getId())
//                        .build()).toList();
    }
}
