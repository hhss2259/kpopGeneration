package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.CommentViewDto;
import kpop.kpopGeneration.dto.PageCustomDto;
import kpop.kpopGeneration.dto.PostDetailDto;
import kpop.kpopGeneration.entity.Comment;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.repository.PostRepository;
import kpop.kpopGeneration.repository.CommentRepository;
import kpop.kpopGeneration.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PostsServiceDetailsTest {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("포스트 자세히 보기 + 댓글 목록 출력")
    void commentTest2(){
        //given
        Member member = new Member("aaaa", "1111", "member1");
        Member savedMember = memberRepository.save(member);

        Post post = new Post("테스트 포스트", "테스트하기", savedMember, Category.MUSIC);
        Post savedPost = postRepository.save(post);


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
        PostDetailDto postById = postService.findPostById(savedPost.getId(), PageRequest.of(0, 10));

        // 포스트 검증하기
        assertEquals(savedPost.getId(), postById.getId());
        assertEquals(savedPost.getTitle(),postById.getTitle());
        assertEquals(savedPost.getBody(), postById.getBody());
        assertEquals(savedPost.getCategory(), postById.getCategory());
        assertEquals(savedMember.getNickName(), postById.getNickname());
        assertEquals(0, postById.getCommentCnt(), ()-> "현재 service 계층이 아닌 repository 계층으로 comment를 저장했으므 " +
                "post의 commentCnt는 증가해서는 안됩니다.");

        //댓글 리스트 검증하기
        PageCustomDto<CommentViewDto> commentList = postById.getCommentList();
        assertEquals(10, commentList.getSize());
        assertEquals(10, commentList.getNumberOfElements());

    }
}
