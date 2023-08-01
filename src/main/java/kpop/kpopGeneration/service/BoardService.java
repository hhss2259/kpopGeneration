package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PostDetailDto;
import kpop.kpopGeneration.dto.PostSaveDto;
import kpop.kpopGeneration.dto.PostTitleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 게시판 CRUD 기능을 정의하고 있는 인터페이스입니다.
 */
public interface BoardService {
    /**
     * 게시글 저장
     */
    Long savePost(PostSaveDto postSaveDto, String username);
    /**
     * 게시글 목록
     */
    Page<PostTitleDto> findPostListByCategory(Category category, Pageable pageable);

    /**
     * 게시글 자세히 보기
     */
    PostDetailDto findPostById(Long id);

    /**
     * 게시글 좋아요 누르기
     */

    /**
     * 게시글 수정
     */

    /**
     * 게시글 삭제
     */

}
