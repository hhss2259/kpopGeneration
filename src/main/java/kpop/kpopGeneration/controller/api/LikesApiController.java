package kpop.kpopGeneration.controller.api;

import kpop.kpopGeneration.dto.DefaultResponse;
import kpop.kpopGeneration.dto.PostLikesViewDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.oauth2.Oauth2Member;
import kpop.kpopGeneration.service.PostLikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LikesApiController {

    private final PostLikesService postLikesService;

    @GetMapping("/api/post/likes")
    public DefaultResponse getLikes(@RequestParam String post){
        Long id = Long.parseLong(post);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = null;
        PostLikesViewDto postLikes = null;

        if(principal instanceof Member ) {
            member = (Member) principal;
            postLikes = postLikesService.getPostLikes(id, member.getUsername());
        }else if(principal instanceof Oauth2Member){
            Oauth2Member oauth2Member = (Oauth2Member) principal;
            member = oauth2Member.getMember();
            postLikes = postLikesService.getPostLikes(id, member.getUsername());
        }else{
            postLikes = postLikesService.getPostLikes(id);
        }

        return  DefaultResponse.res(20001, "성공", postLikes);
    }

    @GetMapping("/api/post/likes/toggle")
    public DefaultResponse toggleLikes(@RequestParam String post){
        Long id = Long.parseLong(post);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Member member = null;
        if(principal instanceof  Member){
            member = (Member) principal;
        }else if(principal instanceof  Oauth2Member){
            Oauth2Member oauth2Member = (Oauth2Member) principal;
            member = oauth2Member.getMember();
        }

        PostLikesViewDto current = postLikesService.getPostLikes(id, member.getUsername());
        PostLikesViewDto changed = null;
        if(current.getIsLiked() == true){
            changed = postLikesService.decreaseLikes(id, member.getUsername());
        }else{
            changed = postLikesService.increaseLikes(id, member.getUsername());
        }

        return DefaultResponse.res(20001, "성공", changed);
    }
}
