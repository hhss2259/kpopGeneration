package kpop.kpopGeneration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String main(){
        return "main";
    }

    @GetMapping("/news")
    public String news(){
        return "news";
    }

    @GetMapping("/topic")
    public String topic(){
        return "topic";
    }



}
