package kpop.kpopGeneration.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@Slf4j
public
class TestController {
    @GetMapping("/test")
    public String test(){
        return "test/testMain";
    }

    @GetMapping("/testLogin")
    public String login(){
        return "test/testLogin";
    }

    @GetMapping("/testMyPage")
    public String myPage(){
        return "test/testMyPage";
    }

    @GetMapping("/testManager")
    public String manager(){
        return "test/testManager";
    }

    @GetMapping("/testAdmin")
    public String admin(){
        return "test/testAdmin";
    }


}
