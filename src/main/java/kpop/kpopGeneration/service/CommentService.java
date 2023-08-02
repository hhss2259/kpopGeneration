package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.CommentSaveDto;
import kpop.kpopGeneration.dto.CommentViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    /**
     * 댓글 저장하기
     */
    Long saveComment(CommentSaveDto commentSaveDto, String username);

    /**
     * 댓글 목록 조회하기 (각 포스트별)
     */
    Page<CommentViewDto> findCommentListByPost(Long postId, Pageable pageable);
}
