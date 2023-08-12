package kpop.kpopGeneration.service;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.exception.DuplicateException;
import kpop.kpopGeneration.repository.MemberRepository;
import kpop.kpopGeneration.security.entity.MemberRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("회원 가입 정상 작동확인")
    void save() {
        //given
        Member member1 = new Member("aaaa", "1111", "member1");

        //when
        Long savedId = memberService.save(member1);

        //then
        assertNotEquals(0, savedId);
    }

    @Test
    @DisplayName("회원 가입 시 password 암호화 확인")
    void savePassword() {

        //given
        String username = "aaaa";
        String password = "1111";
        String nickname = "member1";
        Member member1 = new Member(username, password, nickname);

        //when
        Long savedId = memberService.save(member1);
        Member savedMember = memberRepository.findByUsername(username).get();

        //then
        assertEquals(username, savedMember.getUsername());
        assertTrue(passwordEncoder.matches(password, savedMember.getPassword()));
    }


    @Test
    @DisplayName("중복된 username 확인")
    void checkDuplicateUsername() {
        //given
        String username = "aaaa";
        String password = "1111";
        String nickname = "member1";
        Member member1 = new Member(username, password, nickname);

        //when
        memberService.save(member1);

        //then
        assertTrue(memberService.checkDuplicateUsername("bbbb"));
        assertThrows(DuplicateException.class, () -> memberService.checkDuplicateUsername(username));

    }

    @Test
    @DisplayName("중복된 nickname 확인")
    void checkDuplicateNickname() {
        //given
        String username = "aaaa";
        String password = "1111";
        String nickname = "member1";
        Member member1 = new Member(username, password, nickname);

        //when
        memberService.save(member1);

        //then
        assertTrue(memberService.checkDuplicateNickname("member2"));
        assertThrows(DuplicateException.class, () -> memberService.checkDuplicateNickname(nickname));

    }

    @Test
    @DisplayName("이메일 정보 추가")
    void saveMemberWithEmail(){
        //given
        Member member1 = new Member("aaaa", "1111", "member1", "hhss2259@naver.com");

        //when
        memberService.save(member1);

        //then
        Optional<Member> byEmail = memberRepository.findByEmail("hhss2259@naver.com");
        assertTrue(byEmail.isPresent());

        Member member = memberRepository.findByUsername("aaaa").get();
        assertNotNull(member.getEmail());

    }
}