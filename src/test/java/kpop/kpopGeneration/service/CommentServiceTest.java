package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.CommentSaveDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.repository.PostRepository;
import kpop.kpopGeneration.repository.CommentRepository;
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
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 저장하기")
    void saveComment() {
        //given
        Member member = new Member("aaaa", "1111", "member1");
        Member savedMember = memberRepository.save(member);
        assertNotNull(savedMember.getId());

        Post post = new Post("테스트", "테스트하기", savedMember, Category.MUSIC);
        Post savedPost = postRepository.save(post);
        assertNotNull(savedMember.getId());

        //then
        CommentSaveDto commentSaveDto = new CommentSaveDto(savedPost.getId(), "테스트 댓글입니다", null, false);
        Long savedComment1 = commentService.saveComment(commentSaveDto, "aaaa");
        assertNotNull(savedComment1);
        assertNotEquals(1, savedMember.getPostCnt());
        assertEquals(1, savedMember.getCommentCnt());
        assertEquals(1, savedPost.getCommentCnt());

        CommentSaveDto commentSaveDto2 = new CommentSaveDto(savedPost.getId(), "대댓글 테스트입니다", savedComment1, true);
        Long savedComment2 = commentService.saveComment(commentSaveDto2, "aaaa");
        assertNotNull(savedComment2);
        assertNotEquals(savedComment1, savedComment2);
        assertEquals(2, savedMember.getCommentCnt());
        assertEquals(2, savedPost.getCommentCnt());
    }


}