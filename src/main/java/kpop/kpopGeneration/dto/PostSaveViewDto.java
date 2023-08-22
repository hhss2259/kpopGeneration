package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveViewDto {

    Long id;
    String title;
    String body;
    String category;

    List<String> images;

    public PostSaveViewDto(Long id, String title, String body, Category category) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.category = category.name();
    }
}
