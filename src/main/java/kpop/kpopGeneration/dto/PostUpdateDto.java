package kpop.kpopGeneration.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostUpdateDto {

    String username;
    String title;
    String body;
    Category category;

    public PostUpdateDto(String username, String title, String body, Category category) {
        this.username = username;
        this.title = title;
        this.body = body;
        this.category = category;
    }
}
