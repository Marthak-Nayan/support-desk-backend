package com.minisupportdesk.Repository;

import com.minisupportdesk.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //List<Comment> findByTicket_Id(Long ticketId);

    List<Comment> findByRoom_Id(Long roomId);

}
