package kpop.kpopGeneration.exception;

import kpop.kpopGeneration.dto.DefaultResponse;
import org.aspectj.lang.annotation.AdviceName;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler(Duplicate.class)
    public DefaultResponse<Integer> duplicateUsername(){
        return DefaultResponse.res(40001, "중복된 username", 0);
    }
}
