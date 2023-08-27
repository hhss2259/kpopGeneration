package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.config.LoginInfo;
import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.MemberViewDto;
import kpop.kpopGeneration.dto.PageCustomDto;
import kpop.kpopGeneration.dto.PostTitleViewDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.oauth2.Oauth2Member;
import kpop.kpopGeneration.security.service.MemberContext;
import kpop.kpopGeneration.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {
    /**
     * 메인 페이지
     */

    private final PostService postService;
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

        PageCustomDto<PostTitleViewDto> newsViewList
                = postService.findNewsListByCategory(Category.NEWS, PageRequest.of(0, 11));

        List<PostTitleViewDto> content = newsViewList.getContent();
        List<PostTitleViewDto> bigCard = new ArrayList<>();
        List<PostTitleViewDto> smallCard = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            bigCard.add(content.get(i));
        }
        for (int i = 2; i < content.size(); i++) {
            smallCard.add(content.get(i));
        }
        model.addAttribute("big", bigCard);
        model.addAttribute("small", smallCard);
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

    @GetMapping("/denied")
    public String denied(
                        Model model){
//        model.addAttribute("errorMessage", errorMessage);
//        model.addAttribute("referer", referer );
        return "denied";
    }

}
