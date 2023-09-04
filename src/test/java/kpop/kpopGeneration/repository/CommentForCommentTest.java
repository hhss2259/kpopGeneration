package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.CommentViewDto;
import kpop.kpopGeneration.entity.Comment;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CommentForCommentTest {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    private Member saveMember(){
        Member member = new Member("aaaa", "1111", "member1");
        Member save = memberRepository.save(member);
        return save;
    }

    private Post savePost(Member savedMember){
        Post post = new Post("테스트 포스트", "테스트하기", savedMember, Category.MUSIC);
        Post save = postRepository.save(post);
        return save;
    }
    @Test
    @DisplayName("댓글 inCommentForComment Test")
    void commentTest(){
        //given
        Member savedMember =  saveMember();
        Post savedPost = savePost(savedMember);

        //when
        Comment commentForPost = new Comment("댓글 테스트입니다", savedPost, null, savedMember);
        Comment savedCommentForPost = commentRepository.save(commentForPost);

        Comment commentForComment = new Comment("대댓글 테스트입니다", savedPost, savedCommentForPost, savedMember);
        Comment savedCommentForComment = commentRepository.save(commentForComment);

        //then
        assertFalse(commentRepository.getIsCommentForComment(savedCommentForPost.getId()), ()->"댓글을 isCommentForComment 값이 false이어야 합니다");
        assertTrue(commentRepository.getIsCommentForComment(savedCommentForComment.getId()),()->"대댓글을 isCommentForComment 값이 true이어야 합니다");
    }

    @Test
    @DisplayName("댓글(forPost) 리스트 출력하기")
    void commentForPost(){
        //given
        Member savedMember = saveMember();
        Post savedPost = savePost(savedMember);


        //when
        // 10개의 댓글을 저장한다.
        for(int i = 0 ; i< 10; i++){
            commentRepository.save(new Comment("댓글", savedPost, null, savedMember));
        }

        //then
        Page<CommentViewDto> commentListByPost = commentRepository.findCommentListByPost(savedPost.getId(), PageRequest.of(0, 10));
        List<CommentViewDto> content = commentListByPost.getContent();

        for (int i = 0; i < content.size(); i++) {
            assertEquals(0 , content.get(i).getDepth(), ()-> "일반 댓글의 깊이는 0입니다");
            assertNotNull(content.get(i).getPostId(), ()-> "모든 댓글은 post를 가지고 있어야 합니다");
            assertNull(content.get(i).getParentCommentId(),()-> "일반 댓글은 부모 댓글을 가지고 있지 않습니다");
            assertFalse(content.get(i).getIsCommentForComment(),()-> "일반 댓글의 isCommentForComment는 false 입니다");
        }

    }


    @Test
    @DisplayName("대댓글(forComment) 리스트 출력하기")
    void commentTest2(){
        //given
        Member savedMember = saveMember();
        Post savedPost = savePost(savedMember);

        //when
        Comment[] commentArray = new Comment[11];
        for(int i = 0 ; i< 10; i++){
            // 첫번째 댓글은 대댓글이 아닌 댓글이다
            if(i == 0){
                Comment savedComment1 = commentRepository.save(new Comment("댓글", savedPost, null, savedMember));
                commentArray[i] = savedComment1;
                break;
            }
            //앞에 저장한 댓글을 부모 댓글로 삼는다.
            Comment newComment = commentRepository.save(new Comment("댓글", savedPost, commentArray[i-1], savedMember));
            commentArray[i] = newComment;
        }

        //then
        Page<CommentViewDto> commentListByPost = commentRepository.findCommentListByPost(savedPost.getId(), PageRequest.of(0, 10));
        List<CommentViewDto> content = commentListByPost.getContent();

        Long parentComment = null;
        Integer parentDepth = 0;
        for (int i = 0; i < content.size(); i++) {
            assertEquals(parentDepth, content.get(i).getDepth());
            parentDepth = content.get(i).getDepth()+1;

            if(i == 0){
                assertNull(content.get(i).getParentCommentId());
                parentComment = content.get(i).getCommentId();
            }else{
                assertEquals(parentComment, content.get(i).getParentCommentId());
                parentComment = content.get(i).getCommentId();

            }
        }

    }

}
