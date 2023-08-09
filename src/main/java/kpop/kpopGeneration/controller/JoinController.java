package kpop.kpopGeneration.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class JoinController {

    @GetMapping("/join")
    public String join(){
        return "join";
    }

    @PostMapping("/join")
    public String complete(JoinForm form, RedirectAttributes redirectAttributes){

        System.out.println("가즈아아아아!!!");
        System.out.println("form.checked = " + form.agree);
        System.out.println("form.checked = " + form.nicknameValue);
        redirectAttributes.addAttribute("join", "success");

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
