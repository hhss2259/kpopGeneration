package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.service.LoginService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginPage(@ModelAttribute MemberDto memberDto){

        return "/member/login";
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class MemberDto{
        String username;
        String password;
    }

    
    @GetMapping("/home")
    public String getHome(){
        log.info("로그인 완료");
        return "/home";
    }


}
