package kpop.kpopGeneration.controller.api;

import kpop.kpopGeneration.dto.DefaultResponse;
import kpop.kpopGeneration.exception.DuplicateException;
import kpop.kpopGeneration.service.MemberService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class JoinApiController {

    private final MemberService memberService;

    @ExceptionHandler(DuplicateException.class)
    public DefaultResponse<Integer> duplicateUsername(){
        return DefaultResponse.res(40001, "중복되었습니다");
    }

    @PostMapping("/api/username")
    public DefaultResponse<String> checkUsername(@RequestBody UsernameDto dto){
        memberService.checkDuplicateUsername(dto.getUsername());
        return DefaultResponse.res(20001, "사용할 수 있습니다");
    }
    @Data
    static class UsernameDto{
        String username;
    }

    @PostMapping("/api/nickname")
    public DefaultResponse<String> checkNickname(@RequestBody NicknameDto dto){
        memberService.checkDuplicateNickname(dto.getNickname());
        return DefaultResponse.res(20001, "사용할 수 있습니다");
    }
    @Data
    static class NicknameDto{
        String nickname;
    }

}
