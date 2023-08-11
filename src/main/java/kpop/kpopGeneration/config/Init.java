package kpop.kpopGeneration.config;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PostSaveDto;
import kpop.kpopGeneration.entity.Member;
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

    @PostConstruct
    public void init() {

        Member member1 = new Member("xxxx", "1111", "앤써");
        Member member2 = new Member("yyyy", "1111", "앤써");
        Member member3 = new Member("zzzz", "1111", "앤써");

        memberService.save(member1);
        memberService.save(member2);
        memberService.save(member3);

        for (int i = 0; i < 103; i++) {
            postService.savePost(new PostSaveDto("테스트 포스트" + i, "포스트 테스트하기 " + i, Category.MUSIC), member1.getUsername());
        }

        for (int i = 0; i <30; i++) {
            postService.savePost(new PostSaveDto("테스트 포스트" + i, "포스트 테스트하기 " + i, Category.REVIEW), member1.getUsername());
        }
        for (int i = 0; i <12; i++) {
            postService.savePost(new PostSaveDto("테스트 포스트" + i, "포스트 테스트하기 " + i, Category.CERTIFICATION), member1.getUsername());
        }

        for (int i = 0; i <44; i++) {
            postService.savePost(new PostSaveDto("테스트 포스트" + i, "포스트 테스트하기 " + i, Category.NORMAL), member1.getUsername());
        }
    }


}
