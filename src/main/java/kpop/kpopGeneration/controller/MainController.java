package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.config.LoginInfo;
import kpop.kpopGeneration.dto.MemberViewDto;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {
    /**
     * 메인 페이지
     */
    @GetMapping("/")
    public String main(@RequestParam(required = false) String join,
                       Model model) {
        /**
         * 회원가입에 성공하면 성공 메세지를 보여주기 위해 사용 (회원가입 시 항상 메인페이지로 이동)
        */
        if(join != null && join.equals(LoginInfo.JOIN_SUCCESS)){
            model.addAttribute("join", LoginInfo.JOIN_SUCCESS);
        }
        /**
         *  SecurityContextHolder에서 현재 로그인한 사용자의 정보를 가지고 온다
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof Oauth2Member){
            Oauth2Member oauth2Member = (Oauth2Member) authentication.getPrincipal();
            Member loginMember = oauth2Member.getMember();
            if (loginMember.getNickName() == null ){
                return "oauthJoin";
            }
        }
        return "main";
    }

    @GetMapping("/errorPage")
    public String error(@RequestParam(required = false) String errorMessage,
                        @RequestParam(required = false) String referer,
                        Model model){
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("referer", referer );
        return "error";
    }

    @GetMapping("/news/list")
    public String news(){
        return "newsList";
    }
}
