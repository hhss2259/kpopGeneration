package kpop.kpopGeneration.controller;

import com.sun.istack.NotNull;
import kpop.kpopGeneration.config.SpringSecurityMethod;
import kpop.kpopGeneration.dto.CommentSaveDto;
import kpop.kpopGeneration.dto.CommentUpdateViewDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.oauth2.Oauth2Member;
import kpop.kpopGeneration.service.CommentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 저장하기
     */
    @PostMapping("/comment")
    public String comment(@ModelAttribute CommentSaveViewDto commentSaveViewDto) {
        CommentSaveDto commentSaveDto = new CommentSaveDto(
                commentSaveViewDto.getPostId(),
                commentSaveViewDto.getTextBody(),
                commentSaveViewDto.getParentCommentId(),
                commentSaveViewDto.getIsCommentForComment());
        if (commentSaveViewDto.getIsCommentForComment() == null) {
            commentSaveDto.setIsCommentForComment(false);
        }
        Long aLong = commentService.saveComment(commentSaveDto, SpringSecurityMethod.getUsername());
        return "redirect:/post/detail?id=" + commentSaveViewDto.getPostId();
    }

    /**
     * 댓글 수정하기
     */
    @PostMapping("/comment/update")
    String updateComment(@ModelAttribute CommentUpdateViewDto commentUpdateViewDto) {
        commentService.updateComment(commentUpdateViewDto);
        return "redirect:/post/detail?id=" + commentUpdateViewDto.getPostId();
    }

    /**
     * 댓글 삭제하기
     */
    @GetMapping("/comment/delete")
    String deleteComment(@RequestParam String comment) {
        Long id = Long.parseLong(comment);
        Long aLong = commentService.deleteComment(id, SpringSecurityMethod.getUsername());
        return "redirect:/post/detail?id="+aLong;
    }

    @Data
    static class CommentSaveViewDto{
        Long postId;
        String textBody;
        Long parentCommentId;
        Boolean isCommentForComment;
    }


}
