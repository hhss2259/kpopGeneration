package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BoardRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;

    @Test
    @DisplayName("DB 포스트 갯수 확인")
    void savePost2() {
        //given
        Member member = new Member("aaaa", "1111", "member1");
        Post post1 = new Post("테스트1", "테스트 메세지", member, Category.NORMAL);
        Post post2 = new Post("테스트2", "테스트 메세지", member, Category.NORMAL);
        Post post3 = new Post("테스트3", "테스트 메세지", member, Category.NORMAL);

        //when
        memberRepository.save(member); // 반드시 member 먼저 저장해주어야 한다 => 그래야 FK 값을 저장 가능한다

        boardRepository.save(post1);
        boardRepository.save(post2);
        boardRepository.save(post3);
        int cnt = boardRepository.findCnt();

        //then
        assertEquals(3, cnt);

//        save the transient instance before flushing 에러
//        FK 로 사용되는 컬럼값이 없는 상태에서 데이터를 넣으려다 발생한 에러입니다.
        //https://bcp0109.tistory.com/344
    }


    @Test
    @DisplayName("DB 보드 테이블 조회하기 ")
    void BoardList(){
        //given
        Member member = new Member("aaaa", "1111", "member1");
        memberRepository.save(member);

        int cnt_music = 2;
        int cnt_review = 3;
        int cnt_certification = 4;
        int cnt_normal = 5;

        for(int i = 0 ; i< cnt_music; i++){
            boardRepository.save(new Post("음악 카테고리" + i, "음악 카테고리 테스트 메세지" + 1, member, Category.MUSIC));
        }
        for(int i = 0 ; i< cnt_review; i++){
            boardRepository.save(new Post("리뷰 카테고리" + i, "음악 카테고리 테스트 메세지" + 1, member, Category.REVIEW));
        }
        for(int i = 0 ; i< cnt_certification; i++){
            boardRepository.save(new Post("인증/후기 카테고리" + i, "인증/후기 카테고리 테스트 메세지" + 1, member, Category.CERTIFICATION));
        }
        for(int i = 0 ; i< cnt_normal; i++){
            boardRepository.save(new Post("일반 카테고리" + i, "음악 카테고리 테스트 메세지" + 1, member, Category.NORMAL));
        }

        // DB 총 갯수 조회
        assertEquals(cnt_music+cnt_review+cnt_certification+cnt_normal, boardRepository.findCnt());

        PageRequest pageRequest = PageRequest.of(0, 10);
        // 카테고리별 조회
        Page<Post> musicPost = boardRepository.findPostListByCategory(Category.MUSIC, pageRequest);
        assertEquals(cnt_music, musicPost.getNumberOfElements());
        assertEquals(cnt_music, musicPost.getTotalElements());
        assertEquals(pageRequest.getPageSize(), musicPost.getSize());

        Page<Post> reviewPost = boardRepository.findPostListByCategory(Category.REVIEW, pageRequest);
        assertEquals(3, reviewPost.getNumberOfElements());

        Page<Post> certificationPost = boardRepository.findPostListByCategory(Category.CERTIFICATION, pageRequest);
        assertEquals(4,certificationPost.getNumberOfElements());

        Page<Post> normalPost = boardRepository.findPostListByCategory(Category.NORMAL, pageRequest);
        assertEquals(5, normalPost.getNumberOfElements());


        // 모든(All) 포스트 조회
        PageRequest pageRequest2 = PageRequest.of(4, 3);
        Page<Post> allPost = boardRepository.findALLPostList(pageRequest2);
        assertEquals(5,allPost.getTotalPages());
        assertTrue(allPost.isLast());
        assertEquals(2, allPost.getNumberOfElements());
        assertEquals(3, allPost.getSize());
    }


    @Test
    @Disabled
    @DisplayName("본문 길이 확인")
    void lengthTest(){
        //given
        Member member = new Member("aaaa", "1111", "member1");
        String body = "안녕";
        for (int i = 0; i < 2499; i++) {
            body += body;
        }
        Post post1 = new Post("테스트1", body, member, Category.NORMAL);

        //when
        memberRepository.save(member);
        Post saved = boardRepository.save(post1);


        //then
        Optional<Post> byId = boardRepository.findById(saved.getId());
        assertEquals(body, byId.get().getBody());
    }

    @Test
    @DisplayName("포스트 자세히 보기")
    void postDetail(){

        //given
        Member member = new Member("aaaa", "1111", "member1");
        Member savedMember = memberRepository.save(member);

        Post post = new Post("테스트 포스트", "테스트하기", savedMember,Category.REVIEW);

        //when
        Post savedPost = boardRepository.save(post);
        Optional<Post> postById = boardRepository.findPostById(savedPost.getId());
        Optional<Post> invalidPostById = boardRepository.findPostById(101L);


        //then
        assertTrue(postById.isPresent());
        assertTrue(postById.get().getMember().getNickName().equals("member1"));
        assertEquals("테스트 포스트", postById.get().getTitle());
        assertTrue(invalidPostById.isEmpty());
    }


}