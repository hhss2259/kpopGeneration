package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.oauth2.Oauth2Member;
import kpop.kpopGeneration.security.service.MemberContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
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
                       @RequestParam(required = false) String join,
                       Model model) {
        if (loginError != null ){
            model.addAttribute("loginError", true);
            model.addAttribute("errorMessage", errorMessage);
        }

        if(join !=null && join.equals("success")){
            model.addAttribute("join", "success");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication.getPrincipal() = " + authentication.getPrincipal().getClass());


        if (authentication.getPrincipal() instanceof Member){
            Member loginMember = (Member) authentication.getPrincipal();
            model.addAttribute("memberDto", new MemberDto());
        }

        if (authentication.getPrincipal() instanceof Oauth2Member){
            Oauth2Member loginMember = (Oauth2Member) authentication.getPrincipal();
            if (loginMember.getMember().getNickName() == null){
                model.addAttribute("additional", new MemberDto());
            }
            model.addAttribute("memberDto", new MemberDto());
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
