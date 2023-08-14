package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostLikesViewDto {
    Long likes;
    Boolean isLiked;
}
