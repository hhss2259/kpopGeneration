package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.*;
import org.springframework.data.domain.Pageable;

/**
 * 게시판 CRUD 기능을 정의하고 있는 인터페이스입니다.
 */
public interface PostService {
    /**
     * 게시글 저장
     */
    Long savePost(PostSaveDto postSaveDto, String username);
    /**
     * 게시글 목록
     */
    PageCustomDto<PostTitleViewDto> findPostListByCategory(Category category, Pageable pageable);

    /**
     * 게시글 자세히 보기 + 댓글 목록 출력하기
     */
    PostDetailDto findPostById(Long id,  Pageable commentPageable);

    /**
     * 게시글 좋아요 누르기
     */

    /**
     * 게시글 조회수 증가
     */

    /**
     * 게시글 수정
     */
    Long updatePost(Long id,  PostSaveDto postSaveDto);

    /**
     * 게시글 삭제
     */
    Long deletePost(Long id);
}
