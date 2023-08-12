package kpop.kpopGeneration.mail;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.exception.DuplicateException;
import kpop.kpopGeneration.repository.MemberRepository;
import kpop.kpopGeneration.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;


    private Map createMessage(String to) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();
        String key = createKey();

        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("이메일 인증 테스트");//제목

        String msg="";
        msg+= "<div style='margin:20px;'>";
        msg+= "<h1> Firesea에 오신 것을 환영합니다!</h1>";
        msg+= "<br>";
        msg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msg+= "<br>";
        msg+= "<p>감사합니다.<p>";
        msg+= "<br>";
        msg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg+= "<div style='font-size:130%'>";
        msg+= "CODE : <strong>";
        msg+= key+"</strong><div><br/> ";
        msg+= "</div>";
        message.setText(msg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("hhss2259@gmail.com","kpopGeneration"));//보내는 사람

        Map map = new HashMap();
        map.put("message", message);
        map.put("key", key);
        return map;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        // 알파벳 대소문자와 숫자로 이루어진 8자리 인증 코드 생성
        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0: //소문자 추가
                    key.append((char) ((rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1: //대문자 추가
                    key.append((char) ((rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2: //숫자 추가
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    public String send(String to)throws Exception {
        Map map = createMessage(to);
        try {
            javaMailSender.send((MimeMessage) map.get("message"));
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalEmailException();
        }
        return (String) map.get("key");
    }

    public void checkEmail(String email) {
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if(byEmail.isPresent()){ // 만약 이메일이 이미 존재하면
            throw new DuplicateException(); // 예외 발생
        }
    }

}
