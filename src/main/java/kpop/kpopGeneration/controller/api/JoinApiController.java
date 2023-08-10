package kpop.kpopGeneration.controller.api;

import kpop.kpopGeneration.dto.DefaultResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class JoinApiController {

    @PostMapping("/api/username")
    public DefaultResponse<String> checkUsername(@RequestBody UsernameDto dto){
        if(dto.getUsername().equals("hhss2259")){
            return DefaultResponse.res(2, "실패", "Bad");
        }
        return DefaultResponse.res(1, "성공", "good");
    }
    @Data
    static class UsernameDto{
        String username;
    }

    @PostMapping("/api/nickname")
    public DefaultResponse<String> checkNickname(@RequestBody NicknameDto dto){
        System.out.println("dto.getNickname() = " + dto.getNickname());
        return DefaultResponse.res(20001, "성공", "good");
    }
    @Data
    static class NicknameDto{
        String nickname;
    }

}
