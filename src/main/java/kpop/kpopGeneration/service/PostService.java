package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.*;
import org.springframework.data.domain.Pageable;

/**
 * 게시판 CRUD 기능을 정의하고 있는 인터페이스
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
     * 포스트의 원작자 찾아오기
     */
    String findWriter(Long id);

    /**
     * 포스트 업데이트 정보가져오기
     */
    PostUpdateDto findPostUpdateDto(Long id);


    /**
     * 게시글 자세히 보기 + 댓글 목록 출력하기
     */
    PostDetailDto findPostById(Long id,  Pageable commentPageable);


    /**
     * 게시글 조회수 증가
     */
    void increaseViews(Long id);


    /**
     * 게시글 수정
     */
    Long updatePost(Long id,  PostSaveDto postSaveDto);

    /**
     * 게시글 삭제
     */
    Long deletePost(Long id, String username);
}
