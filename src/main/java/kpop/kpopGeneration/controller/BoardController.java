package kpop.kpopGeneration.controller;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PageCustomDto;
import kpop.kpopGeneration.dto.PostTitleViewDto;
import kpop.kpopGeneration.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;
    @GetMapping("/board")
    public String findPostListByCategory(@RequestParam(required = false) String requestCategory,
                                         @PageableDefault(size = 10, page = 0) Pageable pageable,
                                         Model model) {
        Category category = null;
        if(requestCategory == null){
            category = Category.ALL;
        }else{
            category = Category.valueOf(requestCategory);
        }

        PageCustomDto<PostTitleViewDto> postListByCategory = boardService.findPostListByCategory(category, pageable);
        model.addAttribute("pageDto", postListByCategory);
        return "/board/board";
    }

}
