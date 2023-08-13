package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.CommentSaveDto;
import kpop.kpopGeneration.dto.CommentViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    /**
     * 포스트에 댓글 달기
     */
    Long saveComment(CommentSaveDto commentSaveDto, String username);

    /**
     * 댓글 목록 조회하기 (Ajax)
     */
    Page<CommentViewDto> findCommentListByPost(Long postId, Pageable pageable);


    /**
     * 댓글 좋아요 기능 (좋아요 추가 삭제)
     */

    /**
     * 댓글 삭제 기능
     */

    /**
     * 댓글 수정 기능
     */

    /**
     *  내가 작성한 댓글 모아보기
     */

}
