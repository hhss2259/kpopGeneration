package kpop.kpopGeneration.dto;

import lombok.Data;

@Data
public class CommentUpdateViewDto {
    Long postId;
    String textBody;
    Long commentId;

}
