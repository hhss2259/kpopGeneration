package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentPostByMemberDto {

    String title;
    LocalDateTime createdTime;

}
