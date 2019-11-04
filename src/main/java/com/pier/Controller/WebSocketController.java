package com.pier.Controller;

import com.pier.bean.Order;
import com.pier.result.Result;
import com.pier.result.ResultUtil;
import com.pier.service.impl.WebSocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * @author zhongweiwu
 * @date 2019/10/31 20:28
 */
@Controller
@RequestMapping("/socket")
public class WebSocketController {

    @Autowired
    private WebSocketServiceImpl webSocketService;

    @GetMapping("/test")
    public String testSocket(){
        return "test_socket";
    }

    @GetMapping("/msg")
    @ResponseBody
    public void testMsg(){
        webSocketService.setId("1");
        webSocketService.sendInfo("1,haha", "1");
    }

    @RequestMapping("getOnlineCount")
    @ResponseBody
    public int getOnlineCount(){
        return WebSocketServiceImpl.getOnlineCount();
    }
}
