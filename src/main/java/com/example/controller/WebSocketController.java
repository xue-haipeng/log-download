package com.example.controller;

import com.example.domain.HelloMessage;
import com.example.utils.JschSshUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Xue on 02/17/17.
 */
@Controller
public class WebSocketController {

    @RequestMapping("/ws_cmd")
    public String webCmd() {
        return "ws_command";
    }

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/output")
    public void printOut(HelloMessage message) throws Exception {
        System.out.println("************** " + message.getCommand() + " **************");
/*        Thread.sleep(100);
        String output = TestSsh1.sshService(Arrays.asList(message.getCommand().split(",")));*/
        List<String> cmd = Arrays.asList(message.getCommand().split(","));

        try {
            JschSshUtil.sshConn("192.168.147.139", "root", "ramily", cmd, simpMessagingTemplate);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JschSshUtil.sshDisconn();
        }
    }
}
