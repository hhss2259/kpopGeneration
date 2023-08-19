package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.config.LoginInfo;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.oauth2.Oauth2Member;
import kpop.kpopGeneration.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Security;

@Controller
@Slf4j
@RequiredArgsConstructor
public class JoinController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String join(Model model){

        return "join";}

    /**
     * 일반회원 가입 시 사용된다.
     *  작성한 폼을 통해 새로운 멤버 객체를 만든 후 DB에 저장한다
     */
    @PostMapping("/join")
    public String complete(@ModelAttribute JoinForm form, RedirectAttributes redirectAttributes){
        boolean correct = form.checkForm();
        if(correct == false){
            return "redirect:/post/list";
        }

        Member newMember = form.makeMember();
        Long save = memberService.save(newMember);

        redirectAttributes.addAttribute("join", LoginInfo.JOIN_SUCCESS);
        return "redirect:/";
    }
    
    /**
     * oauth2를 사용하여 회원 가입 시 사용된다.
     * 가입하려는 유저의 기본 정보는 이미 DB에 저장되어 있는 상태이고,
     * 현재 전달받은 form는 유저의 닉네임 정보만 존재한다.
     * 따라서 유저의 닉네임을 변경해주면 된다.
     */
    @PostMapping("/join/oauth2")
    public String completeOauth2Join(@ModelAttribute JoinForm form, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("join", LoginInfo.JOIN_SUCCESS);
        Oauth2Member principal = (Oauth2Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        principal.getMember().getUsername();

        memberService.updateNickname( principal.getMember().getUsername(), form.getNickname());
        principal.getMember().updateNickname(form.getNickname());

        return "redirect:/";
    }
    @Data
    @ToString
    static class JoinForm{
        private Boolean agree;
        private String username;
        private String password;
        private String passwordSecond;
        private String email;
        private String nickname;
        boolean checkForm(){
            if (this.agree == false || this.username == null || this.password == null || this.email == null || this.nickname == null) {
                return false;
            }
            return true;
        }

        Member makeMember(){
            return new Member(username, password, nickname, email);
        }
    }
}
