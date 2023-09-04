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

    Boolean managerRole;


    public MemberViewDto(Long id, String nickname, boolean logined){
        this.id = id;
        this.nickname =nickname;
        this.logined = logined;
        this.managerRole =false;
    }

    public void updateManagerRole(Boolean managerRole) {
        this.managerRole = managerRole;
    }
}
