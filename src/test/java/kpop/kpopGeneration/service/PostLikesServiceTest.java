package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PostLikesViewDto;
import kpop.kpopGeneration.dto.PostSaveDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.entity.QMember;
import kpop.kpopGeneration.exception.DuplicateException;
import kpop.kpopGeneration.exception.NotExistedPostLikes;
import kpop.kpopGeneration.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostLikesServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PostService postService;

    @Autowired
    PostLikesService postLikesService;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("PostLikes 조회해오기")
    void getPostLikes(){
        //given
        for (int i = 0; i < 6; i++) {
            memberService.save(new Member("aaaa" + i, "1111" + i, "member" + i, "1111@naver.com"+i));
        }
        Long savePostId = postService.savePost(new PostSaveDto("테스트", "테슽트 포스트", Category.REVIEW), "aaaa" + 0);

        //when
        postLikesService.increaseLikes(savePostId, "aaaa"+0);
        postLikesService.increaseLikes(savePostId, "aaaa"+1);
        postLikesService.increaseLikes(savePostId, "aaaa"+2);
        postLikesService.increaseLikes(savePostId, "aaaa"+3);
        postLikesService.increaseLikes(savePostId, "aaaa"+4);

        //then
        PostLikesViewDto likes = postLikesService.getPostLikes(savePostId, "aaaa" + 0);
        assertEquals(5, likes.getLikes());
        assertEquals(true, likes.getIsLiked());

        PostLikesViewDto notLike = postLikesService.getPostLikes(savePostId, "aaaa" + 5);
        assertEquals(5, notLike.getLikes());
        assertEquals(false, notLike.getIsLiked());

        Post post = postRepository.findPostById(savePostId).get();
        assertEquals(likes.getLikes(), post.getLikes());
        assertEquals(notLike.getLikes(), post.getLikes());
    }

    @Test
    @DisplayName("포스트 좋아요 누르기")
    void increaseLikes(){
        //given
        memberService.save(new Member("aaaa", "1111", "member1", "11@naver.com"));
        memberService.save(new Member("bbbb", "2222", "member2", "22@naver.com"));
        Long savePostId = postService.savePost(new PostSaveDto("테스트", "테스트 포스트", Category.REVIEW), "aaaa");

        //when
        postLikesService.increaseLikes(savePostId, "aaaa");
        PostLikesViewDto likes = postLikesService.getPostLikes(savePostId, "aaaa");

        //then
        assertEquals(1, likes.getLikes());
        assertEquals(true, likes.getIsLiked());
        assertThrows(DuplicateException.class, ()->postLikesService.increaseLikes(savePostId, "aaaa"));

        PostLikesViewDto notLike = postLikesService.getPostLikes(savePostId, "bbbb");
        assertEquals(1, notLike.getLikes());
        assertEquals(false, notLike.getIsLiked());
 }

    @Test
    @DisplayName("포스트 좋아요 취소하기")
    void decreaseLikes(){
        //given
        memberService.save(new Member("aaaa", "1111", "member1", "11@naver.com"));
        memberService.save(new Member("bbbb", "2222", "member2", "22@naver.com"));
        Long savePostId = postService.savePost(new PostSaveDto("테스트", "테스트 포스트", Category.REVIEW), "aaaa");

        //when
        postLikesService.increaseLikes(savePostId, "aaaa");

        //then
        assertThrows(NotExistedPostLikes.class, ()->postLikesService.decreaseLikes(savePostId, "bbbb"));

        PostLikesViewDto postLikesViewDto = postLikesService.decreaseLikes(savePostId, "aaaa");
        assertEquals(0, postLikesViewDto.getLikes());
        assertEquals(false, postLikesViewDto.getIsLiked());

        Post post = postRepository.findPostById(savePostId).get();
        assertEquals(0, post.getLikes());
    }
}