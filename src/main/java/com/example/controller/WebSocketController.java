package com.example.controller;

import com.example.TestSsh1;
import com.example.domain.HelloMessage;
import com.example.domain.Response;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

/**
 * Created by Xue on 02/17/17.
 */
@Controller
public class WebSocketController {

    @RequestMapping("/ws_cmd")
    public String webCmd() {
        return "ws_command";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/output")
    public Response printOut(HelloMessage message) throws Exception {
        Thread.sleep(100);
        String output = TestSsh1.sshService(Arrays.asList(message.getName().split(",")));
        return new Response(output);
    }
}
