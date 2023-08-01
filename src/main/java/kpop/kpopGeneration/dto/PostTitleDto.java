package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostTitleDto {

    Long orderNumber;
    Category category;
    String title;
    String nickname;
    LocalDateTime date;

}
