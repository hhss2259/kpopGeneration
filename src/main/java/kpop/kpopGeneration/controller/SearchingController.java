package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PageCustomDto;
import kpop.kpopGeneration.dto.PostTitleViewDto;
import kpop.kpopGeneration.dto.SearchingDto;
import kpop.kpopGeneration.service.SearchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
@Slf4j
public class SearchingController {

    private final SearchingService searchingService;
    @PostMapping("/searching")
    public String search(@ModelAttribute SearchingDto dto, @PageableDefault Pageable pageable){
        PageCustomDto<PostTitleViewDto> search = searchingService.search(Category.ALL, dto.getOption(), dto.getKeyword(), pageable);
        return "redirect:/";
    }
}
