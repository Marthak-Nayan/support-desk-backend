package com.minisupportdesk.common.comment.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private String message;

    private Long senderId;

    private LocalDateTime sendTime;

}
