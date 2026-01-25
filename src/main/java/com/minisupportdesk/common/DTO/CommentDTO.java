package com.minisupportdesk.common.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private String message;

    private Long senderId;

}
