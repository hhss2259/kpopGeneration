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
                       @RequestParam(required = false) String errorMessage,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        if (errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        /**
         * 회원가입에 성공하면 성공 메세지를 보여주기 위해 사용
        */
        if(join != null && join.equals(LoginInfo.JOIN_SUCCESS)){
            model.addAttribute("join", LoginInfo.JOIN_SUCCESS);
        }
        /**
         *  SecurityContextHolder에서 현재 로그인한 사용자의 정보를 가지고 온다
         *  <세 가지 경우의 수>
         *  1. 로그인하지 않았을 경우 => 모델에 아무런 정보를 저장하지 않음으로써 현재 로그인한 상태라가 아니라는 것을 표현
         *  2. 일반 회원가입한 사용자인 경우 => 추가 정보를 받을 필요가 없다
         *  3. SNS 회원가입 사용자인 경우 => 최초 로그인 시 추가 정보(닉네임)을 입력받기 위해  'aouthJoin' 페이지로 이동,
         *      최초 로그인이 아니면 추가 정보를 받을 필요가 없으므로 일반 회원가입 유저와 동등하게 행동함
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

    @GetMapping("/errorRedirect")
    public String error(@RequestParam(required = false) String errorMessage,
                        @RequestParam String referer,
                        RedirectAttributes redirectAttributes){
        /**
         *  errorMessage가 존재하는 경우 = 로그인 시 에러가 발생했을 때
         *  1. 해당 아이디가 존재하지 않을때
         *  2. 비밀번호가 일치하지 않을 때
         *  3. form 안에 존재하는 secret_key의 정보가 일치하지 않을 떄
         */

        redirectAttributes.addAttribute("errorMessage", errorMessage);
        return "redirect:"+referer;
    }

    @GetMapping("/news/list")
    public String news(){
        return "newsList";
    }

    @GetMapping("/topic/list")
    public String topic(){
        return "topicList";
    }

}
