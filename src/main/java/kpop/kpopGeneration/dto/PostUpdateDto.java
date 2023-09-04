package kpop.kpopGeneration.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostUpdateDto {

    Long id;
    String username;
    String title;
    String body;
    Category category;

    public PostUpdateDto(Long id, String username, String title, String body, Category category) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.body = body;
        this.category = category;
    }
}
