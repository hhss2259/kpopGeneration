package kpop.kpopGeneration.dto;

import kpop.kpopGeneration.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailDto {

    Long id;
    String title;
    String body;
    Category category;
    String nickname;
    Long views;
    Long likes;
    Long commentCnt;
    PageCustomDto<CommentViewDto> commentList;

    LocalDateTime date;

    public PostDetailDto(Post post, PageCustomDto<CommentViewDto> commentList){
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.category = post.getCategory();
        this.nickname = post.getMember().getNickName();;
        this.views = post.getViews();
        this.likes = post.getLikes();
        this.commentCnt = post.getCommentCnt();
        this.commentList = commentList;
        this.date = post.getCreatedTime();
    }
}
