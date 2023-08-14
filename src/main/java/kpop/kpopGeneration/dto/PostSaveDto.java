package kpop.kpopGeneration.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSaveDto {
    Category category;
    String body;
    String title;


    public PostSaveDto(String title, String body, Category category ){
        this.title = title;
        this.body = body;
        this.category = category;
    }

}
