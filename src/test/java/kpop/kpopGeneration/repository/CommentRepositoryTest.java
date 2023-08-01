package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.entity.Comment;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;


    @Test
    @DisplayName("Comment 엔티티 생성 및 DB 연동 확인")
    void createComment(){
        //given
        Member member = new Member("aaaa", "1111", "member1");
        Member savedMember = memberRepository.save(member);

        Post post = new Post("테스트 포스트", "테스트하기", savedMember, Category.MUSIC);
        Post savedPost = boardRepository.save(post);


        //when
        Comment commentForPost = new Comment(savedPost, null, savedMember);
        Comment savedCommentForPost = commentRepository.save(commentForPost);

        Comment commentForComment = new Comment(savedPost, savedCommentForPost, savedMember);
        Comment savedCommentForComment = commentRepository.save(commentForComment);


        //then
        assertNull(savedCommentForPost.getComment());
        assertFalse(savedCommentForPost.isCommentForComment());
        assertNotEquals(0, savedCommentForPost.getId());
        assertTrue(savedCommentForPost.getPost().getTitle().equals("테스트 포스트"));
        assertTrue(savedCommentForPost.getMember().getNickName().equals("member1"));

        assertNotNull(savedCommentForComment.getComment());
        assertTrue(savedCommentForComment.isCommentForComment());
        assertNotEquals(0, savedCommentForComment.getId());
        assertTrue(savedCommentForComment.getPost().getTitle().equals("테스트 포스트"));
        assertTrue(savedCommentForComment.getMember().getNickName().equals("member1"));
    }
}