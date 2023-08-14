package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.*;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostServiceTest {
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
    @DisplayName("포스트 저장 하기")
    void savePost() {
        //given
        Member member = new Member("bbbb", "1111", "member1");
        PostSaveDto postSaveDto = new PostSaveDto("테스트", "테스트용 포스트", Category.NORMAL);

        //when
        Long savedMemberId = memberService.save(member);
        int cntByUsername = memberRepository.findCntByUsername(member.getUsername());

        Member savedMember = memberRepository.findByUsername(member.getUsername()).get(); // savedMember는 영속성 컨텍스트가 관리하고 있다
        assertEquals(0, savedMember.getPostCnt(), "멤버의 최초 포스트 갯수는 0으로 초기화되어야 합니다");

        Long savePostId = postService.savePost(postSaveDto, member.getUsername());
        savedMember = memberRepository.findByUsername(member.getUsername()).get();
        assertEquals(1, savedMember.getPostCnt(), "멤버의 포스트 갯수가 1개 증가해야 합니다");

        Long savePostId2 = postService.savePost(postSaveDto, member.getUsername());
        savedMember = memberRepository.findByUsername(member.getUsername()).get();
        assertEquals(2, savedMember.getPostCnt(), "멤버의 포스트 갯수가 1개 증가해야 합니다");

        //then
        assertNotNull(savedMemberId, "저장된 멤버의 id가 null이면 안 됩니다.");
        assertNotNull(savePostId, "저장된 포스트의 id가 null이면 안 됩니다");
        assertNotNull(savePostId2, "저장된 포스트의 id가 null이면 안 됩니다");
    }

    @Test
    @DisplayName("포스트 리스트 카테고리별 가져오기")
    void postList() {

        Member member = new Member("aaaa", "1111", "member1");
        memberService.save(member);

        for (int i = 0; i < 2; i++) {
            postService.savePost(new PostSaveDto("음악 카테고리" + i, "음악 카테고리 테스트 메세지" + 1, Category.MUSIC), member.getUsername());
        }
        for (int i = 0; i < 3; i++) {
            postService.savePost(new PostSaveDto("리뷰 카테고리" + i, "음악 카테고리 테스트 메세지" + 1, Category.REVIEW), member.getUsername());
        }
        for (int i = 0; i < 4; i++) {
            postService.savePost(new PostSaveDto("인증/후기 카테고리" + i, "인증/후기 카테고리 테스트 메세지" + 1, Category.CERTIFICATION), member.getUsername());
        }
        for (int i = 0; i < 5; i++) {
            postService.savePost(new PostSaveDto("일반 카테고리" + i, "음악 카테고리 테스트 메세지" + 1, Category.NORMAL), member.getUsername());
        }

        PageCustomDto<PostTitleViewDto> musicPost = postService.findPostListByCategory(Category.MUSIC, PageRequest.of(0, 1));
        assertEquals(1, musicPost.getSize());
        assertEquals(2, musicPost.getTotalPages());
        assertEquals(2, musicPost.getTotalElements());
        assertEquals(1, musicPost.getNumberOfElements());
        assertTrue(musicPost.getIsFirst());

        PageCustomDto<PostTitleViewDto> allPost = postService.findPostListByCategory(Category.ALL, PageRequest.of(0, 3));
        assertEquals(3, allPost.getSize());
        assertEquals(5, allPost.getTotalPages());
        assertEquals(14, allPost.getTotalElements());
        assertEquals(3, allPost.getNumberOfElements());
        assertTrue(allPost.getIsFirst());
        for (int i = 0; i < allPost.getNumberOfElements(); i++) {
            assertTrue(allPost.getContent().get(i).getNickname().equals("member1"));

        }
//        for (PostTitleViewDto post : allPost) {
//            assertTrue(post.getNickname().equals("member1"));
//        }
        Member savedMember = memberRepository.findByUsername(member.getUsername()).get();
        assertEquals(14, savedMember.getPostCnt());
    }

    @Test
    @DisplayName("포스트 내용 변경하기")
    void updatePost() {
        //given
        Member member = new Member("aaaa", "1111", "member1", "hhss2259@naver.com");
        Long save = memberService.save(member);
        PostSaveDto postSaveDto= new PostSaveDto("테스트 포스트", "테스트 포스트입니다.", Category.NORMAL);
        Long savePost = postService.savePost(postSaveDto, member.getUsername());

        String title = "업데이트 포스트";
        String body = "업데이트 바디";
        Category category = Category.MUSIC;

        Post originalPost = postRepository.findPostById(savePost).get();

        //when
        postService.updatePost(savePost, new PostSaveDto(title, body, category));
        Post updatedPost = postRepository.findPostById(savePost).get();

        //then
        assertTrue(title.equals(updatedPost.getTitle()));
        assertTrue(body.equals(updatedPost.getBody()));
        assertEquals(category, updatedPost.getCategory());

        assertTrue(originalPost.getCreatedTime().isEqual(updatedPost.getCreatedTime()));
        assertEquals(originalPost.getMember().getNickName(), updatedPost.getMember().getNickName());
        assertEquals(originalPost.getId(), updatedPost.getId());
        assertEquals(originalPost.getLikes(), updatedPost.getLikes());
        assertEquals(originalPost.getViews(), updatedPost.getViews());
    }

    @Test
    @DisplayName("포스트 삭제하기")
    void deletePost() {
        //given
        Member member = new Member("aaaa", "1111", "member1", "hhss2259@naver.com");
        Long save = memberService.save(member);
        PostSaveDto postSaveDto = new PostSaveDto("테스트 포스트", "테스트 포스트입니다.", Category.NORMAL);
        Long savePost = postService.savePost(postSaveDto, member.getUsername());

        Post originalPost = postRepository.findPostById(savePost).get();

        //when
        Member originalMember = memberRepository.findByUsername("aaaa").get();
        assertEquals(1, originalMember.getPostCnt());
        Long deleted = postService.deletePost(savePost, "aaaa");
        Member deletingMember = memberRepository.findByUsername("aaaa").get();
        assertEquals(0, deletingMember.getPostCnt());

        //then
        Post deletedPost = postRepository.findById(savePost).get(); //삭제된 포스트도 동시에 찾는다
        assertEquals(true, deletedPost.getDeletedTrue());
        assertNotNull(deletedPost.getDeletedTime());

        assertTrue(originalPost.getCreatedTime().isEqual(deletedPost.getCreatedTime()));
        assertEquals(originalPost.getMember().getNickName(), deletedPost.getMember().getNickName());
        assertEquals(originalPost.getId(), deletedPost.getId());
        assertEquals(originalPost.getLikes(), deletedPost.getLikes());
        assertEquals(originalPost.getViews(), deletedPost.getViews());
    }

    @Test
    @DisplayName("조회수 증가시키기")
    void increaseViews(){
        //given
        Member member = new Member("aaaa", "1111", "member1", "hhss2259@naver.com");
        Long save = memberService.save(member);
        PostSaveDto postSaveDto = new PostSaveDto("테스트 포스트", "테스트 포스트입니다.", Category.NORMAL);
        Long savePost = postService.savePost(postSaveDto, member.getUsername());

        //when
        for (int i = 0; i < 5; i++) {
            postService.increaseViews(savePost);
        }

        //then
        Post increasedPost = postRepository.findPostById(savePost).get();
        assertEquals(5, increasedPost.getViews());
    }
}