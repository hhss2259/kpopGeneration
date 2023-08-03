package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.CommentViewDto;
import kpop.kpopGeneration.dto.PageCustomDto;
import kpop.kpopGeneration.dto.PostDetailDto;
import kpop.kpopGeneration.entity.Comment;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CommentForCommentTest {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Test
    @DisplayName("댓글 inCommentForComment Test")
    void commentTest(){
        //given
        Member member = new Member("aaaa", "1111", "member1");
        Member savedMember = memberRepository.save(member);

        Post post = new Post("테스트 포스트", "테스트하기", savedMember, Category.MUSIC);
        Post savedPost = boardRepository.save(post);


        //when
        Comment commentForPost = new Comment("댓글 테스트입니다", savedPost, null, savedMember);
        Comment savedCommentForPost = commentRepository.save(commentForPost);

        Comment commentForComment = new Comment("대댓글 테스트입니다", savedPost, savedCommentForPost, savedMember);
        Comment savedCommentForComment = commentRepository.save(commentForComment);

        //then
        assertFalse(commentRepository.getIsCommentForComment(savedCommentForPost.getId()));
        assertTrue(commentRepository.getIsCommentForComment(savedCommentForComment.getId()));
    }

    @Test
    @DisplayName("댓글(forPost) 리스트 출력하기")
    void commentForPost(){
        //given
        Member member = new Member("aaaa", "1111", "member1");
        Member savedMember = memberRepository.save(member);

        Post post = new Post("테스트 포스트", "테스트하기", savedMember, Category.MUSIC);
        Post savedPost = boardRepository.save(post);


        for(int i = 0 ; i< 10; i++){
            commentRepository.save(new Comment("댓글", savedPost, null, savedMember));
        }

        //then
        Page<CommentViewDto> commentListByPost = commentRepository.findCommentListByPost(savedPost.getId(), PageRequest.of(0, 10));
        List<CommentViewDto> content = commentListByPost.getContent();
        for (int i = 0; i < content.size(); i++) {
            CommentViewDto commentViewDto = content.get(i);
            assertEquals(0 , commentViewDto.getDepth(), ()-> "대댓글이 아닌 댓글의 깊이는 0입니다");
            assertNotEquals(0, commentViewDto.getPostId(), ()-> "모든 댓글은 post를 가지고 있어야 합니다");
            assertNull(commentViewDto.getParentCommentId(),()-> "대댓글이 아닌 댓글은 부모 댓글을 가지고 있지 않습니다");
        }

    }


    @Test
    @DisplayName("대댓글(forComment) 리스트 출력하기")
    void commentTest2(){
        //given
        Member member = new Member("aaaa", "1111", "member1");
        Member savedMember = memberRepository.save(member);

        Post post = new Post("테스트 포스트", "테스트하기", savedMember, Category.MUSIC);
        Post savedPost = boardRepository.save(post);


        Comment[] commentArray = new Comment[11];
        for(int i = 0 ; i< 10; i++){
            // 첫번째 댓글은 대댓글이 아닌 댓글이다
            if(i == 0){
                Comment comment = new Comment("댓글", savedPost, null, savedMember);
                Comment savedComment1 = commentRepository.save(comment);
                commentArray[0] = savedComment1;
            }
            //앞에 저장한 댓글을 부모 댓글로 삼는다.
            Comment newComment = commentRepository.save(new Comment("댓글", savedPost, commentArray[i], savedMember));
            commentArray[i+1] = newComment;
        }

        //then
        Page<CommentViewDto> commentListByPost = commentRepository.findCommentListByPost(savedPost.getId(), PageRequest.of(0, 10));
        List<CommentViewDto> content = commentListByPost.getContent();

        Long parentComment = null;
        Integer parentDepth = 0;
        for (int i = 0; i < content.size(); i++) {
            CommentViewDto commentViewDto = content.get(i);
            assertEquals(parentDepth, commentViewDto.getDepth());
            parentDepth = commentViewDto.getDepth()+1;
            assertNotEquals(0, commentViewDto.getPostId());

            if(i == 0){
                assertNull(commentViewDto.getParentCommentId());
                parentComment = commentViewDto.getCommentId();
            }else{
                assertEquals(parentComment, commentViewDto.getParentCommentId());
                parentComment = commentViewDto.getCommentId();

            }
        }

    }

}
