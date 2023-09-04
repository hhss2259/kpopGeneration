package kpop.kpopGeneration.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public
class TestController {
    @GetMapping("/test")
    public String test() {
        return "test/testMain";
    }

    @GetMapping("/testLogin")
    public String login() {
        return "test/testLogin";
    }

    @GetMapping("/test/member")
    public String myPage() {
        return "test/testMyPage";
    }

    @GetMapping("/test/manager")
    public String manager() {
        return "test/testManager";
    }

    @GetMapping("/test/admin")
    public String admin() {
        return "test/testAdmin";
    }

}
