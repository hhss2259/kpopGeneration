package kpop.kpopGeneration.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostSaveDto {
    Category category;
    String body;
    String title;
    List<String> images;

    public PostSaveDto(String title, String body, Category category ){
        this.title = title;
        this.body = body;
        this.category = category;
    }

    public PostSaveDto(String title, String body, Category category, List<String> images ){
        this.title = title;
        this.body = body;
        this.category = category;
        this.images = images;
    }
}
