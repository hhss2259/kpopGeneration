package kpop.kpopGeneration.controller.api;

import kpop.kpopGeneration.dto.DefaultResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class JoinApiController {

    @PostMapping("/api/checkUsername")
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


    @PostMapping("/api/authenticateEmail")
    public DefaultResponse<Integer> authenticateEmail(@RequestBody EmailDto dto){
        return DefaultResponse.res(1, "성공", 20);
    }
    @Data
    static class EmailDto{
        String email;
    }
}
