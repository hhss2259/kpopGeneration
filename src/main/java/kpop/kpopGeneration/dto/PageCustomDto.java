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
    List<T> content;
    Integer size;
    Integer  numberOfElements;
    Long totalElements;
    Integer totalPages;

    Boolean hasNext;
    Boolean hasPrevious;
    Boolean isFirst;
    Boolean isLast;

    Pageable nextPageable;
    Pageable previousPageable;


}
