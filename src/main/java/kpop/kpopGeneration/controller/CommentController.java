package kpop.kpopGeneration.controller;

import com.sun.istack.NotNull;
import kpop.kpopGeneration.dto.CommentSaveDto;
import kpop.kpopGeneration.service.CommentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public String comment(@ModelAttribute CommentSaveViewDto commentSaveViewDto){
        System.out.println("commentSaveViewDto = " + commentSaveViewDto.getPostId());
        System.out.println("commentSaveViewDto = " + commentSaveViewDto.getPostId().getClass());
        System.out.println("commentSaveViewDto = " + commentSaveViewDto.getTextBody());
        System.out.println("commentSaveViewDto = " + commentSaveViewDto.getTextBody().getClass());
        System.out.println("commentSaveViewDto = " + commentSaveViewDto.getParentCommentId());
        System.out.println("commentSaveViewDto = " + commentSaveViewDto.getParentCommentId().getClass());
        System.out.println("commentSaveViewDto = " + commentSaveViewDto.getIsCommentForComment());
        System.out.println("commentSaveViewDto = " + commentSaveViewDto.getIsCommentForComment().getClass());

//        commentService.saveComment(new CommentSaveDto())

        return "redirect:/post?id"+commentSaveViewDto.getPostId();
    }


    @Data
    static class CommentSaveViewDto{
        Long postId;
        String textBody;
        Long parentCommentId;
        Boolean isCommentForComment;
    }
}
