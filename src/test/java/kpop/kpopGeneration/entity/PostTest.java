package kpop.kpopGeneration.entity;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.repository.PostRepository;
import kpop.kpopGeneration.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("엔터티 생성 및 DB 연동 확인")
    void savePost() {
        //given
        Member member = new Member("aaaa", "1111", "member1");
        Post post = new Post("테스트", "테스트 메세지", member, Category.NORMAL);

        //when
        memberRepository.save(member);
        Post save = postRepository.save(post);

        //then
        assertNotEquals(0L, save.getId());
        assertNotNull(save.getCreatedTime());
        System.out.println("생성 시간 :" +save.getCreatedTime());
        assertNotNull(save.getLastModifiedTime());
        System.out.println("마지막 수정 시간 : "+save.getLastModifiedTime());
        assertNull(save.getDeletedTime());
        System.out.println("삭제 시간 : "+save.getDeletedTime());
        assertFalse(save.isDeletedTrue());
        System.out.println("삭제 여부 :" +save.isDeletedTrue());

    }
}