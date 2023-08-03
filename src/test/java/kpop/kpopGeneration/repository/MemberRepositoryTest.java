package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.repository.MemberRepository;
import net.bytebuddy.asm.Advice;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.AutoConfigureMockWebServiceClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
@Rollback
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("회원가입 확인")
    void join() {

        //given
        Member member1 = new Member("aaaa", "1111", "member1");
        Member member2 = new Member("bbbb", "2222", "member2");
        //when
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        //then
        Assertions.assertNotEquals(0, savedMember1.getId(), "member의 id는 1이어서는 안됩니다");
        Assertions.assertNotEquals(0, savedMember1.getId(), "member의 id는 1이어서는 안됩니다");
    }
    @Test
    @DisplayName("중복된 username 확인")
    void duplicateUsername(){

        //given
        Member member1 = new Member("aaaa", "1111","member1");

        //when
        memberRepository.save(member1);

        //then
        assertEquals(0, memberRepository.findCntByUsername("bbbb"));
        assertEquals(1, memberRepository.findCntByUsername("aaaa"));
    }
    @Test
    @DisplayName("중복된 nickname 확인")
    void duplicateNickname(){

        //given
        Member member1 = new Member("aaaa", "1111","member1");

        //when
        memberRepository.save(member1);

        //then
        assertEquals(0, memberRepository.findCntByNickname("member2"));
        assertEquals(1, memberRepository.findCntByNickname("member1"));
    }


}