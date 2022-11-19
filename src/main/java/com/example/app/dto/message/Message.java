package com.example.app.dto.message;


import com.example.app.models.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private Long productId;

    private String value;

    private StatusEnum status;

    private Long userId;

    private String name;

    private LocalDateTime time;

    private String username;

    public Message(Long productId, String value, StatusEnum status, Long userId, String name, LocalDateTime time) {
        this.productId = productId;
        this.value = value;
        this.status = status;
        this.userId = userId;
        this.name = name;
        this.time = time;
    }
}
