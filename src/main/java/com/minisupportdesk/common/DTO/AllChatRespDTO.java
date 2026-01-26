package com.minisupportdesk.common.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllChatRespDTO {

    private List<CommentDTO> comment;

//    private Long roomId;

    private Long tickedId;

}
