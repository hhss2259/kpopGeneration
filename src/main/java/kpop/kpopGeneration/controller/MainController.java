package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.service.MemberContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String main(@RequestParam(required = false) String loginError,
                       @RequestParam(required = false) String errorMessage,
                       Model model) {
        if (loginError != null ){
            model.addAttribute("loginError", true);
            model.addAttribute("errorMessage", errorMessage);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof Member){
            Member loginMember = (Member) authentication.getPrincipal();
            model.addAttribute("memberDto", new MemberDto(loginMember.getUsername(), loginMember.getPassword()));
        }
        return "main";

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class MemberDto{
        String username;
        String nickname;
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
