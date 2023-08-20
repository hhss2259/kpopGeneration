package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PageCustomDto;
import kpop.kpopGeneration.dto.PostTitleViewDto;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.repository.SearchingRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.annotation.XmlAttachmentRef;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SearchingServiceImpl implements SearchingService {
    private final SearchingRepositoryImpl searchRepository;
    /**
     * 검색을 실행한다
     * @param category 카테고리 정보 : ALL, MUSIC ,REVIEW, CERTIFICATION, NORMAL
     * @param option 검색 대상 설정 : content(제목 + 내용) or nickname(작성자 닉네임)
     * @param keyword 검색 키워드 : null이어서는 안된다
     * @param pageable 검색 페이지
     * @return
     */
    public PageCustomDto<PostTitleViewDto> search(Category category, String option, String keyword, Pageable pageable) {
        Page<Post> postList = null;
        
        //검색 대상 설정
        if (option.equals("content")) {
            postList = searchRepository.searchByContent(category, keyword, pageable);
        }
        if (option.equals("nickname")) {
            postList = searchRepository.searchByNickname(category, keyword, pageable);
        }

        // 포스트 엔티티를 DTO로 변환한다
        Page<PostTitleViewDto> postTitleList = postList.map(
                post -> new PostTitleViewDto(
                        post.getId(), post.getCategory(), post.getTitle(),
                        post.getMember().getNickName(), post.getLastModifiedTime(),
                        post.getLikes(), post.getCommentCnt()
                )
        );

        // Page를 PageCustomDTO로 변환
        PageCustomDto<PostTitleViewDto> postViewDto = getPageCustom(postTitleList);
        postViewDto.setCurrent((int)(pageable.getPageNumber()+1));
        postViewDto.setCategory(category);

        return postViewDto;
    }
    private <T> PageCustomDto<T> getPageCustom(Page<T> list){
        PageCustomDto<T> dto = new PageCustomDto<>();

        dto.setContent(list.getContent());
        dto.setSize(list.getSize());
        dto.setNumberOfElements(list.getNumberOfElements());
        dto.setTotalElements(list.getTotalElements());
        dto.setTotalPages(list.getTotalPages());
        dto.setHasNext(list.hasNext());
        dto.setHasPrevious(list.hasPrevious());
        dto.setIsFirst(list.isFirst());
        dto.setIsLast(list.isLast());
        dto.setNextPageable(list.nextPageable());
        dto.setPreviousPageable(list.previousPageable());
        return dto;
    }
}
