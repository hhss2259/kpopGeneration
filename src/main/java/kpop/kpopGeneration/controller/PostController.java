package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.dto.*;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.service.PostService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    /**
     * 포스트들의 리스트들을 조회해와서 제공한다
     * 커뮤니티 게시판의 페이징을 담당한다
     */
    @GetMapping("/post/list")
    public String findPostListByCategory(@RequestParam(required = false) String category,
                                         @PageableDefault(size = 10, page = 0) Pageable pageable,
                                         Model model) {
        Category requestCategory = null;
        if(category == null){
            requestCategory = Category.ALL;
        }else{
            requestCategory = Category.valueOf(category);
        }

        PageCustomDto<PostTitleViewDto> postListByCategory = postService.findPostListByCategory(requestCategory, pageable);
        model.addAttribute("pageDto", postListByCategory);
        return "postList";
    }

    @GetMapping("/post/detail")
    public String postDetail(@RequestParam String id, Model model){
        long postId = Long.parseLong(id);
        PostDetailDto postById = postService.findPostById(postId, PageRequest.of(0, 20));
        model.addAttribute("postDetail", postById);
        return "postDetail";
    }



    /**
     * 포스트 작성 페이지로 이동한다
     */
    @GetMapping("/post")
    public String writePost() {
        return "post";
    }

    /**
     * 작성한 포스트를 저장한다
     */
    @PostMapping("/post")
    public String savePost(@ModelAttribute PostForm form){
        System.out.println("form.getCategory() = " + form.getCategory());
        System.out.println("form.getTitle() = " + form.getTitle());
        System.out.println("form.getTextdata() = " + form.getTextdata());

        PostSaveDto postSaveDto = new PostSaveDto(form.getTitle(), form.getTextdata(), Category.MUSIC);
        postService.savePost(postSaveDto, "xxxx");

        return "redirect:/postList";
    }

    /**
     * 포스트 저장 시 사용하는 DTO
     */
    @Data
    static class PostForm{
        String category;
        String title;
        String textdata;
    }

}
