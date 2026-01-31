package com.minisupportdesk.common.comment.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomEventDTO {

    private Long roomId;

    private Long userId;

    private String message;

    private String eventType;

}
