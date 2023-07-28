package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String loginPage(){
        return "/loginPage";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response){
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String certificatedUser = loginService.login(username, password);


        HttpSession session = request.getSession();
        session.setAttribute("username", username);

        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String getHome(){
        log.info("로그인 완료");
        return "/home";
    }


}
