package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PostSaveDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.service.MemberService;
import kpop.kpopGeneration.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("Test")
class SearchingRepositoryImplTest {

    @Autowired
    SearchingRepositoryImpl searchingRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    PostService postService;

    @Test
    @DisplayName("검색 조건 - content")
    void searchOption(){
        //given
        Member member = new Member("aaaa","1111","member1");
        Long save = memberService.save(member);

        PostSaveDto post1 = new PostSaveDto("키워드1", "포스트",  Category.MUSIC);
        PostSaveDto post2 = new PostSaveDto("키워드2", "포스트",  Category.REVIEW);
        PostSaveDto post3 = new PostSaveDto("키워드3", "포스트",  Category.NORMAL);
        PostSaveDto post4 = new PostSaveDto("포스트", "키워드4", Category.MUSIC);
        PostSaveDto post5 = new PostSaveDto("포스트", "키워드5",  Category.MUSIC);
        postService.savePost(post1, member.getUsername());
        postService.savePost(post2, member.getUsername());
        postService.savePost(post3, member.getUsername());
        postService.savePost(post4, member.getUsername());
        postService.savePost(post5, member.getUsername());

        //제목과 본문에 키워드가 들어가 있는 경우
        Page<Post> page = searchingRepository.searchByContent(Category.ALL, "키워드", PageRequest.of(0, 10));
        assertEquals(5, page.getNumberOfElements());

        // 카테고리에 따른 분류
        Page<Post> page2 = searchingRepository.searchByContent(Category.MUSIC, "키워드", PageRequest.of(0, 10));
        assertEquals(3, page2.getNumberOfElements());

        //제목과 본문에 키워드가 없는 경우
        Page<Post> page3 = searchingRepository.searchByContent(Category.ALL, "없음", PageRequest.of(0, 10));
        assertEquals(0, page3.getNumberOfElements());

        // 키워드에 띄어쓰기가 들어가 있는 경우, 각 단어 혹은 글자 별로 쪼개서 검색 : "키 워 드" => "키","워", "드"가 포함된 글
        Page<Post> page4 = searchingRepository.searchByContent(Category.ALL, "키 워 드", PageRequest.of(0, 10));
        assertEquals(5, page4.getNumberOfElements());

        // contains 함수는 해당 단어를 포함하고만 있으면 된다
        Page<Post> page5 = searchingRepository.searchByContent(Category.ALL, "키워", PageRequest.of(0, 10));
        assertEquals(5, page5.getNumberOfElements());

        // contains 함수는 해당 단어를 포함하고만 있으면 된다
        Page<Post> page6 = searchingRepository.searchByContent(Category.ALL, "키 포", PageRequest.of(0, 10));
        assertEquals(5, page6.getNumberOfElements());

        // 띄어쓰기나 공백이 없는 경우 => 전체 단어를 포함하고 있어야 된다. : "키워드입니다!"라는 단어를 포함하고 있어야 한다.
        Page<Post> page7 = searchingRepository.searchByContent(Category.ALL, "키워드입니다!", PageRequest.of(0, 10));
        assertEquals(0, page7.getNumberOfElements());
    }


    @Test
    @DisplayName("검색 조건 - nickname")
    void searchOption2(){
        //given
        Member member = new Member("aaaa","1111","키워드");
        Long save = memberService.save(member);

        PostSaveDto post1 = new PostSaveDto("테스트", "포스트",  Category.MUSIC);
        PostSaveDto post2 = new PostSaveDto("테스트", "포스트",  Category.REVIEW);
        PostSaveDto post3 = new PostSaveDto("테스트", "포스트",  Category.NORMAL);
        PostSaveDto post4 = new PostSaveDto("포스트", "테스트", Category.MUSIC);
        PostSaveDto post5 = new PostSaveDto("포스트", "테스트",  Category.MUSIC);
        postService.savePost(post1, member.getUsername());
        postService.savePost(post2, member.getUsername());
        postService.savePost(post3, member.getUsername());
        postService.savePost(post4, member.getUsername());
        postService.savePost(post5, member.getUsername());

        //when
        Page<Post> page = searchingRepository.searchByNickname(Category.ALL, "키워드", PageRequest.of(0, 10));
        assertEquals(5, page.getNumberOfElements());

        Page<Post> page2 = searchingRepository.searchByNickname(Category.MUSIC, "키워드", PageRequest.of(0, 10));
        assertEquals(3, page2.getNumberOfElements());

        Page<Post> page3 = searchingRepository.searchByNickname(Category.ALL, "없음", PageRequest.of(0, 10));
        assertEquals(0, page3.getNumberOfElements());
    }


}