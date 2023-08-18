package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class PureCommentViewDto {

    Long commentId;
    String nickname;
    Long memberId;
    String textBody;
    Long likes;
    Long postId;
    Long parentCommentId;
    Boolean isCommentForComment;
    LocalDateTime lastModifiedTime;
    Integer depth;
    String parentNickname;
}
