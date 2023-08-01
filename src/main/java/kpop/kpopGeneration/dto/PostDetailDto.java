package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailDto {

    Long id;
    String title;
    String body;
    String category;
    String nickname;
    Long views;
    Long likes;
    Long commentCnt;
}
