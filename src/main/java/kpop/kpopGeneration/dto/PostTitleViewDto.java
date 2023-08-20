package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostTitleViewDto{

    Long postId;
    Category category;
    String title;
    String nickname;
    LocalDateTime date;
    Long likes;
    Long commentCnt;
}
