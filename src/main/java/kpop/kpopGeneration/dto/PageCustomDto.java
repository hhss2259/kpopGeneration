package kpop.kpopGeneration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageCustomDto<T> {
    public List<T> content;
    public Integer size;
    public Integer  numberOfElements;
    public Long totalElements;
    public Integer totalPages;
    public Boolean hasNext;
    public Boolean hasPrevious;
    public Boolean isFirst;
    public Boolean isLast;
    public Integer current;

    public Pageable nextPageable;
    public Pageable previousPageable;
    public Category category;

}
