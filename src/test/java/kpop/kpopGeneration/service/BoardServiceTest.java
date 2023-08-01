package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PostSaveDto;
import kpop.kpopGeneration.dto.PostTitleDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.repository.BoardRepository;
import kpop.kpopGeneration.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("포스트 저장 하기")
    void savePost() {
        //given
        Member member = new Member("aaaa", "1111", "member1");
        PostSaveDto postSaveDto = new PostSaveDto("테스트", "테스트용 포스트", Category.NORMAL);

        //when
        Long savedMemberId = memberService.save(member);
        Member savedMember = memberRepository.findByUsername(member.getUsername()).get(); // savedMember는 영속성 컨텍스트가 관리하고 있다
        assertEquals(0, savedMember.getPostCnt(), "멤버의 최초 포스트 갯수는 0으로 초기화되어야 합니다");
        System.out.println(savedMember.getPostCnt());

        Long savePostId = boardService.savePost(postSaveDto, member.getUsername());
        savedMember = memberRepository.findByUsername(member.getUsername()).get();
        assertEquals(1, savedMember.getPostCnt(), "멤버의 포스트 갯수가 1개 증가해야 합니다");
        System.out.println(savedMember.getPostCnt());

        Long savePostId2 = boardService.savePost(postSaveDto, member.getUsername());
        savedMember = memberRepository.findByUsername(member.getUsername()).get();
        assertEquals(2, savedMember.getPostCnt(), "멤버의 포스트 갯수가 1개 증가해야 합니다");
        System.out.println(savedMember.getPostCnt());


        //then
        assertNotNull(savedMemberId, "저장된 멤버의 id가 null이면 안 됩니다.");
        assertNotNull(savePostId, "저장된 포스트의 id가 null이면 안 됩니다");
        assertNotNull(savePostId2, "저장된 포스트의 id가 null이면 안 됩니다");

    }

    @Test
    @DisplayName("포스트 리스트 카테고리별 가져오기")
    void postList(){

        Member member = new Member("aaaa", "1111", "member1");
        memberService.save(member);

        for(int i = 0 ; i< 2; i++){
            boardService.savePost(new PostSaveDto("음악 카테고리" + i, "음악 카테고리 테스트 메세지" + 1, Category.MUSIC), member.getUsername());
        }
        for(int i = 0 ; i< 3; i++){
            boardService.savePost(new PostSaveDto("리뷰 카테고리" + i, "음악 카테고리 테스트 메세지" + 1,Category.REVIEW), member.getUsername());
        }
        for(int i = 0 ; i< 4; i++){
            boardService.savePost(new PostSaveDto("인증/후기 카테고리" + i, "인증/후기 카테고리 테스트 메세지" + 1, Category.CERTIFICATION), member.getUsername());
        }
        for(int i = 0 ; i< 5; i++){
            boardService.savePost(new PostSaveDto("일반 카테고리" + i, "음악 카테고리 테스트 메세지" + 1, Category.NORMAL), member.getUsername());
        }


        Page<PostTitleDto> musicPost = boardService.findPostListByCategory(Category.MUSIC, PageRequest.of(0, 1));
        assertEquals(1, musicPost.getSize());
        assertEquals(2, musicPost.getTotalPages());
        assertEquals(2, musicPost.getTotalElements());
        assertEquals(1, musicPost.getNumberOfElements());
        assertTrue(musicPost.isFirst());

        Page<PostTitleDto> allPost = boardService.findPostListByCategory(Category.ALL, PageRequest.of(0, 3));
        assertEquals(3, allPost.getSize());
        assertEquals(5, allPost.getTotalPages());
        assertEquals(14, allPost.getTotalElements());
        assertEquals(3, allPost.getNumberOfElements());
        assertTrue(allPost.isFirst());
        for (PostTitleDto post : allPost) {
            assertTrue(post.getNickname().equals("member1"));
        }
        Member savedMember = memberRepository.findByUsername(member.getUsername()).get();
        assertEquals(14, savedMember.getPostCnt());
    }

}