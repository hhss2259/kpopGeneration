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

        Member member1 = new Member("xxxx", "1111", "앤써");
        Member member2 = new Member("yyyy", "1111", "앤써");
        Member member3 = new Member("zzzz", "1111", "앤써");
        Member member4 = new Member("hhss2259", "11111111", "앤써");

        memberService.save(member1);
        memberService.save(member2);
        memberService.save(member3);
        memberService.save(member4);

//
//        for (int i = 0; i < 103; i++) {
//            postService.savePost(new PostSaveDto("테스트 포스트" + i, "포스트 테스트하기 " + i, Category.MUSIC), member1.getUsername());
//        }
//
//        for (int i = 0; i <30; i++) {
//            postService.savePost(new PostSaveDto("테스트 포스트" + i, "포스트 테스트하기 " + i, Category.REVIEW), member1.getUsername());
//        }
//        for (int i = 0; i <12; i++) {
//            postService.savePost(new PostSaveDto("테스트 포스트" + i, "포스트 테스트하기 " + i, Category.CERTIFICATION), member1.getUsername());
//        }
//
//        for (int i = 0; i <44; i++) {
//            postService.savePost(new PostSaveDto("테스트 포스트" + i, "포스트 테스트하기 " + i, Category.NORMAL), member1.getUsername());
//        }

        Long savePost = postService.savePost(new PostSaveDto("테스트 포스트", "포스트 테스트하기 ", Category.NORMAL), member1.getUsername());
        for (int i = 0; i < 7; i++) {
            postService.savePost(new PostSaveDto("테스트 포스트"+i, "포스트 테스트하기 "+i, Category.NORMAL), member1.getUsername());
        }


        Long[] longs = new Long[147];
        Long aLong = commentService.saveComment(new CommentSaveDto(savePost, "테스트 댓글입니다" + 0, null, true), member1.getUsername());
//
//         longs[0] = aLong;
//        for (int i = 1; i < 147; i++) {
//            Long aLong1 = commentService.saveComment(new CommentSaveDto(savePost, "테스트 댓글입니다" + i, longs[i - 1], true), member1.getUsername());
//            longs[i] = aLong1;
//        }


    }


}
