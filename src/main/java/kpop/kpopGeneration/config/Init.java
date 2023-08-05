package kpop.kpopGeneration.config;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PostSaveDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.service.BoardService;
import kpop.kpopGeneration.service.CommentService;
import kpop.kpopGeneration.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.PrePersist;


//@Component
@RequiredArgsConstructor
public class Init {

    private final MemberService memberService ;
    private final BoardService boardService;
    private final CommentService commentService;

    @PostConstruct
    public void init() {

        Member member = new Member("backendDeveloper", "1111", "앤써");
        memberService.save(member);
        for (int i = 0; i < 5; i++) {
            boardService.savePost(new PostSaveDto("테스트 포스트" + i, "포스트 테스트하기 " + i, Category.MUSIC), "backendDeveloper");
        }
    }


}
