package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveViewDto {

    String title;
    String body;
    String category;

    public PostSaveViewDto(String title, String body, Category category) {
        this.title = title;
        this.body = body;
        this.category = category.name();
    }
}
