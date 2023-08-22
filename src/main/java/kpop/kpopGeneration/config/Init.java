package kpop.kpopGeneration.config;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.CommentSaveDto;
import kpop.kpopGeneration.dto.PostSaveDto;
import kpop.kpopGeneration.dto.PostSaveViewDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.repository.CommentRepository;
import kpop.kpopGeneration.service.PostService;
import kpop.kpopGeneration.service.CommentService;
import kpop.kpopGeneration.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@RequiredArgsConstructor
public class Init {

    private final MemberService memberService ;
    private final PostService postService;
    private final CommentService commentService;

    private final CommentRepository commentRepository;
    @PostConstruct
    public void init() {

        Member member1 = new Member("xxxx", "1111", "앤써1");
        Member member2 = new Member("yyyy", "1111", "앤써2");
        Member member3 = new Member("zzzz", "1111", "앤써3");
        Member member4 = new Member("hhss2259", "11111111", "앤써4");

        memberService.save(member1);
        memberService.save(member2);
        memberService.save(member3);
        memberService.save(member4);



//        Long savePost = postService.savePost(new PostSaveDto("테스트 포스트", "포스트 테스트하기 ", Category.NORMAL), member1.getUsername());
//        Long aa = commentService.saveComment(new CommentSaveDto(savePost, "aa", null, false), member2.getUsername());
//        Long aa1 = commentService.saveComment(new CommentSaveDto(savePost, "aa", aa, true), member2.getUsername());
//        Long aa3 = commentService.saveComment(new CommentSaveDto(savePost, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", aa1, true), member3.getUsername());
//        Long aa2 = commentService.saveComment(new CommentSaveDto(savePost, "aa", aa3, true), member4.getUsername());
//        Long aa4 = commentService.saveComment(new CommentSaveDto(savePost, "aa", null, false), member3.getUsername());
//        Long aa5 = commentService.saveComment(new CommentSaveDto(savePost, "aa", null, false), member1.getUsername());
//        Long aa6 = commentService.saveComment(new CommentSaveDto(savePost, "aa", aa5, true), member4.getUsername());
//        Long saveComment = commentService.saveComment(new CommentSaveDto(savePost, "aa", null, false), member1.getUsername());
//

        for (int i = 0; i < 111; i++) {
            postService.savePost(new PostSaveDto("키워드" + i, "포스트", Category.NEWS),member1.getUsername());
        }

//        Long savePost = postService.savePost(new PostSaveDto("키워드", "포스트", Category.MUSIC), member1.getUsername());
//        for(int i = 1; i<111 ;i++){
//            commentService.saveComment(new CommentSaveDto(savePost, "댓글" + i, null, false), member1.getUsername());
//        }


    }


}
