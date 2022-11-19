package com.example.app.controllers.webSockets;


import com.example.app.dto.message.Message;
import com.example.app.models.UserSecurity;
import com.example.app.service.LotWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LotWebSocketController {


    private final SimpMessagingTemplate simpMessagingTemplate;

    private final LotWebSocketService lotWebSocketService;


    @Autowired
    public LotWebSocketController(SimpMessagingTemplate simpMessagingTemplate, LotWebSocketService lotWebSocketService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.lotWebSocketService = lotWebSocketService;
    }

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/sub-lot")
    public Message recMessage(@Payload Message message){
        Message resMessage = lotWebSocketService.changePrice(message);
        simpMessagingTemplate.convertAndSendToUser(message.getProductId().toString(),"/private", resMessage);
        return message;
    }
}

