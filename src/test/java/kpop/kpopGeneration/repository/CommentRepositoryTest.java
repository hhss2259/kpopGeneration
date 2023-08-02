package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.CommentViewDto;
import kpop.kpopGeneration.entity.Comment;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;


    @Test
    @DisplayName("Comment 엔티티 생성 및 DB 연동 확인")
    void createComment() {
        //given
        Member member = new Member("aaaa", "1111", "member1");
        Member savedMember = memberRepository.save(member);

        Post post = new Post("테스트 포스트", "테스트하기", savedMember, Category.MUSIC);
        Post savedPost = boardRepository.save(post);


        //when
        Comment commentForPost = new Comment("댓글 테스트입니다" ,savedPost, null, savedMember);
        Comment savedCommentForPost = commentRepository.save(commentForPost);

        Comment commentForComment = new Comment("댓글 테스트입니다" ,savedPost, savedCommentForPost, savedMember);
        Comment savedCommentForComment = commentRepository.save(commentForComment);


        //then
        assertNull(savedCommentForPost.getParentComment());
        assertFalse(savedCommentForPost.getIsCommentForComment());
        assertNotEquals(0, savedCommentForPost.getId());
        assertTrue(savedCommentForPost.getParentPost().getTitle().equals("테스트 포스트"));
        assertTrue(savedCommentForPost.getMember().getNickName().equals("member1"));
        assertEquals(LocalDateTime.now().getYear(), savedCommentForPost.getCreatedTime().getYear());
        assertEquals(LocalDateTime.now().getMonth(), savedCommentForPost.getCreatedTime().getMonth());
        assertEquals(LocalDateTime.now().getHour(), savedCommentForPost.getCreatedTime().getHour());

        assertNotNull(savedCommentForComment.getParentComment());
        assertTrue(savedCommentForComment.getIsCommentForComment());
        assertNotEquals(0, savedCommentForComment.getId());
        assertTrue(savedCommentForComment.getParentPost().getTitle().equals("테스트 포스트"));
        assertTrue(savedCommentForComment.getMember().getNickName().equals("member1"));
        assertEquals(LocalDateTime.now().getYear(), savedCommentForComment.getCreatedTime().getYear());
        assertEquals(LocalDateTime.now().getMonth(), savedCommentForComment.getCreatedTime().getMonth());
        assertEquals(LocalDateTime.now().getHour(), savedCommentForComment.getCreatedTime().getHour());
    }


    @Test
    @DisplayName("댓글 조회하기")
    void commentTest(){
        //given
        Member member = new Member("aaaa", "1111", "member1");
        Member savedMember = memberRepository.save(member);

        Post post = new Post("테스트 포스트", "테스트하기", savedMember, Category.MUSIC);
        Post savedPost = boardRepository.save(post);


        //when
        Comment commentForPost = new Comment("댓글 테스트입니다", savedPost, null, savedMember);
        Comment savedCommentForPost = commentRepository.save(commentForPost);
        assertFalse(savedCommentForPost.getIsCommentForComment());

        Comment commentForComment = new Comment("대댓글 테스트입니다", savedPost, savedCommentForPost, savedMember);
        assertTrue(commentForComment.getIsCommentForComment());
        Comment savedCommentForComment = commentRepository.save(commentForComment);

        assertFalse(commentRepository.getIsCommentForComment(commentForPost.getId()));
        assertTrue(commentRepository.getIsCommentForComment(commentForComment.getId()));


        List<CommentViewDto> commentListByPost = commentRepository.findCommentListByPost(savedPost.getId(), PageRequest.of(0, 10));
        System.out.println("commentListByPost = " + commentListByPost.size());
        for (CommentViewDto comments : commentListByPost) {
            System.out.println("comments.getCommentId() = " + comments.getCommentId());
            System.out.println("comments.isCommentForComment() = " + comments.getIsCommentForComment());
            System.out.println("comments.getParentCommentId = " + comments.getParentCommentId());
        }

    }





}