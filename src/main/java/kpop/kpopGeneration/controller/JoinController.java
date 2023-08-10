package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.config.LoginInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class JoinController {

    @GetMapping("/join")
    public String join(){return "join";}

    /**
     * 일반회원 가입 시 사용된다.
     * 정상적으로 가입이 된 후, 메인페이지로 redirect된다
     */
    @PostMapping("/join")
    public String complete(@ModelAttribute JoinForm form, RedirectAttributes redirectAttributes){

        redirectAttributes.addAttribute("join", LoginInfo.JOIN_SUCCESS);
        return "redirect:/";
    }
    /**
     * oauth2를 사용하여 회원 가입 시 사용된다.
     * 정상적으로 가입이 된 후, 메인페이지로 redirect된다
     */
    @PostMapping("/join/oauth2")
    public String completeOauth2Join(@ModelAttribute JoinForm form, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("join", LoginInfo.JOIN_SUCCESS);
        return "redirect:/";
    }

    @Data
    @ToString
    static class JoinForm{
        Boolean agree;
        String username;
        String password;
        String passwordSecond;
        String email;
        String nickname;
        Boolean nicknameValue;
    }
}
