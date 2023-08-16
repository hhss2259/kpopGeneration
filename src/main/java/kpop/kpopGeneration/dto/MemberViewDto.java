package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberViewDto {
    Long id;
    String nickname;
    Boolean logined;


    public MemberViewDto(Long id, String nickname, boolean logined){
        this.id = id;
        this.nickname =nickname;
        this.logined = logined;
    }
}
