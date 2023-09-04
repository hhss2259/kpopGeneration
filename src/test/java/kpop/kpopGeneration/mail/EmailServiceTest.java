package kpop.kpopGeneration.mail;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.exception.DuplicateException;
import kpop.kpopGeneration.repository.MemberRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    JavaMailSender javaMailSender;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("이메일 중복 체크")
    void duplicateEmail(){
        //given
        String email = "hhss1111@naver.com";
        EmailService emailService = new EmailService(javaMailSender, memberRepository);
        Member member1 = new Member("aaaa","1111", "member1", email);
        //when
        memberRepository.save(member1);
        //then
        assertThrows(DuplicateException.class, ()-> emailService.checkEmail(email));
        assertDoesNotThrow(()->emailService.checkEmail("hhss2222@naver.com"));
    }
}