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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@Slf4j
public class SearchingController {

    private final SearchingService searchingService;
    @GetMapping("/searching")
    public String search(@RequestParam(required = false) Category category,  @RequestParam String option, @RequestParam String keyword, @PageableDefault Pageable pageable, Model model){
        PageCustomDto<PostTitleViewDto> result = null;
        if(category == null){
            result = searchingService.search(Category.ALL, option, keyword, pageable);
        }else{
            result = searchingService.search(category, option, keyword, pageable);
        }
        model.addAttribute("postList", result);
        model.addAttribute("option", option);
        model.addAttribute("keyword", keyword);
        return "searchingList";
    }
}
