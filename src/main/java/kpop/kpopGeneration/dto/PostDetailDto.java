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
    LocalDateTime date;

    PageCustomDto<CommentViewDto> commentList;
    PageCustomDto<RecentPostByMemberDto> recent;


    public PostDetailDto(Post post, PageCustomDto<CommentViewDto> commentList, PageCustomDto<RecentPostByMemberDto> recent){
        //포스트 정보
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.category = post.getCategory();
        this.nickname = post.getMember().getNickName();;
        this.views = post.getViews();
        this.likes = post.getLikes();
        this.commentCnt = post.getCommentCnt();
        this.date = post.getCreatedTime();

        //포스트 댓글 정보
        this.commentList = commentList;


        //작성자의 최신 포스트 글 정보
        this.recent = recent;
    }
}
