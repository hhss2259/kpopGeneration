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

    // newsList만 가지고 있어야 한다.
    String src;

    public PostTitleViewDto(Long postId, Category category, String title, String nickname, LocalDateTime date, Long likes, Long commentCnt) {
        this.postId = postId;
        this.category = category;
        this.title = title;
        this.nickname = nickname;
        this.date = date;
        this.likes = likes;
        this.commentCnt = commentCnt;
    }
}
