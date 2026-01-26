package com.minisupportdesk.common.DTO;

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
