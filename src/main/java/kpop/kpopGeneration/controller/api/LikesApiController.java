package kpop.kpopGeneration.controller.api;

import kpop.kpopGeneration.config.SpringSecurityMethod;
import kpop.kpopGeneration.dto.DefaultResponse;
import kpop.kpopGeneration.dto.LikesViewDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.oauth2.Oauth2Member;
import kpop.kpopGeneration.service.CommentLikesService;
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
    private final CommentLikesService commentLikesService;

    /**
     * 게시물에 좋아요 상태를 조회힌다
     * 로그인하지 않았을 경우, 단순히 좋아요 수를 조회하고
     * 로그인한 상태인 경우, 좋아요 갯수와 해당 유저가 좋아요를 눌러는지 여부를 조회한다
     */
    @GetMapping("/api/post/likes")
    public DefaultResponse getLikes(@RequestParam String post){
        Long id = Long.parseLong(post);
        LikesViewDto postLikes = null;
        if(SpringSecurityMethod.checkLogin()) {
            postLikes = postLikesService.getPostLikes(id, SpringSecurityMethod.getUsername()); // 좋아요 갯수 + 해당 유저가 좋아요를 눌렀는지 여부
        }else{
            postLikes = postLikesService.getPostLikes(id);
        }
        return  DefaultResponse.res(20001, "성공", postLikes);
    }

    /**
     *  게시물의 좋아요 버튼을 눌렀을 경우 처리를 담당한다(토글 형식)
     *  아직 좋아요를 누르지 않았을 경우, 좋아요 수를 증가시키고 해당 좋아요 정보를 DB에 저장한다
     *  이미 좋아요를 누른 경우, 좋아요를 취소하고, 좋아요 정보를 DB에서 삭제한다
     */
    @GetMapping("/api/post/likes/toggle")
    public DefaultResponse toggleLikes(@RequestParam String post){
        Long id = Long.parseLong(post);
        String username = SpringSecurityMethod.getUsername();
        LikesViewDto current = postLikesService.getPostLikes(id, username);
        LikesViewDto changed = null;
        if(current.getIsLiked() == true){ //이미 좋아요를 누른 경우
            changed = postLikesService.decreaseLikes(id, username); //좋아요를 취소한다
        }else{ //아직 좋아요를 누르지 않은 경우
            changed = postLikesService.increaseLikes(id, username); // 좋아요를 저장한다
        }
        return DefaultResponse.res(20001, "성공", changed);
    }

    /**
     * 댓글의 좋아요 상태를 조회힌다
     * 로그인하지 않았을 경우, 단순히 좋아요 수를 조회하고
     * 로그인한 상태인 경우, 좋아요 갯수와 해당 유저가 좋아요를 눌러는지 여부를 조회한다
     */
    @GetMapping("/api/comment/likes")
    public DefaultResponse getCommentLikes(@RequestParam String comment){
        Long id = Long.parseLong(comment);
        LikesViewDto commentLikes = null;
        if(SpringSecurityMethod.checkLogin()) {
            commentLikes = commentLikesService.getCommentLikes(id, SpringSecurityMethod.getUsername());
        }else{
            commentLikes = commentLikesService.getCommentLikes(id);
        }
        return  DefaultResponse.res(20001, "성공", commentLikes);
    }

    /**
     *  댓글의 좋아요 버튼을 눌렀을 경우 처리를 담당한다 (토글 형식)
     *  아직 좋아요를 누르지 않았을 경우, 좋아요 수를 증가시키고 해당 좋아요 정보를 DB에 저장한다
     *  이미 좋아요를 누른 경우, 좋아요를 취소하고, 좋아요 정보를 DB에서 삭제한다
     */
    @GetMapping("/api/comment/likes/toggle")
    public DefaultResponse likeComments(@RequestParam String comment){
        Long id = Long.parseLong(comment);
        String username = SpringSecurityMethod.getUsername();
        LikesViewDto current = commentLikesService.getCommentLikes(id, username);
        LikesViewDto changed = null;
        if(current.getIsLiked() == true){
            changed = commentLikesService.decreaseLikes(id, username);
        }else{
            changed = commentLikesService.increaseLikes(id, username);
        }
        return DefaultResponse.res(20001, "성공", changed);
    }



}
