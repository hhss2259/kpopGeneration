package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.dto.*;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.oauth2.Oauth2Member;
import kpop.kpopGeneration.service.PostService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
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
                                         @RequestParam(required = false) String errorMessage,
                                         Model model) {
        // 에러 발생 시 에러 정보를 보여준다.
        if (errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        // 카테고리와 페이지 정보를 이용하여 필요한 포스트 리스트들을 서버에서 조회해온다
        Category requestCategory = null;
        if(category == null){
            requestCategory = Category.ALL;
        }else{
            requestCategory = Category.valueOf(category);
        }
        PageCustomDto<PostTitleViewDto> postListByCategory = postService.findPostListByCategory(requestCategory, pageable);
        model.addAttribute("postList", postListByCategory);

        return "postList";
    }

    /**
     * 포스트 자세히 보기 기능 : 포스트 정보 + 댓글 정보 + 작성자의 최근글 정보
     */
    @GetMapping("/post/detail")
    public String postDetail(@RequestParam String id, Model model,
                             @RequestParam(required = false) String errorMessage,
                             @PageableDefault(page=0, size = 20) Pageable commentPageable){
        if (errorMessage != null){
            model.addAttribute("errorMessage", errorMessage);
        }

        Long postId = Long.parseLong(id);
        postService.increaseViews(postId);
        PostDetailDto postById = postService.findPostById(postId, commentPageable);

        model.addAttribute("postDetail", postById);
        return "postDetail";
    }

    /**
     * 포스트 작성 페이지로 이동한다
     */
    @GetMapping("/post")
    public String writePost( @RequestParam(required = false) String post,
                             Model model) {

        // 로그인하지 않은 사람은 접근할 수 없다
        if(checkLogin()== false || post == null){
            return "redirect:/";
        }

        // 해당 글의 원작자가 아닌 사람은 접근할 수 없다
        Long id = Long.parseLong(post);
        PostUpdateDto postUpdateDto = postService.findPostUpdateDto(id);
        if(checkAuthority(postUpdateDto.getUsername()) == false){ //잘못된 접근
            return "redirect:/";
        }

        // 포스트 최초 작성으로, 빈 작성화면을 보여주면 된다
        if(post == null ){
            model.addAttribute("postSaveViewDto", new PostSaveViewDto());
            return "post";
        }

        // 포스트 수정 작성임으로, 기존 포스트 정보를 보여주어야 한다.
        PostSaveViewDto postSaveViewDto = new PostSaveViewDto(postUpdateDto.getTitle(), postUpdateDto.getBody(), postUpdateDto.getCategory());
        model.addAttribute("postSaveViewDto", postSaveViewDto);
        return "post";
    }

    /**
     * 작성한 포스트를 저장한다
     */
    @PostMapping("/post")
    public String savePost(@ModelAttribute PostSaveViewDto postSaveViewDto){
        System.out.println("form.getCategory() = " + postSaveViewDto.getCategory());
        System.out.println("form.getTitle() = " + postSaveViewDto.getTitle());
        System.out.println("form.getTextdata() = " + postSaveViewDto.getBody());

//        PostSaveDto postSaveDto = new PostSaveDto(form.getTitle(), form.getTextdata(), Category.valueOf(form.getCategory()));
//        postService.savePost(postSaveDto, "xxxx");

        return "redirect:/post/list";
    }


    boolean checkLogin(){
        boolean result = false;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof Member || principal instanceof Oauth2Member){
            result = true;
        }
        return result;
    }

    boolean checkAuthority(String username){
        boolean result = false;
        if(checkLogin() == false){
            return false;
        }
        Member member = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof Member){
           member = (Member) principal;
        }else if(principal instanceof Oauth2Member){
            member = ((Oauth2Member) principal).getMember();
        }

        if(member.getUsername().equals(username)){
            result = true;
        }
        return result;
    }
}
