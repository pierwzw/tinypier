package com.pier.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zhongweiwu
 * @date 2019/10/24 16:27
 */
@Slf4j
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/passwd/{passwd}")
    public ModelAndView passwd(@PathVariable String passwd){
        return new ModelAndView("passwd", "passwd", passwd);
    }
}
