package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.config.SpringSecurityMethod;
import kpop.kpopGeneration.dto.*;
import kpop.kpopGeneration.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class NewsController {
    private final PostService postService;



    /**
     * 작성한 포스트를 저장한다
     * 또한 이미 작성된 포스트일 경우 포스트를 수정한다.
     */
    @PostMapping("/news")
    public String savePost(@ModelAttribute PostSaveViewDto postSaveViewDto){
        PostSaveDto postSaveDto = new PostSaveDto(postSaveViewDto.getTitle(), postSaveViewDto.getBody(), Category.valueOf(postSaveViewDto.getCategory()));
        // 포스트 수정
        if(postSaveViewDto.getId() != null){
            postService.updatePost(postSaveViewDto.getId(), postSaveDto);
        }else{ //포스트 새로 작성
            postService.savePost(postSaveDto, SpringSecurityMethod.getUsername());
        }
        return "redirect:/news/list";
    }


    /**
     * 포스트들의 리스트들을 조회해와서 제공한다
     */
    @GetMapping("/news/list")
    public String findPostListByCategory(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                         Model model) {
        // 무조건 News 카테고리를 가지고 온다.
        PageCustomDto<PostTitleViewDto> postListByCategory = postService.findPostListByCategory(Category.NEWS, pageable);
        model.addAttribute("postList", postListByCategory);
        return "newsList";
    }

    /**
     * 포스트 자세히 보기 기능 : 포스트 정보 + 댓글 정보 + 작성자의 최근글 정보
     */
    @GetMapping("/news/detail")
    public String postDetail(@RequestParam String id, Model model, @PageableDefault(page=0, size = 20) Pageable commentPageable){
        Long postId = Long.parseLong(id);
        postService.increaseViews(postId);
        PostDetailDto postById = postService.findPostById(postId, commentPageable);
        model.addAttribute("postDetail", postById);
        return "newsDetail";
    }

    /**
     * 포스트 작성 페이지로 이동한다
     */
    @GetMapping("/news")
    public String writePost( @RequestParam(required = false) String post, Model model) {
        // 로그인하지 않은 사람은 접근할 수 없다
        if(SpringSecurityMethod.checkLogin()== false){
            return "redirect:/";
        }

        // 포스트 최초 작성으로, 빈 작성화면을 보여주면 된다
        if(post == null ){
            model.addAttribute("postSaveViewDto", new PostSaveViewDto());
            return "news";
        }

        // (post가 null이 아닌 경우) 해당 글의 원작자가 아닌 사람은 접근할 수 없다
        Long id = Long.parseLong(post);
        PostUpdateDto postUpdateDto = postService.findPostUpdateDto(id);
        if(SpringSecurityMethod.checkAuthority(postUpdateDto.getUsername()) == false){ //잘못된 접근
            return "redirect:/";
        }

        // 포스트 수정 작성임으로, 기존 포스트 정보를 보여주어야 한다.
        PostSaveViewDto postSaveViewDto = new PostSaveViewDto(postUpdateDto.getId(), postUpdateDto.getTitle(), postUpdateDto.getBody(), postUpdateDto.getCategory());
        model.addAttribute("postSaveViewDto", postSaveViewDto);
        return "news";
    }

    /**
     * 포스트 삭제하기
     */
    @GetMapping("/news/delete")
    public String deletePost(@RequestParam String post){
        long id = Long.parseLong(post);
        Long aLong = postService.deletePost(id, SpringSecurityMethod.getUsername());
        return "redirect:/news/list";
    }



}
