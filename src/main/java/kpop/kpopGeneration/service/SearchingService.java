package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PageCustomDto;
import kpop.kpopGeneration.dto.PostTitleViewDto;
import org.springframework.data.domain.Pageable;

public interface SearchingService {
    public PageCustomDto<PostTitleViewDto> search(Category category, String option, String keyword, Pageable pageable);
}
