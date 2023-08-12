package kpop.kpopGeneration.mail;

import kpop.kpopGeneration.dto.DefaultResponse;
import kpop.kpopGeneration.exception.DuplicateException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/api/email")
    public DefaultResponse<String> mail(@RequestBody EmailDto dto) throws Exception{
        String email = dto.getEmail();

        emailService.checkEmail(email);
        String key =  emailService.send(email);
        return DefaultResponse.res(20001, "이메일 발송에 성공했습니다", key);
    }
    @ExceptionHandler(IllegalEmailException.class)
    public DefaultResponse illegalEmailException() {
        return DefaultResponse.res(40002, "이메일을 발송하지 못했습니다.");
    }
    @ExceptionHandler(DuplicateException.class)
    public DefaultResponse duplicateEmail(){
        return DefaultResponse.res(40001, "이미 회원가입한 이메일입니다");
    }
    @Data
    static class EmailDto {
        String email;
    }
}
