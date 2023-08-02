package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentSaveDto {

    Long postId;
    String textBody;
    Long parentCommentId;
    Boolean isCommentForComment;

}
